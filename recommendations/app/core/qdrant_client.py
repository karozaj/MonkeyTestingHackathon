from qdrant_client import QdrantClient
from qdrant_client.http import models
from app.core.config import settings


class QdrantManager:
    def __init__(self):
        self.client = QdrantClient(
            url=settings.QDRANT_URL,
            api_key=settings.QDRANT_API_KEY
        )
    
    def _collection_exists(self, collection_name: str) -> bool:
        """Check if collection exists (compatible with qdrant-client 1.7.0)"""
        try:
            collections = self.client.get_collections().collections
            return any(c.name == collection_name for c in collections)
        except Exception:
            return False
    
    async def init_collections(self):
        """Inicjalizacja kolekcji w Qdrant"""
        collections = [
            settings.EVENTS_COLLECTION,
            settings.USERS_COLLECTION
        ]
        
        for collection_name in collections:
            if not self._collection_exists(collection_name):
                self.client.create_collection(
                    collection_name=collection_name,
                    vectors_config=models.VectorParams(
                        size=settings.EMBEDDING_DIM,
                        distance=models.Distance.COSINE
                    )
                )
                print(f"Utworzono kolekcję: {collection_name}")
        
        await self._create_payload_indexes()
    
    async def _create_payload_indexes(self):
        """Create payload indexes for filtering fields"""
        events_indexes = [
            ("category", models.PayloadSchemaType.KEYWORD),
            ("location", models.PayloadSchemaType.KEYWORD),
        ]
        
        for field_name, field_type in events_indexes:
            try:
                self.client.create_payload_index(
                    collection_name=settings.EVENTS_COLLECTION,
                    field_name=field_name,
                    field_schema=field_type
                )
                print(f"Utworzono indeks dla pola: {field_name} w kolekcji {settings.EVENTS_COLLECTION}")
            except Exception as e:
                if "already exists" not in str(e).lower():
                    print(f"Uwaga przy tworzeniu indeksu {field_name}: {e}")
                users_indexes = [
            ("location", models.PayloadSchemaType.KEYWORD),
        ]
        
        for field_name, field_type in users_indexes:
            try:
                self.client.create_payload_index(
                    collection_name=settings.USERS_COLLECTION,
                    field_name=field_name,
                    field_schema=field_type
                )
                print(f"Utworzono indeks dla pola: {field_name} w kolekcji {settings.USERS_COLLECTION}")
            except Exception as e:
                if "already exists" not in str(e).lower():
                    print(f"Uwaga przy tworzeniu indeksu {field_name}: {e}")
    
    def get_client(self) -> QdrantClient:
        return self.client


qdrant_manager = QdrantManager()


def get_qdrant_client() -> QdrantClient:
    return qdrant_manager.get_client()
