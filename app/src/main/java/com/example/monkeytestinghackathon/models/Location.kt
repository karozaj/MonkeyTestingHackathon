package com.example.monkeytestinghackathon.models

enum class Location(
    val key: String,
    val value: String
) {
    WARSAW("WARSAW", "Warszawa"),
    KRAKOW("KRAKOW", "Krakow"),
    GDANSK("GDANSK", "Gdansk"),
    POZNAN("POZNAN", "Poznan"),
    WROCLAW("WROCLAW", "Wroclaw"),
    LODZ("LODZ", "Łodz"),
    SOPOT("SOPOT", "Sopot"),
    KATOWICE("KATOWICE", "Katowice"),
    LUBLIN("LUBLIN", "Lublin"),
    SZCZECIN("SZCZECIN", "Szczecin");


    companion object {
        fun locationFromKey(key: String): Location? =
            Location.entries.find { it.key == key }
    }
}