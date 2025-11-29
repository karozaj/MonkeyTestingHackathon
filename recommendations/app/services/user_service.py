from datetime import datetime
from typing import Optional
from qdrant_client.http import models
from app.core.config import settings
from app.core.qdrant_client import get_qdrant_client
from app.services.embedding_service import embedding_service
from app.models.schemas import UserCreate, UserResponse, UserBehavior
import uuid


class UserService:
    """
    Serwis zarządzający użytkownikami i ich embeddingami.
    """
    
    def __init__(self):
        self.client = get_qdrant_client()
    
    async def create_user(self, user: UserCreate) -> UserResponse:
        """Tworzy nowego użytkownika i generuje embedding na podstawie description"""
        
        user_embedding = embedding_service.generate_user_embedding(
            description=user.description,
            preferred_categories=user.preferred_categories,
            location=user.location.value,
            preferred_game_types=user.preferred_game_types
        )
        
        point_id = str(uuid.uuid4())
        created_at = datetime.now()
        
        self.client.upsert(
            collection_name=settings.USERS_COLLECTION,
            points=[
                models.PointStruct(
                    id=point_id,
                    vector=user_embedding,
                    payload={
                        "user_id": user.user_id, 
                        "username": user.username,
                        "location": user.location.value,
                        "description": user.description,
                        "preferred_categories": user.preferred_categories,
                        "preferred_game_types": user.preferred_game_types,
                        "created_at": created_at.isoformat()
                    }
                )
            ]
        )
        
        return UserResponse(
            id=point_id,
            user_id=user.user_id,
            username=user.username,
            location=user.location,
            description=user.description,
            preferred_categories=user.preferred_categories,
            preferred_game_types=user.preferred_game_types,
            created_at=created_at
        )
    
    async def get_user(self, user_id: str) -> Optional[UserResponse]:
        """Pobiera dane użytkownika z Qdrant po user_id w payload"""
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
                point = results[0][0]
                payload = point.payload
                from app.models.schemas import LocationEnum
                
                return UserResponse(
                    id=str(point.id),
                    user_id=payload.get("user_id", user_id),
                    username=payload.get("username", ""),
                    location=LocationEnum(payload.get("location", "Warszawa")),
                    description=payload.get("description", ""),
                    preferred_categories=payload.get("preferred_categories", []),
                    preferred_game_types=payload.get("preferred_game_types", []),
                    created_at=datetime.fromisoformat(payload.get("created_at", datetime.now().isoformat()))
                )
        except Exception as e:
            print(f"Błąd pobierania użytkownika: {e}")
        return None
    
    async def update_user_embedding(
        self,
        user_id: str,
        description: str,
        preferred_categories: list[str]
    ) -> bool:
        """Aktualizuje embedding użytkownika"""
        try:
            user = await self.get_user(user_id)
            if not user:
                return False
            
            new_embedding = embedding_service.generate_user_embedding(
                description=description,
                preferred_categories=preferred_categories,
                location=user.location.value,
                preferred_game_types=user.preferred_game_types
            )
            
            self.client.upsert(
                collection_name=settings.USERS_COLLECTION,
                points=[
                    models.PointStruct(
                        id=user.id,  
                        vector=new_embedding,
                        payload={
                            "user_id": user_id,  
                            "username": user.username,
                            "location": user.location.value,
                            "description": description,
                            "preferred_categories": preferred_categories,
                            "preferred_game_types": user.preferred_game_types,
                            "created_at": user.created_at.isoformat()
                        }
                    )
                ]
            )
            return True
        except Exception as e:
            print(f"Błąd aktualizacji embeddingu: {e}")
            return False

    async def record_behavior(self, behavior) -> bool:
        """Zapisuje zachowanie użytkownika (join, view, like, share)"""
        try:
            print(f"[USER] 📝 Zapisuję zachowanie: user={behavior.user_id}, event={behavior.event_id}, action={behavior.action_type}")
            # TODO: W przyszłości można zapisywać do osobnej kolekcji dla analizy
            return True
        except Exception as e:
            print(f"Błąd zapisywania zachowania: {e}")
            return False


user_service = UserService()
