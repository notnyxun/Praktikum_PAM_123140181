package com.example.myapplication.model

enum class Category(
    val displayName: String,
    val tagLabel: String
) {
    ALL("Semua", "#SEMUA"),
    TECHNOLOGY("Teknologi", "#TEKNOLOGI"),
    SPORTS("Olahraga", "#OLAHRAGA"),
    BUSINESS("Bisnis", "#BISNIS"),
    ENTERTAINMENT("Hiburan", "#HIBURAN")
}
