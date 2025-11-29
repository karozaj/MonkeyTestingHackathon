from pydantic_settings import BaseSettings
from functools import lru_cache


class Settings(BaseSettings):
    QDRANT_URL: str = "https://94854cd0-ee8c-4b4f-9ffa-917a9c88f9b0.europe-west3-0.gcp.cloud.qdrant.io:6333"
    QDRANT_API_KEY: str | None = None
    EVENTS_COLLECTION: str = "events_embeddings"
    USERS_COLLECTION: str = "users_embeddings"
    
    EMBEDDING_MODEL: str = "sentence-transformers/all-MiniLM-L6-v2"
    EMBEDDING_DIM: int = 384
    
    DEFAULT_FEED_LIMIT: int = 20
    SEMANTIC_WEIGHT: float = 0.5
    RECENCY_WEIGHT: float = 0.3
    POPULARITY_WEIGHT: float = 0.2
    
    GEMINI_API_KEY: str = ""
    ENABLE_LLM_VERIFICATION: bool = False  
    
    class Config:
        env_file = ".env"
        extra = "allow"


@lru_cache()
def get_settings() -> Settings:
    return Settings()


settings = get_settings()
