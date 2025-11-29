from fastapi import APIRouter, HTTPException, Query
from typing import Optional
from app.models.schemas import EventCreate, EventResponse, EventSearchResponse
from app.services.event_service import event_service

router = APIRouter()


@router.get("/", response_model=EventSearchResponse)
async def search_events(
    category: Optional[str] = Query(default=None, description="Filtruj po kategorii (np. tournament, card_games)"),
    location: Optional[str] = Query(default=None, description="Filtruj po lokalizacji (np. Warsaw, Krakow)"),
    limit: int = Query(default=20, ge=1, le=100),
    offset: int = Query(default=0, ge=0)
) -> EventSearchResponse:
    """
    Wyszukuje wydarzenia z filtrami.
    """
    try:
        events = await event_service.search_events(
            query="",
            category_filter=category,  
            location_filter=location,
            limit=limit + offset
        )
        total = len(events)
        paginated_events = events[offset:offset + limit]
        return EventSearchResponse(
            events=paginated_events,
            total=total,
            has_more=(offset + limit) < total
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Błąd wyszukiwania: {str(e)}")


@router.post("/", response_model=EventResponse)
async def create_event(event: EventCreate) -> EventResponse:
    """
    Tworzy nowe wydarzenie.
    Automatycznie generuje embedding i zapisuje w events_embeddings.
    """
    try:
        return await event_service.create_event(event)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Błąd tworzenia wydarzenia: {str(e)}")


@router.get("/{event_id}", response_model=EventResponse)
async def get_event(event_id: str) -> EventResponse:
    """Pobiera szczegóły wydarzenia."""
    event = await event_service.get_event(event_id)
    if not event:
        raise HTTPException(status_code=404, detail="Wydarzenie nie znalezione")
    return event


@router.post("/{event_id}/join")
async def join_event(event_id: str, user_id: str) -> dict:
    """
    Dołącza użytkownika do wydarzenia.
    Aktualizuje licznik uczestników i zapisuje zachowanie.
    """
    from app.services.user_service import user_service
    from app.models.schemas import UserBehavior
    from datetime import datetime
    
    # Sprawdź czy event istnieje
    event = await event_service.get_event(event_id)
    if not event:
        raise HTTPException(status_code=404, detail="Wydarzenie nie znalezione")
    
    # Aktualizuj liczbę uczestników
    await event_service.update_participants_count(event_id, 1)
    
    # Zapisz zachowanie użytkownika
    behavior = UserBehavior(
        user_id=user_id,
        event_id=event_id,
        action_type="join",
        timestamp=datetime.now()
    )
    await user_service.record_behavior(behavior)
    
    return {"message": "Dołączono do wydarzenia", "event_id": event_id}


@router.post("/{event_id}/leave")
async def leave_event(event_id: str, user_id: str) -> dict:
    """Opuszcza wydarzenie."""
    event = await event_service.get_event(event_id)
    if not event:
        raise HTTPException(status_code=404, detail="Wydarzenie nie znalezione")
    
    await event_service.update_participants_count(event_id, -1)
    
    return {"message": "Opuszczono wydarzenie", "event_id": event_id}


@router.delete("/{event_id}")
async def delete_event(event_id: str) -> dict:
    """Usuwa wydarzenie."""
    success = await event_service.delete_event(event_id)
    if not success:
        raise HTTPException(status_code=404, detail="Nie udało się usunąć wydarzenia")
    return {"message": "Wydarzenie usunięte", "event_id": event_id}
