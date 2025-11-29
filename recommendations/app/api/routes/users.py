from fastapi import APIRouter, HTTPException
from app.models.schemas import UserCreate, UserResponse
from app.services.user_service import user_service

router = APIRouter()


@router.post("/", response_model=UserResponse)
async def create_user(user: UserCreate) -> UserResponse:
    """
    Tworzy nowego użytkownika.
    Automatycznie generuje embedding na podstawie zainteresowań
    i zapisuje w users_embeddings.
    """
    try:
        return await user_service.create_user(user)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Błąd tworzenia użytkownika: {str(e)}")


@router.get("/{user_id}", response_model=UserResponse)
async def get_user(user_id: str) -> UserResponse:
    """Pobiera dane użytkownika."""
    user = await user_service.get_user(user_id)
    if not user:
        raise HTTPException(status_code=404, detail="Użytkownik nie znaleziony")
    return user


@router.put("/{user_id}/preferences")
async def update_user_preferences(
    user_id: str,
    description: str,
    preferred_categories: list[str]
) -> dict:
    """
    Aktualizuje preferencje użytkownika.
    Przelicza embedding użytkownika w users_embeddings.
    """
    success = await user_service.update_user_embedding(
        user_id=user_id,
        description=description,
        preferred_categories=preferred_categories
    )
    
    if not success:
        raise HTTPException(status_code=404, detail="Nie udało się zaktualizować preferencji")
    
    return {"message": "Preferencje zaktualizowane", "user_id": user_id}
