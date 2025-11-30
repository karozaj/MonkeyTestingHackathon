from datetime import datetime
from typing import Optional
from qdrant_client.http import models
from app.core.config import settings
from app.core.qdrant_client import get_qdrant_client
from app.services.embedding_service import embedding_service
from app.services.llm_verification_services import llm_verification_service
from app.models.schemas import EventWithScore
import math


class RecommendationService:
    """
    Pipeline rekomendacji z hybrid ranking:
    1. Pobiera embedding użytkownika z users_embeddings
    2. Wyszukuje semantycznie podobne eventy w events_embeddings
    3. Łączy wyniki z aktualności (start_time) i popularności (participants)
    4. Zwraca posortowany feed
    """
    
    def __init__(self):
        self.client = get_qdrant_client()
    
    async def get_user_embedding(self, user_id: str) -> Optional[list[float]]:
        try:
            results = self.client.scroll(
                collection_name=settings.USERS_COLLECTION,
                scroll_filter=models.Filter(
                    must=[
                        models.FieldCondition(
                            key="user_id",
                            match=models.MatchValue(value=user_id)
                        )
                    ]
                ),
                limit=1,
                with_vectors=True
            )
            if results and results[0] and len(results[0]) > 0:
                return results[0][0].vector
        except Exception as e:
            print(f"Błąd pobierania user embedding: {e}")
        return None
    
    async def get_user_preferences(self, user_id: str) -> Optional[dict]:
        try:
            results = self.client.scroll(
                collection_name=settings.USERS_COLLECTION,
                scroll_filter=models.Filter(
                    must=[
                        models.FieldCondition(
                            key="user_id",
                            match=models.MatchValue(value=user_id)
                        )
                    ]
                ),
                limit=1,
                with_payload=True
            )
            if results and results[0] and len(results[0]) > 0:
                payload = results[0][0].payload
                return {
                    "description": payload.get("description", ""),
                    "preferred_categories": payload.get("preferred_categories", []),
                    "preferred_game_types": payload.get("preferred_game_types", [])
                }
        except Exception as e:
            print(f"Błąd pobierania preferencji użytkownika: {e}")
        return None
    
    
    
    
    
    async def semantic_search(
        self, 
        query_vector: list[float],
        limit: int = 50,
        category_filter: Optional[str] = None,
        location_filter: Optional[str] = None,
        game_type_filter: Optional[str] = None
    ) -> list[dict]:
        """Wyszukiwanie semantyczne w events_embeddings"""
        
        filter_conditions = None
        must_conditions = []
        
        if category_filter:
            must_conditions.append(
                models.FieldCondition(
                    key="category",
                    match=models.MatchValue(value=category_filter)
                )
            )
        
        if location_filter:
            must_conditions.append(
                models.FieldCondition(
                    key="location",
                    match=models.MatchValue(value=location_filter)
                )
            )
        
        if game_type_filter:
            must_conditions.append(
                models.FieldCondition(
                    key="game_type",
                    match=models.MatchValue(value=game_type_filter)
                )
            )
        
        if must_conditions:
            filter_conditions = models.Filter(must=must_conditions)
        
        results = self.client.query_points(
            collection_name=settings.EVENTS_COLLECTION,
            query=query_vector,
            limit=limit,
            query_filter=filter_conditions,
            with_payload=True
        ).points
        
        return [
            {
                "id": str(hit.id),
                "score": hit.score,
                "payload": hit.payload
            }
            for hit in results
        ]
    
    def calculate_recency_score(self, start_time: datetime) -> float:
        """Oblicza score na podstawie aktualności wydarzenia"""
        now = datetime.now()
        
        if start_time.tzinfo is not None:
            start_time = start_time.replace(tzinfo=None)
        
        if start_time < now:
            return 0.0
        
        # Im bliżej w czasie, tym wyższy score
        days_until = (start_time - now).days
        
        # Exponential decay - wydarzenia w ciągu 7 dni mają najwyższy score
        score = math.exp(-days_until / 7)
        return min(1.0, max(0.0, score))
    
    def calculate_popularity_score(
        self, 
        participants_count: int, 
        max_participants: Optional[int] = None
    ) -> float:
        """Oblicza score na podstawie popularności"""
        if max_participants and max_participants > 0:
            fill_rate = participants_count / max_participants
            return min(1.0, fill_rate)
        else:
            return min(1.0, math.log10(participants_count + 1) / 3)
    
    # Łączenie wszystkiego w jedno
    def hybrid_ranking(
        self,
        semantic_results: list[dict],
        semantic_weight: float = None,
        recency_weight: float = None,
        popularity_weight: float = None
    ) -> list[EventWithScore]:
        """
        Hybrid ranking łączący:
        - Semantyczne podobieństwo z Qdrant
        - Aktualność (start_time)
        - Popularność (liczba uczestników)
        """
        semantic_weight = semantic_weight or settings.SEMANTIC_WEIGHT
        recency_weight = recency_weight or settings.RECENCY_WEIGHT
        popularity_weight = popularity_weight or settings.POPULARITY_WEIGHT
        
        ranked_events = []
        
        for result in semantic_results:
            payload = result["payload"]
            
            start_time = datetime.fromisoformat(payload.get("start_time", datetime.now().isoformat()))
            
            semantic_score = result["score"]
            recency_score = self.calculate_recency_score(start_time)
            popularity_score = self.calculate_popularity_score(
                payload.get("participants_count", 0),
                payload.get("max_participants")
            )
            
            final_score = (
                semantic_weight * semantic_score +
                recency_weight * recency_score +
                popularity_weight * popularity_score
            )
            
            end_time = None
            if payload.get("end_time"):
                end_time = datetime.fromisoformat(payload["end_time"])
            
            ranked_events.append(EventWithScore(
                id=result["id"],
                title=payload.get("title", ""),
                description=payload.get("description", ""),
                category=payload.get("category", ""),
                location=payload.get("location", ""),
                game_type=payload.get("game_type", "Other"),
                start_time=start_time,
                end_time=end_time,
                max_participants=payload.get("max_participants"),
                participants_count=payload.get("participants_count", 0),
                created_at=datetime.fromisoformat(payload.get("created_at", datetime.now().isoformat())),
                semantic_score=semantic_score,
                recency_score=recency_score,
                popularity_score=popularity_score,
                final_score=final_score
            ))
        
        ranked_events.sort(key=lambda x: x.final_score, reverse=True)
        
        return ranked_events
    
    async def get_personalized_feed(
        self,
        user_id: str,
        limit: int = 20,
        offset: int = 0,
        category_filter: Optional[str] = None,
        location_filter: Optional[str] = None,
        game_type_filter: Optional[str] = None,
        use_llm_verification: bool = None  
    ) -> tuple[list[EventWithScore], int]:
        """
        Główny pipeline rekomendacji:
        1. Pobierz embedding użytkownika
        2. Wyszukaj semantycznie z filtrami
        3. Zastosuj hybrid ranking
        4. (Opcjonalnie) Weryfikacja LLM
        """
        print(f"\n{'='*50}")
        print(f"[FEED] 🎯 Generowanie feedu dla user: {user_id}")
        print(f"[FEED] 📋 Parametry: limit={limit}, offset={offset}, category={category_filter}, location={location_filter}, game_type={game_type_filter}")
        
        enable_llm = use_llm_verification if use_llm_verification is not None else settings.ENABLE_LLM_VERIFICATION
        print(f"[FEED] 🤖 LLM verification: {'WŁĄCZONE' if enable_llm else 'WYŁĄCZONE'}")
        
        # 1. Pobierz embedding użytkownika
        print(f"[FEED] 1️⃣ Pobieranie embeddingu użytkownika...")
        user_embedding = await self.get_user_embedding(user_id)
        
        if not user_embedding:
            print(f"[FEED] ⚠️ Brak embeddingu - zwracam domyślny feed")
            return await self._get_default_feed(limit, offset, category_filter, location_filter, game_type_filter)
        
        print(f"[FEED] ✅ Embedding pobrany (dim: {len(user_embedding)})")
        
        # 2. Wyszukiwanie semantyczne
        print(f"[FEED] 2️⃣ Wyszukiwanie semantyczne...")
        semantic_results = await self.semantic_search(
            query_vector=user_embedding,
            limit=limit + offset + 10,  # Pobierz więcej dla offsetu
            category_filter=category_filter,
            location_filter=location_filter,
            game_type_filter=game_type_filter
        )
        print(f"[FEED] ✅ Znaleziono {len(semantic_results)} eventów")
        
        # 3. Hybrid ranking
        print(f"[FEED] 3️⃣ Hybrid ranking...")
        ranked_events = self.hybrid_ranking(semantic_results)
        print(f"[FEED] ✅ Ranking zakończony ({len(ranked_events)} eventów)")
        
        # 4. LLM Verification (opcjonalne)
        if enable_llm and llm_verification_service.is_available():
            print(f"[FEED] 4️⃣ LLM Verification...")
            user_prefs = await self.get_user_preferences(user_id)
            if user_prefs:
                print(f"[FEED] 📝 Preferencje użytkownika: {user_prefs.get('description', '')[:50]}...")
                ranked_events = await llm_verification_service.verify_and_rerank_events(
                    events=ranked_events,
                    user_description=user_prefs.get("description", ""),
                    preferred_categories=user_prefs.get("preferred_categories", []),
                    preferred_game_types=user_prefs.get("preferred_game_types", []),
                    top_k=limit + offset
                )
        else:
            print(f"[FEED] ⏭️ Pomijam LLM verification")
        
        # Zastosuj offset i limit
        total = len(ranked_events)
        paginated_events = ranked_events[offset:offset + limit]
        
        print(f"[FEED] ✅ Zwracam {len(paginated_events)} eventów (total: {total})")
        print(f"{'='*50}\n")
        
        return paginated_events, total
    
    async def _get_default_feed(
        self,
        limit: int,
        offset: int,
        category_filter: Optional[str] = None,
        location_filter: Optional[str] = None,
        game_type_filter: Optional[str] = None
    ) -> tuple[list[EventWithScore], int]:
        """Domyślny feed dla nowych użytkowników - sortowanie po popularności"""
        
        must_conditions = []
        
        if category_filter:
            must_conditions.append(
                models.FieldCondition(
                    key="category",
                    match=models.MatchValue(value=category_filter)
                )
            )
        
        if location_filter:
            must_conditions.append(
                models.FieldCondition(
                    key="location",
                    match=models.MatchValue(value=location_filter)
                )
            )
        
        if game_type_filter:
            must_conditions.append(
                models.FieldCondition(
                    key="game_type",
                    match=models.MatchValue(value=game_type_filter)
                )
            )
        
        filter_conditions = models.Filter(must=must_conditions) if must_conditions else None
        
        # Scroll wszystkie eventy
        results, _ = self.client.scroll(
            collection_name=settings.EVENTS_COLLECTION,
            scroll_filter=filter_conditions,
            with_payload=True,
            limit=100
        )
        
        # Konwertuj na format dla hybrid ranking
        semantic_results = [
            {
                "id": str(point.id),
                "score": 0.5,  # Neutralny semantic score
                "payload": point.payload
            }
            for point in results
        ]
        
        # Hybrid ranking z większą wagą na popularność
        ranked_events = self.hybrid_ranking(
            semantic_results,
            semantic_weight=0.2,
            recency_weight=0.3,
            popularity_weight=0.5
        )
        
        total = len(ranked_events)
        paginated_events = ranked_events[offset:offset + limit]
        
        return paginated_events, total


recommendation_service = RecommendationService()
