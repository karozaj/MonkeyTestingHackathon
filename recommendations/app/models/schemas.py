from pydantic import BaseModel
from datetime import datetime
from typing import Optional
from enum import Enum


# === Location Enum ===
class LocationEnum(str, Enum):
    """Available locations for users."""
    WARSAW = "Warszawa"
    KRAKOW = "Krakow"
    GDANSK = "Gdansk"
    POZNAN = "Poznan"
    WROCLAW = "Wroclaw"
    LODZ = "Lodz"
    SOPOT = "Sopot"
    KATOWICE = "Katowice"
    LUBLIN = "Lublin"
    SZCZECIN = "Szczecin"


# === Game Type Enum ===
class GameTypeEnum(str, Enum):
    """Types of trading card games."""
    POKEMON = "Pokemon TCG"
    MTG = "Magic: The Gathering"  
    LORCANA = "Disney Lorcana"
    YUGIOH = "Yu-Gi-Oh"
    RIFTBOUND = "Riftbound"  
    ONE_PIECE = "One Piece"
    FLESH_AND_BLOOD = "Flesh and Blood"
    STAR_WARS = "Star Wars Unlimited"
    KEYFORGE = "KeyForge"
    ALTERED = "Altered"
    OTHER = "Other"


# === Event Schemas ===
class EventBase(BaseModel):
    title: str
    description: str
    category: str
    location: str
    game_type: str  # Pokemon, MTG, Lorcana, Yu-Gi-Oh, LoL, etc.
    start_time: datetime
    end_time: Optional[datetime] = None
    max_participants: Optional[int] = None


class EventCreate(EventBase):
    pass


class EventResponse(EventBase):
    id: str
    participants_count: int = 0
    created_at: datetime
    
    class Config:
        from_attributes = True


class EventWithScore(EventResponse):
    semantic_score: float
    recency_score: float
    popularity_score: float
    final_score: float


# === User Schemas ===
class UserBase(BaseModel):
    user_id: str
    username: str
    location: LocationEnum
    description: str = ""  # Opis użytkownika, np. "Bardziej social niż competitive, ważna jest atmosfera i ludzie"
    preferred_categories: list[str] = []
    preferred_game_types: list[str] = []  # Pokemon, MTG, Lorcana, etc.


class UserCreate(UserBase):
    pass


class UserResponse(UserBase):
    id: str
    created_at: datetime
    
    class Config:
        from_attributes = True


# === User Behavior Schemas ===
class UserBehavior(BaseModel):
    user_id: str
    event_id: str
    action_type: str  # "view", "like", "join", "share"
    timestamp: datetime = datetime.now()


# === Feed Schemas ===
class FeedRequest(BaseModel):
    user_id: str
    limit: int = 20
    offset: int = 0
    category_filter: Optional[str] = None
    location_filter: Optional[str] = None
    game_type_filter: Optional[str] = None  # Pokemon, MTG, Lorcana, etc.
    use_llm_verification: Optional[bool] = None  # Włącz weryfikację LLM (domyślnie wg ustawień serwera)


class FeedResponse(BaseModel):
    events: list[EventWithScore]
    total: int
    has_more: bool


# === Event Search Schemas ===
class EventSearchResponse(BaseModel):
    events: list[EventResponse]
    total: int
    has_more: bool
