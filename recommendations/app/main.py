from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager
from app.api.routes import feed, events, users
from app.core.config import settings
from app.core.qdrant_client import get_qdrant_client
from qdrant_client.http import models

@asynccontextmanager
async def lifespan(app: FastAPI):
    """Startup and shutdown events"""
    client = get_qdrant_client()
    
    try:
        client.create_payload_index(
            collection_name=settings.EVENTS_COLLECTION,
            field_name="category",
            field_schema=models.PayloadSchemaType.KEYWORD
        )
    except Exception:
        pass 
    
    try:
        client.create_payload_index(
            collection_name=settings.EVENTS_COLLECTION,
            field_name="location",
            field_schema=models.PayloadSchemaType.KEYWORD
        )
    except Exception:
        pass
    
    try:
        client.create_payload_index(
            collection_name=settings.EVENTS_COLLECTION,
            field_name="game_type",
            field_schema=models.PayloadSchemaType.KEYWORD
        )
    except Exception:
        pass
    
    print("Qdrantverified")
    yield


app = FastAPI(
    title="Event Recommendation API",
    description="API do rekomendacji wydarzeń z wykorzystaniem Qdrant",
    version="1.0.0",
    lifespan=lifespan
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(feed.router, prefix="/api/v1/feed", tags=["Feed"])
app.include_router(events.router, prefix="/api/v1/events", tags=["Events"])
app.include_router(users.router, prefix="/api/v1/users", tags=["Users"])


@app.get("/")
async def root():
    return {"message": "Event Recommendation API", "status": "running"}


@app.get("/health")
async def health_check():
    return {"status": "healthy"}
