[README.md](https://github.com/user-attachments/files/23838677/README.md)
# Event Recommendation API

API do rekomendacji wydarzeń karcianych (TCG) z wykorzystaniem Qdrant i opcjonalną weryfikacją LLM (Gemini).

## 🏗️ Architektura

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Android App    │────▶│   FastAPI       │────▶│    Qdrant       │
│                 │◀────│   Backend       │◀────│   (2 kolekcje)  │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                              │
                              ▼ (opcjonalnie)
                        ┌─────────────────┐
                        │  Gemini LLM     │
                        │  (weryfikacja)  │
                        └─────────────────┘
```

## 📦 Kolekcje w Qdrant

1. **events_embeddings** - embeddingi wydarzeń (tytuł, opis, kategoria, lokalizacja, game_type)
2. **users_embeddings** - embeddingi użytkowników (zainteresowania, preferencje, lokalizacja)

## 🎮 Obsługiwane gry karciane (TCG)

- Pokemon TCG
- Magic: The Gathering (MTG)
- Disney Lorcana
- Yu-Gi-Oh
- Riftbound
- One Piece
- Flesh and Blood
- Star Wars Unlimited
- KeyForge
- Altered

## 🔄 Pipeline Rekomendacji

```
1. Pobierz embedding użytkownika z users_embeddings
2. Wyszukaj semantycznie w events_embeddings
3. Zastosuj filtry (category, location, game_type)
4. Zastosuj Hybrid Ranking:
   - Semantic similarity: 50%
   - Recency (start_time): 30%
   - Popularity (participants): 20%
5. (Opcjonalnie) Weryfikacja LLM (Gemini)
6. Zwróć posortowany feed
```

## 🚀 Uruchomienie


### Lokalne uruchomienie

#### 1. Uruchom Qdrant (Docker)
```bash
docker run -p 6333:6333 -p 6334:6334 qdrant/qdrant:latest
```

#### 2. Zainstaluj zależności
```bash
pip install -r requirements.txt
```

#### 3. Skonfiguruj zmienne środowiskowe (.env)
```env
QDRANT_URL=http://localhost:6333
QDRANT_API_KEY=your_api_key
GEMINI_API_KEY=your_gemini_key
ENABLE_LLM_VERIFICATION=false
```

#### 4. Uruchom API
```bash
python run.py
```

API będzie dostępne pod: `http://localhost:8000`

Dokumentacja Swagger: `http://localhost:8000/docs`

## 📡 Endpointy API

### Feed - Spersonalizowane rekomendacje

#### GET /api/v1/feed/{user_id}
```
GET /api/v1/feed/{user_id}?limit=20&offset=0&category=tournament&location=Warsaw&game_type=Pokemon&use_llm=true
```

Parametry:
- `limit` - liczba wyników (1-100, domyślnie 20)
- `offset` - przesunięcie (domyślnie 0) ile eventow ma dodatkowo zwracać
- `category` - filtr kategorii (np. tournament, card_games)
- `location` - filtr lokalizacji (np. Warsaw, Krakow)
- `game_type` - filtr gry (np. Pokemon, MTG, Lorcana)
- `use_llm` - włącz weryfikację LLM (domyślnie wg ustawień serwera)

#### POST /api/v1/feed/
```json
{
    "user_id": "uuid",
    "limit": 20,
    "offset": 0,
    "category_filter": "tournament",
    "location_filter": "Warsaw",
    "game_type_filter": "Pokemon",
    "use_llm_verification": true
}
```

### Events - Zarządzanie wydarzeniami

```
GET  /api/v1/events/              # Wyszukaj wydarzenia (filtry: category, location)
POST /api/v1/events/              # Utwórz wydarzenie
GET  /api/v1/events/{event_id}    # Pobierz wydarzenie
POST /api/v1/events/{id}/join     # Dołącz do wydarzenia
POST /api/v1/events/{id}/leave    # Opuść wydarzenie
DELETE /api/v1/events/{event_id}  # Usuń wydarzenie
```

### Users - Zarządzanie użytkownikami

```
POST /api/v1/users/                      # Utwórz użytkownika
GET  /api/v1/users/{user_id}             # Pobierz użytkownika
PUT  /api/v1/users/{id}/preferences      # Aktualizuj preferencje
```

## 📋 Schematy danych

### EventCreate
```json
{
    "title": "Pokemon TCG Tournament",
    "description": "Turniej Pokemon dla wszystkich poziomów",
    "category": "tournament",
    "location": "Warsaw",
    "game_type": "Pokemon TCG",
    "start_time": "2025-12-01T14:00:00",
    "end_time": "2025-12-01T20:00:00",
    "max_participants": 32
}
```

### UserCreate
```json
{
    "user_id": "uuid",
    "username": "player123",
    "location": "Warszawa",
    "description": "Bardziej social niż competitive, ważna jest atmosfera",
    "preferred_categories": ["tournament", "casual"],
    "preferred_game_types": ["Pokemon TCG", "Disney Lorcana"]
}
```

### FeedResponse
```json
{
    "events": [
        {
            "id": "uuid",
            "title": "Pokemon TCG Tournament",
            "description": "...",
            "category": "tournament",
            "location": "Warsaw",
            "game_type": "Pokemon TCG",
            "start_time": "2025-12-01T14:00:00",
            "participants_count": 12,
            "created_at": "2025-11-29T10:00:00",
            "semantic_score": 0.85,
            "recency_score": 0.90,
            "popularity_score": 0.75,
            "final_score": 0.84
        }
    ],
    "total": 50,
    "has_more": true
}
```

## 🧪 Testowanie

```bash
# Health check
curl http://localhost:8000/health

# Utwórz użytkownika
curl -X POST http://localhost:8000/api/v1/users/ \
  -H "Content-Type: application/json" \
  -d '{
    "user_id": "user-123",
    "username": "test",
    "location": "Warszawa",
    "description": "Lubię turnieje Pokemon",
    "preferred_categories": ["tournament"],
    "preferred_game_types": ["Pokemon TCG"]
  }'

# Utwórz wydarzenie
curl -X POST http://localhost:8000/api/v1/events/ \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Pokemon TCG Tournament",
    "description": "Turniej dla wszystkich poziomów",
    "category": "tournament",
    "location": "Warsaw",
    "game_type": "Pokemon TCG",
    "start_time": "2025-12-01T14:00:00"
  }'

# Pobierz spersonalizowany feed
curl "http://localhost:8000/api/v1/feed/user-123?game_type=Pokemon&limit=10"

# Wyszukaj wydarzenia
curl "http://localhost:8000/api/v1/events/?category=tournament&location=Warsaw"
```

## ⚙️ Konfiguracja

| Zmienna | Opis | Domyślnie |
|---------|------|-----------|
| `QDRANT_URL` | URL do Qdrant | `http://localhost:6333` |
| `QDRANT_API_KEY` | Klucz API Qdrant | - |
| `GEMINI_API_KEY` | Klucz API Gemini (dla LLM) | - |
| `ENABLE_LLM_VERIFICATION` | Włącz weryfikację LLM | `false` |
| `SEMANTIC_WEIGHT` | Waga podobieństwa semantycznego | `0.5` |
| `RECENCY_WEIGHT` | Waga świeżości | `0.3` |
| `POPULARITY_WEIGHT` | Waga popularności | `0.2` |


