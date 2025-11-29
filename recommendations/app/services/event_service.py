from datetime import datetime
from uuid import uuid4
from typing import Optional
from qdrant_client.http import models
from app.core.config import settings
from app.core.qdrant_client import get_qdrant_client
from app.services.embedding_service import embedding_service
from app.models.schemas import EventCreate, EventResponse


class EventService:
    def __init__(self):
        self.client = get_qdrant_client()
    
    async def create_event(self, event: EventCreate) -> EventResponse:
        """Tworzy nowe wydarzenie i zapisuje embedding w Qdrant"""
        
        event_id = str(uuid4())
        created_at = datetime.now()
          # Generuj embedding
        embedding = embedding_service.generate_event_embedding(
            title=event.title,
            description=event.description,
            category=event.category,
            location=event.location,
            game_type=event.game_type
        )
        
        # Payload do Qdrant
        payload = {
            "title": event.title,
            "description": event.description,
            "category": event.category,
            "location": event.location,
            "game_type": event.game_type,
            "start_time": event.start_time.isoformat(),
            "end_time": event.end_time.isoformat() if event.end_time else None,
            "max_participants": event.max_participants,
            "participants_count": 0,
            "created_at": created_at.isoformat()
        }
        
        # Zapisz do Qdrant
        self.client.upsert(
            collection_name=settings.EVENTS_COLLECTION,
            points=[
                models.PointStruct(
                    id=event_id,
                    vector=embedding,
                    payload=payload
                )
            ]
        )
        
        return EventResponse(
            id=event_id,
            title=event.title,
            description=event.description,
            category=event.category,
            location=event.location,
            game_type=event.game_type,
            start_time=event.start_time,
            end_time=event.end_time,
            max_participants=event.max_participants,
            participants_count=0,
            created_at=created_at
        )
    
    async def get_event(self, event_id: str) -> EventResponse | None:
        """Pobiera wydarzenie z Qdrant"""
        try:
            result = self.client.retrieve(
                collection_name=settings.EVENTS_COLLECTION,
                ids=[event_id],
                with_payload=True
            )
            
            if result and len(result) > 0:
                payload = result[0].payload
                return EventResponse(
                    id=event_id,
                    title=payload["title"],
                    description=payload["description"],
                    category=payload["category"],
                    location=payload["location"],
                    game_type=payload.get("game_type", "Other"),
                    start_time=datetime.fromisoformat(payload["start_time"]),
                    end_time=datetime.fromisoformat(payload["end_time"]) if payload.get("end_time") else None,
                    max_participants=payload.get("max_participants"),
                    participants_count=payload.get("participants_count", 0),
                    created_at=datetime.fromisoformat(payload["created_at"])
                )
        except Exception as e:
            print(f"Błąd pobierania eventu: {e}")
        return None
    
    async def update_participants_count(self, event_id: str, delta: int) -> bool:
        """Aktualizuje liczbę uczestników"""
        try:
            result = self.client.retrieve(
                collection_name=settings.EVENTS_COLLECTION,
                ids=[event_id],
                with_payload=True
            )
            
            if result and len(result) > 0:
                current_count = result[0].payload.get("participants_count", 0)
                new_count = max(0, current_count + delta)
                
                self.client.set_payload(
                    collection_name=settings.EVENTS_COLLECTION,
                    payload={"participants_count": new_count},
                    points=[event_id]
                )
                return True
        except Exception as e:
            print(f"Błąd aktualizacji uczestników: {e}")
        return False
    
    async def search_events(
        self,
        query: str = "",
        limit: int = 20,
        category_filter: Optional[str] = None,
        location_filter: Optional[str] = None,
        game_type_filter: Optional[str] = None
    ) -> list[EventResponse]:
        """Wyszukuje eventy tekstowo lub z filtrami"""
        
        # Buduj filtry
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
        
        # Jeśli jest query - użyj wyszukiwania semantycznego
        if query and query.strip():
            query_embedding = embedding_service.generate_embedding(query)
            results = self.client.query_points(
                collection_name=settings.EVENTS_COLLECTION,
                query=query_embedding,
                limit=limit,
                query_filter=filter_conditions,
                with_payload=True
            ).points
        else:
            # Bez query - użyj scroll z filtrami
            results, _ = self.client.scroll(
                collection_name=settings.EVENTS_COLLECTION,
                scroll_filter=filter_conditions,
                limit=limit,
                with_payload=True
            )
        
        # Konwertuj na EventResponse
        events = []
        for hit in results:
            payload = hit.payload
            events.append(EventResponse(
                id=str(hit.id),
                title=payload["title"],
                description=payload["description"],
                category=payload["category"],
                location=payload["location"],
                game_type=payload.get("game_type", "Other"),
                start_time=datetime.fromisoformat(payload["start_time"]),
                end_time=datetime.fromisoformat(payload["end_time"]) if payload.get("end_time") else None,
                max_participants=payload.get("max_participants"),
                participants_count=payload.get("participants_count", 0),
                created_at=datetime.fromisoformat(payload["created_at"])
            ))
        
        return events
    
    async def get_all_events(self, limit: int = 100) -> list[EventResponse]:
        """Pobiera wszystkie eventy"""
        results, _ = self.client.scroll(
            collection_name=settings.EVENTS_COLLECTION,
            with_payload=True,
            limit=limit
        )
        
        events = []
        for point in results:
            payload = point.payload
            events.append(EventResponse(
                id=str(point.id),
                title=payload["title"],
                description=payload["description"],
                category=payload["category"],
                location=payload["location"],
                game_type=payload.get("game_type", "Other"),
                start_time=datetime.fromisoformat(payload["start_time"]),
                end_time=datetime.fromisoformat(payload["end_time"]) if payload.get("end_time") else None,
                max_participants=payload.get("max_participants"),
                participants_count=payload.get("participants_count", 0),
                created_at=datetime.fromisoformat(payload["created_at"])
            ))
        
        return events


event_service = EventService()
