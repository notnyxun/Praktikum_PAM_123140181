package com.example.myapplication.model

data class NewsItem(
    val id: String,
    val title: String,
    val summary: String,
    val category: Category,
    val author: String,
    val timestamp: Long = System.currentTimeMillis(),
    val rawContent: String,
    val wordCount: Int = 180
)
