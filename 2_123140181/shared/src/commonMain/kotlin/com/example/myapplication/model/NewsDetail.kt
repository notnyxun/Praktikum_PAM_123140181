package com.example.myapplication.model

data class NewsDetail(
    val id: String,
    val title: String,
    val category: Category,
    val fullContent: String,
    val author: String,
    val publishedTimeFormatted: String,
    val relatedArticles: List<String>,
    val tags: List<String>,
    val fetchTimeMs: Long
)
