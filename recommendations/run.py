import uvicorn
from app.core.qdrant_client import qdrant_manager
import asyncio


async def init():
    await qdrant_manager.init_collections()


if __name__ == "__main__":
    asyncio.run(init())
    
    uvicorn.run(
        "app.main:app",
        host="0.0.0.0",
        port=8000,
        reload=True
    )

