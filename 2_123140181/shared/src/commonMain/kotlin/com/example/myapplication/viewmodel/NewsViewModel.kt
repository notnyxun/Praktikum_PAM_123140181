package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.NewsRepository
import com.example.myapplication.model.Category
import com.example.myapplication.model.NewsDetail
import com.example.myapplication.model.NewsDisplayModel
import com.example.myapplication.model.NewsItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface NewsDetailUiState {
    data object Idle : NewsDetailUiState
    data class Loading(val newsId: String) : NewsDetailUiState
    data class Success(val detail: NewsDetail) : NewsDetailUiState
    data class Error(val message: String) : NewsDetailUiState
}

class NewsViewModel(
    private val repository: NewsRepository = NewsRepository()
) : ViewModel() {

    private val _rawNewsList = MutableStateFlow<List<NewsItem>>(emptyList())
    
    // Requirement 2: Filter berita berdasarkan kategori
    private val _selectedCategory = MutableStateFlow(Category.ALL)
    val selectedCategory: StateFlow<Category> = _selectedCategory.asStateFlow()

    // Requirement 4: StateFlow untuk menyimpan jumlah berita yang sudah dibaca
    private val _readNewsIds = MutableStateFlow<Set<String>>(emptySet())
    
    /**
     * Requirement 4: StateFlow yang menyimpan jumlah total berita yang sudah dibaca
     */
    val readNewsCount: StateFlow<Int> = _readNewsIds
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )

    private val _isFeedActive = MutableStateFlow(true)
    val isFeedActive: StateFlow<Boolean> = _isFeedActive.asStateFlow()

    private var streamJob: Job? = null

    // Requirement 5: Async detail state
    private val _detailUiState = MutableStateFlow<NewsDetailUiState>(NewsDetailUiState.Idle)
    val detailUiState: StateFlow<NewsDetailUiState> = _detailUiState.asStateFlow()

    /**
     * Combine & Transform Data (Requirement 2 & 3)
     * - Requirement 2: Filter berita berdasarkan kategori tertentu
     * - Requirement 3: Transform data menjadi format yang ditampilkan
     */
    val newsFeed: StateFlow<List<NewsDisplayModel>> = combine(
        _rawNewsList,
        _selectedCategory,
        _readNewsIds
    ) { rawList, category, readIds ->
        // 1. Requirement 2: Filter berdasarkan kategori
        val filteredList = if (category == Category.ALL) {
            rawList
        } else {
            rawList.filter { it.category == category }
        }

        // 2. Requirement 3: Transform data menjadi NewsDisplayModel untuk UI
        filteredList.map { item ->
            transformToDisplayModel(item, isRead = readIds.contains(item.id))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalNewsReceivedCount: StateFlow<Int> = _rawNewsList
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )

    init {
        startNewsStream()
    }

    /**
     * Requirement 1: Flow yang mensimulasikan data berita baru setiap 2 detik
     */
    fun startNewsStream() {
        if (streamJob?.isActive == true) return
        
        _isFeedActive.value = true
        streamJob = viewModelScope.launch {
            repository.getNewsStream(2000L).collect { newItem ->
                _rawNewsList.update { current ->
                    // Prepend new news item to top of feed
                    listOf(newItem) + current
                }
            }
        }
    }

    fun stopNewsStream() {
        streamJob?.cancel()
        _isFeedActive.value = false
    }

    fun toggleNewsStream() {
        if (_isFeedActive.value) {
            stopNewsStream()
        } else {
            startNewsStream()
        }
    }

    /**
     * Requirement 2: Filter berita berdasarkan kategori tertentu
     */
    fun selectCategory(category: Category) {
        _selectedCategory.value = category
    }

    /**
     * Requirement 4: Menandai berita telah dibaca dan memperbarui StateFlow jumlah dibaca
     */
    fun markAsRead(newsId: String) {
        _readNewsIds.update { currentSet ->
            currentSet + newsId
        }
    }

    fun resetReadCount() {
        _readNewsIds.value = emptySet()
    }

    /**
     * Requirement 5: Coroutines untuk mengambil detail berita secara async
     */
    fun fetchNewsDetail(newsId: String) {
        viewModelScope.launch {
            _detailUiState.value = NewsDetailUiState.Loading(newsId)
            
            // Mark article as read when opened
            markAsRead(newsId)

            try {
                val currentItem = _rawNewsList.value.find { it.id == newsId }
                
                // Requirement 5: Memanggil suspend function async dari repository
                val detail = repository.fetchNewsDetailAsync(newsId, currentItem)
                
                _detailUiState.value = NewsDetailUiState.Success(detail)
            } catch (e: Exception) {
                _detailUiState.value = NewsDetailUiState.Error(
                    e.message ?: "Terjadi kesalahan saat memuat detail berita"
                )
            }
        }
    }

    fun dismissNewsDetail() {
        _detailUiState.value = NewsDetailUiState.Idle
    }

    /**
     * Requirement 3: Transform data (Helper mapper function)
     */
    private fun transformToDisplayModel(item: NewsItem, isRead: Boolean): NewsDisplayModel {
        val secondsAgo = maxOf(0, (System.currentTimeMillis() - item.timestamp) / 1000)
        val formattedTime = when {
            secondsAgo < 5 -> "Baru saja"
            secondsAgo < 60 -> "$secondsAgo d yang lalu"
            else -> "${secondsAgo / 60} m yang lalu"
        }

        val estimatedMinutes = maxOf(1, item.wordCount / 100)
        
        return NewsDisplayModel(
            id = item.id,
            titleFormatted = item.title,
            summary = item.summary,
            category = item.category,
            categoryBadge = item.category.tagLabel,
            formattedTime = formattedTime,
            readTimeEstimate = "$estimatedMinutes hlm / 1-2m baca",
            authorFormatted = "Penulis: ${item.author}",
            isRead = isRead
        )
    }
}
