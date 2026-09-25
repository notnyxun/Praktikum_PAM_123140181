package com.example.myapplication

import com.example.myapplication.data.NewsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NewsRepositoryTest {

    private val repository = NewsRepository()

    @Test
    fun testNewsStreamEmitsInitialItem() = runBlocking {
        // Requirement 1: Flow yang mensimulasikan data berita
        val firstItem = repository.getNewsStream(periodMillis = 100L).first()
        
        assertNotNull(firstItem)
        assertTrue(firstItem.id.isNotEmpty())
        assertTrue(firstItem.title.isNotEmpty())
    }

    @Test
    fun testNewsStreamEmitsMultipleItemsWithInterval() = runBlocking {
        // Requirement 1: Flow emitting items
        val items = repository.getNewsStream(periodMillis = 50L)
            .take(3)
            .toList()

        assertEquals(3, items.size)
    }

    @Test
    fun testFetchNewsDetailAsync() = runBlocking {
        // Requirement 5: Coroutines untuk mengambil detail berita secara async
        val firstItem = repository.getNewsStream(periodMillis = 100L).first()
        val detail = repository.fetchNewsDetailAsync(firstItem.id, firstItem)

        assertNotNull(detail)
        assertEquals(firstItem.id, detail.id)
        assertEquals(firstItem.title, detail.title)
        assertTrue(detail.fullContent.isNotEmpty())
        assertTrue(detail.tags.contains("#COROUTINES"))
    }
}
