from sentence_transformers import SentenceTransformer
from app.core.config import settings
import numpy as np


class EmbeddingService:
    def __init__(self):
        self.model = SentenceTransformer(settings.EMBEDDING_MODEL)
    
    def generate_embedding(self, text: str) -> list[float]:
        """Generuje embedding dla tekstu"""
        embedding = self.model.encode(text)
        return embedding.tolist()
    
    def generate_event_embedding(
        self, 
        title: str, 
        description: str, 
        category: str,
        location: str,
        game_type: str = ""
    ) -> list[float]:
        """Generuje embedding dla wydarzenia"""
        combined_text = f"{title} {description} {category} {location} {game_type}"
        return self.generate_embedding(combined_text)
    def generate_user_embedding(
        self,
        description: str,
        preferred_categories: list[str],
        location: str = "",
        preferred_game_types: list[str] = []
    ) -> list[float]:
        """Generuje embedding dla użytkownika na podstawie jego opisu i preferencji"""
        parts = [description] + preferred_categories + preferred_game_types
        if location:
            parts.append(location)
        combined_text = " ".join(parts)
        return self.generate_embedding(combined_text)
  


embedding_service = EmbeddingService()
