import google.generativeai as genai
from typing import List, Dict, Any, Optional
from app.core.config import settings
from app.models.schemas import EventWithScore
import json
import re


class LLMVerificationService:

    
    def __init__(self):
        self._initialized = False
        self.model = None
    
    def _ensure_initialized(self):
        if not self._initialized and settings.GEMINI_API_KEY:
            genai.configure(api_key=settings.GEMINI_API_KEY)
            self.model = genai.GenerativeModel('gemini-2.0-flash')
            self._initialized = True
    
    def is_available(self) -> bool:
        available = bool(settings.GEMINI_API_KEY) and settings.ENABLE_LLM_VERIFICATION
        print(f"is_available: {available} (API_KEY: {'YES' if settings.GEMINI_API_KEY else 'NO'}, ENABLED: {settings.ENABLE_LLM_VERIFICATION})")
        return available
    
    async def verify_and_rerank_events(
        self,
        events: List[EventWithScore],
        user_description: str = "",
        preferred_categories: List[str] = None,
        preferred_game_types: List[str] = None,
        top_k: int = 10
    ) -> List[EventWithScore]:
        
        if not events or not self.is_available():
            return events[:top_k]
        
        self._ensure_initialized()
        
        if not self.model:
            return events[:top_k]
        
        try:
            events_to_analyze = events[:20]

            events_text = "\n".join([
                f"{i+1}. [{e.id}] {e.title} | Kategoria: {e.category} | Gra: {e.game_type} | "
                f"Lokalizacja: {e.location} | Opis: {e.description[:150]}..."
                for i, e in enumerate(events_to_analyze)
            ])
            
            prefs = []
            if user_description:
                prefs.append(f"Opis: {user_description}")
            if preferred_categories:
                prefs.append(f"Kategorie: {', '.join(preferred_categories)}")
            if preferred_game_types:
                prefs.append(f"Gry: {', '.join(preferred_game_types)}")
            
            user_prefs_text = "\n".join(prefs) if prefs else "Brak szczegółowych preferencji"
            
            prompt = f"""Jesteś ekspertem od rekomendacji wydarzeń dla graczy karcianki.

PREFERENCJE UŻYTKOWNIKA:
{user_prefs_text}

WYDARZENIA DO OCENY:
{events_text}

ZADANIE:
Przeanalizuj wydarzenia i zwróć ich numery (1-{len(events_to_analyze)}) posortowane od najlepiej dopasowanych do preferencji użytkownika.

Zwróć TYLKO JSON w formacie:
{{"rankings": [1, 5, 3, 2, 8, ...], "reasons": {{"1": "krótki powód", "5": "krótki powód"}}}}

Zwróć maksymalnie {top_k} najlepszych wydarzeń.
WAŻNE: Odpowiedz TYLKO JSON-em, bez dodatkowego tekstu."""

            response = await self.model.generate_content_async(prompt)
            
            response_text = response.text.strip()
            
            if response_text.startswith("```"):
                response_text = re.sub(r'^```(?:json)?\n?', '', response_text)
                response_text = re.sub(r'\n?```$', '', response_text)
            
            result = json.loads(response_text)
            rankings = result.get("rankings", [])
            reasons = result.get("reasons", {})
            
            reranked_events = []
            used_indices = set()
            
            for rank_num in rankings:
                idx = rank_num - 1
                if 0 <= idx < len(events_to_analyze) and idx not in used_indices:
                    event = events_to_analyze[idx]
                    event.llm_reason = reasons.get(str(rank_num), "")
                    reranked_events.append(event)
                    used_indices.add(idx)
            
            for i, event in enumerate(events_to_analyze):
                if i not in used_indices and len(reranked_events) < top_k:
                    reranked_events.append(event)
            
            print(f" Re-ranking zakończony. Zwracam {len(reranked_events[:top_k])} eventów")
            for i, e in enumerate(reranked_events[:top_k][:5]):
                reason = reasons.get(str(rankings[i] if i < len(rankings) else 0), "")
                print(f"[LLM]    {i+1}. {e.title} - {reason[:50]}")
            
            return reranked_events[:top_k]
            
        except json.JSONDecodeError as e:
            print(f" Nieprawidłowy JSON: {e}")
            return events[:top_k]
        except Exception as e:
            print(f"Błąd verification: {e}")
            return events[:top_k]


# Singleton instance
llm_verification_service = LLMVerificationService()