package com.example.monkeytestinghackathon.models

enum class CardGameTypes(
    val key: String,
    val value: String
) {
    MTG("mtg", "Magic: The Gathering"),
    POKEMON("pokemon", "Pokemon TCG"),
    YUGIOH("yugioh", "Yu-Gi-Oh"),
    LORCANA("lorcana", "Disney Lorcana"),
    RIFTBOUND("riftbound", "Riftbound"),
    ONE_PIECE("one_piece", "One Piece"),
    FLESH_AND_BLOOD("flesh_and_blood", "Flesh and Blood"),
    STAR_WARS("star_wars", "Star Wars Unlimited"),
    KEYFORGE("keyforge", "KeyForge"),
    ALTERED("altered", "Altered"),
    OTHER("other", "Other");

    companion object {
        fun gameTypesFromKey(key: String): CardGameTypes? =
            entries.find { it.key == key }
    }
}