package com.example.myapplication.model

/**
 * Formatted model created by data transformation operators (e.g. map)
 * satisfy Requirement 3: Transform data menjadi format yang ditampilkan
 */
data class NewsDisplayModel(
    val id: String,
    val titleFormatted: String,
    val summary: String,
    val category: Category,
    val categoryBadge: String,
    val formattedTime: String,
    val readTimeEstimate: String,
    val authorFormatted: String,
    val isRead: Boolean
)
