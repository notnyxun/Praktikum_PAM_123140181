package com.example.myapplication

import com.example.myapplication.data.NewsRepository
import com.example.myapplication.model.Category
import com.example.myapplication.viewmodel.NewsDetailUiState
import com.example.myapplication.viewmodel.NewsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialReadCountIsZero() = runTest {
        // Requirement 4: StateFlow menyimpan jumlah berita yang dibaca
        val viewModel = NewsViewModel(NewsRepository())
        assertEquals(0, viewModel.readNewsCount.value)
    }

    @Test
    fun testMarkAsReadUpdatesStateFlowCount() = runTest {
        // Requirement 4: StateFlow menyimpan jumlah berita yang dibaca
        val viewModel = NewsViewModel(NewsRepository())
        
        viewModel.markAsRead("NEWS-1")
        assertEquals(1, viewModel.readNewsCount.value)

        viewModel.markAsRead("NEWS-2")
        assertEquals(2, viewModel.readNewsCount.value)

        // Duplicate read should not increment
        viewModel.markAsRead("NEWS-1")
        assertEquals(2, viewModel.readNewsCount.value)
    }

    @Test
    fun testCategoryFilterSelection() = runTest {
        // Requirement 2: Filter berita berdasarkan kategori tertentu
        val viewModel = NewsViewModel(NewsRepository())
        
        assertEquals(Category.ALL, viewModel.selectedCategory.value)
        
        viewModel.selectCategory(Category.TECHNOLOGY)
        assertEquals(Category.TECHNOLOGY, viewModel.selectedCategory.value)
    }

    @Test
    fun testFetchNewsDetailAsync() = runTest {
        // Requirement 5: Coroutines untuk mengambil detail berita secara async
        val viewModel = NewsViewModel(NewsRepository())
        
        viewModel.fetchNewsDetail("NEWS-100")
        
        val state = viewModel.detailUiState.value
        assertTrue(state is NewsDetailUiState.Success)
        assertEquals("NEWS-100", state.detail.id)
        
        // Fetching detail should also mark article as read (Requirement 4)
        assertEquals(1, viewModel.readNewsCount.value)
    }
}
