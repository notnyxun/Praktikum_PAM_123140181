package com.example.myapplication.data

import com.example.myapplication.model.Category
import com.example.myapplication.model.NewsDetail
import com.example.myapplication.model.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

class NewsRepository {

    private val sampleArticles = listOf(
        Triple(
            "Kotlin 2.0 Resmi Dirilis dengan Compiler K2 Terbaru",
            Category.TECHNOLOGY,
            "JetBrains mengumumkan rilis Kotlin 2.0 yang membawa peningkatan performa kompilasi hingga 2x lipat dan dukungan multiplatform yang semakin solid."
        ),
        Triple(
            "Timnas Indonesia Lolos ke Putaran Ketiga Kualifikasi Piala Dunia",
            Category.SPORTS,
            "Kemenangan dramatis membuat Timnas Indonesia mengamankan tiket sejarah di kualifikasi Piala Dunia zona Asia."
        ),
        Triple(
            "Suku Bunga Bank Sentral Turun, Pasar Saham Menghijau",
            Category.BUSINESS,
            "Keputusan penurunan suku bunga memberikan dorongan positif bagi indeks harga saham gabungan minggu ini."
        ),
        Triple(
            "Film Animasi Terbaru Pecahkan Rekor Box Office Global",
            Category.ENTERTAINMENT,
            "Karya animasi lokal berhasil menarik perhatian jutaan penonton secara internasional pada pekan pertamanya."
        ),
        Triple(
            "Inovasi AI On-Device Generasi Baru Siap Mengubah Smartphone",
            Category.TECHNOLOGY,
            "Perusahaan teknologi terkemuka meluncurkan chip pemrosesan AI terintegrasi dengan efisiensi daya tinggi."
        ),
        Triple(
            "Final Liga Champions Sajikan Pertandingan Penuh Drama",
            Category.SPORTS,
            "Gol penentu kemenangan di menit-menit akhir memastikan gelar juara bagi klub favorit pembaca."
        ),
        Triple(
            "Start-up Edukasi Lokal Raih Pendanaan Seri B US$ 20 Juta",
            Category.BUSINESS,
            "Suntikan dana baru akan digunakan untuk ekspansi platform pembelajaran berbasis AI ke seluruh pelosok negeri."
        ),
        Triple(
            "Konser Musik Spektakuler Sukses Sedot Puluhan Ribu Penonton",
            Category.ENTERTAINMENT,
            "Ajang musik tahunan kembali digelar secara luar biasa dengan penataan panggung audio-visual modern."
        )
    )

    private val authors = listOf(
        "Budi Santoso", "Siti Rahma", "Ahmad Fauzi",
        "Dewi Lestari", "Rian Hidayat", "Eka Pratama"
    )

    private var articleCounter = 1

    /**
     * Requirement 1: Flow yang mensimulasikan data berita baru setiap 2 detik
     */
    fun getNewsStream(periodMillis: Long = DEFAULT_STREAM_INTERVAL_MS): Flow<NewsItem> = flow {
        // Emit first news immediately for instant visual feedback
        val initialItem = createNewsItem(0, periodMillis)
        emit(initialItem)

        while (currentCoroutineContext().isActive) {
            delay(periodMillis)
            val newItem = createNewsItem(articleCounter++, periodMillis)
            emit(newItem)
        }
    }

    private fun createNewsItem(index: Int, intervalMs: Long): NewsItem {
        val baseArticle = sampleArticles[index % sampleArticles.size]
        val author = authors[index % authors.size]
        val id = "NEWS-${System.currentTimeMillis()}-$index"
        
        return NewsItem(
            id = id,
            title = "${baseArticle.first} (#$index)",
            summary = baseArticle.third,
            category = baseArticle.second,
            author = author,
            timestamp = System.currentTimeMillis(),
            rawContent = """
                ${baseArticle.first}.
                
                ${baseArticle.third}
                
                Wartawan $author melaporkan langsung dari lokasi kejadian. Berita ini disimulasikan secara real-time menggunakan Kotlin Coroutines & Flow setiap ${intervalMs}ms. 
                
                Perkembangan teknologi modern memungkinkan pengiriman data secara asynchronous tanpa memblokir thread utama UI.
            """.trimIndent(),
            wordCount = 150 + (index * 12) % 200
        )
    }

    /**
     * Requirement 5: Coroutines untuk mengambil detail berita secara async
     */
    suspend fun fetchNewsDetailAsync(newsId: String, currentItem: NewsItem?): NewsDetail = coroutineScope {
        val startTime = System.currentTimeMillis()

        // Asynchronously fetch additional detail attributes using async block
        val detailDeferred = async(Dispatchers.Default) {
            // Simulate network latency for async detail fetching
            delay(700L)

            val category = currentItem?.category ?: Category.TECHNOLOGY
            val title = currentItem?.title ?: "Berita Utama Hari Ini ($newsId)"
            val author = currentItem?.author ?: "Tim Redaksi"
            
            val fullContent = currentItem?.rawContent ?: """
                Detail lengkap berita $newsId berhasil dimuat secara asynchronous menggunakan Coroutines 'async'.
                
                Data diproses di background thread dan dikembalikan ke UI via Flow/StateFlow.
            """.trimIndent()

            val relatedArticles = sampleArticles
                .filter { it.second == category }
                .map { it.first }
                .take(3)

            val tags = listOf(
                category.tagLabel,
                "#COROUTINES",
                "#KOTLIN_FLOW",
                "#SIMULATOR"
            )

            NewsDetail(
                id = newsId,
                title = title,
                category = category,
                fullContent = fullContent,
                author = author,
                publishedTimeFormatted = formatTimestamp(currentItem?.timestamp ?: System.currentTimeMillis()),
                relatedArticles = relatedArticles,
                tags = tags,
                fetchTimeMs = System.currentTimeMillis() - startTime
            )
        }

        // Await deferred result asynchronously
        detailDeferred.await()
    }

    private fun formatTimestamp(timeMs: Long): String {
        val seconds = (System.currentTimeMillis() - timeMs) / 1000
        return when {
            seconds < 5 -> "Baru saja"
            seconds < 60 -> "$seconds detik yang lalu"
            else -> "${seconds / 60} menit yang lalu"
        }
    }

    companion object {
        const val DEFAULT_STREAM_INTERVAL_MS = 2000L
    }
}
