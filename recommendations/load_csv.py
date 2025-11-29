import asyncio
import sys
import os
import csv
from datetime import datetime

# Add project root to path
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

from app.models.schemas import EventCreate, UserCreate, LocationEnum
from app.services.event_service import EventService
from app.services.user_service import UserService


def load_events_from_csv(file_path: str) -> list[dict]:
    """Load events from CSV file."""
    events = []
    
    with open(file_path, 'r', encoding='utf-8') as file:
        reader = csv.DictReader(file)
        
        for row in reader:
            # Use category as string directly
            category = row["category"].strip()
            
            event = {
                "title": row["title"].strip(),
                "description": row["description"].strip(),
                "category": category,
                "location": row["location"].strip(),
                "game_type": row.get("game_type", "Other").strip() if row.get("game_type") else "Other",
                "start_time": datetime.fromisoformat(row["start_time"].strip()),
                "end_time": datetime.fromisoformat(row["end_time"].strip()) if row.get("end_time") and row["end_time"].strip() else None,
                "max_participants": int(row["max_participants"]) if row.get("max_participants") and row["max_participants"].strip() else None
            }
            events.append(event)

    return events


def load_users_from_csv(file_path: str) -> list[dict]:
    """Load users from CSV file."""
    users = []
    
    with open(file_path, 'r', encoding='utf-8') as file:
        reader = csv.DictReader(file)
        
        for row in reader:            # Parse description and preferred_categories
            description = row.get("description", "").strip()
            preferred_categories = [c.strip() for c in row["preferred_categories"].split(",")] if row.get("preferred_categories") else []
            preferred_game_types = [g.strip() for g in row["preferred_game_types"].split(",")] if row.get("preferred_game_types") else []
            
            # Map location string to enum
            location_str = row["location"].strip() if row.get("location") else "Warsaw"
            try:
                location = LocationEnum(location_str)
            except ValueError:
                print(f"⚠️ Unknown location '{location_str}', defaulting to Warsaw")
                location = LocationEnum.WARSAW
            
            user = {
                "user_id": row["user_id"].strip() if row.get("user_id") else f"usr_{row['username'].strip()}",
                "username": row["username"].strip(),
                "location": location,
                "description": description,
                "preferred_categories": preferred_categories,
                "preferred_game_types": preferred_game_types
            }
            users.append(user)

    return users


async def seed_events_from_csv(csv_path: str):
    """add point to qdrant"""
    print(f"Loading events from: {csv_path}")
    if not os.path.exists(csv_path):
        print(f"File: {csv_path}")
        return
    events_data = load_events_from_csv(csv_path)    
    event_service = EventService()
    
    success_count = 0
    fail_count = 0
    
    for i, event_data in enumerate(events_data, 1):
        try:
            event = EventCreate(**event_data)
            created_event = await event_service.create_event(event)
            print(f"[{i}/{len(events_data)}] Created event: {created_event.title}")
            success_count += 1
        except Exception as e:
            print(f" [{i}/{len(events_data)}] Failed: {event_data['title']} - {e}")
            fail_count += 1
    
    print(f"\n  Events success: {success_count}")
    print(f"  Events failed: {fail_count}")
    return success_count, fail_count


async def seed_users_from_csv(csv_path: str):
    """Seed Qdrant database with users from CSV file."""
    print(f"\nLoading users from: {csv_path}")
    
    if not os.path.exists(csv_path):
        print(f" File not found: {csv_path}")
        return
    
    users_data = load_users_from_csv(csv_path)
    print(f"Found {len(users_data)} users in CSV\n")
    
    # Create user service instance
    user_service = UserService()
    
    success_count = 0
    fail_count = 0
    
    for i, user_data in enumerate(users_data, 1):
        try:
            user = UserCreate(**user_data)
            created_user = await user_service.create_user(user)
            print(f"[{i}/{len(users_data)}] Created user: {created_user.username}")
            success_count += 1
        except Exception as e:
            print(f"[{i}/{len(users_data)}] Failed: {user_data['username']} - {e}")
            fail_count += 1
    
    print(f"\n  Users success: {success_count}")
    print(f"  Users failed: {fail_count}")
    return success_count, fail_count


async def seed_all():
    """Seed both events and users from CSV files."""
    events_csv = os.path.join(
        os.path.dirname(os.path.abspath(__file__)),
        "event_data.csv"
    )
    users_csv = os.path.join(
        os.path.dirname(os.path.abspath(__file__)),
        "user_data.csv"
    )
    
    print(" Qdrant CSV Seeder")
    print("=" * 50)
    
    # Seed events
    await seed_events_from_csv(events_csv)
    
    # Seed users
    await seed_users_from_csv(users_csv)
    
    print("\n" + "=" * 50)
    print(" Seeding complete!")


if __name__ == "__main__":
    asyncio.run(seed_all())
