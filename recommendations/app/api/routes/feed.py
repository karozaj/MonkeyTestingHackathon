from fastapi import APIRouter, HTTPException, Query
from typing import Optional
from app.models.schemas import FeedRequest, FeedResponse
from app.services.recommendation_service import recommendation_service

router = APIRouter()


@router.get("/{user_id}")
async def get_personalized_feed(
    user_id: str,
    limit: int = Query(default=20, ge=1, le=100),
    offset: int = Query(default=0, ge=0),
    category: Optional[str] = Query(default=None, description="Filtruj po kategorii (np. tournament, card_games)"),
    location: Optional[str] = Query(default=None, description="Filtruj po lokalizacji (np. Warsaw, Krakow)"),
    game_type: Optional[str] = Query(default=None, description="Filtruj po rodzaju gry (np. Pokemon, MTG, Lorcana)"),
    use_llm: Optional[bool] = Query(default=None, description="Włącz weryfikację LLM (Gemini) dla lepszego dopasowania")
) -> FeedResponse:
    """
    Pobiera spersonalizowany feed dla użytkownika.
    
    Pipeline:
    1. Pobiera embedding użytkownika z users_embeddings
    2. Wyszukuje semantycznie w events_embeddings
    3. Stosuje hybrid ranking (semantic + recency + popularity)
    4. (Opcjonalnie) Weryfikacja LLM dla lepszego dopasowania
    5. Zwraca posortowaną listę wydarzeń
    
    Filtorwanie:
    - category: filtruj po kategorii wydarzenia
    - location: filtruj po lokalizacji wydarzenia
    - game_type: filtruj po rodzaju gry karcianej (Pokemon, MTG, Lorcana, Yu-Gi-Oh, LoL, etc.)
    - use_llm: włącz/wyłącz weryfikację Gemini (domyślnie wg ustawień serwera)
    """
    try:
        events, total = await recommendation_service.get_personalized_feed(
            user_id=user_id,
            limit=limit,
            offset=offset,
            category_filter=category,
            location_filter=location,
            game_type_filter=game_type,
            use_llm_verification=use_llm
        )
        
        return FeedResponse(
            events=events,
            total=total,
            has_more=(offset + limit) < total
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Błąd pobierania feed: {str(e)}")


@router.post("/")
async def get_feed_with_options(request: FeedRequest) -> FeedResponse:
    """
    Pobiera feed z dodatkowymi opcjami (POST request).
    Używane gdy potrzebujemy przekazać więcej parametrów.
    
    Opcje:
    - use_llm_verification: włącz weryfikację Gemini dla lepszego dopasowania
    """
    try:
        events, total = await recommendation_service.get_personalized_feed(
            user_id=request.user_id,
            limit=request.limit,
            offset=request.offset,
            category_filter=request.category_filter,
            location_filter=request.location_filter,
            game_type_filter=request.game_type_filter,
            use_llm_verification=request.use_llm_verification
        )
        
        return FeedResponse(
            events=events,
            total=total,
            has_more=(request.offset + request.limit) < total
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Błąd pobierania feed: {str(e)}")
