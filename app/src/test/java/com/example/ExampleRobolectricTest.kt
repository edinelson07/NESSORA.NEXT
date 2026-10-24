package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.YouTubeRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string app_name from context returns NEXORA`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("NEXORA", appName)
  }

  @Test
  fun `youtube upload metadata validator rejects empty title`() {
    val repo = YouTubeRepository()
    val result = repo.validateUploadMetadata("", "Descricao valida", "tag1, tag2")
    assertFalse(result.isValid)
    assertTrue(result.errors.any { it.contains("branco") })
  }

  @Test
  fun `youtube upload metadata validator rejects title longer than 100 chars`() {
    val repo = YouTubeRepository()
    val longTitle = "A".repeat(101)
    val result = repo.validateUploadMetadata(longTitle, "Descricao valida", "tag1, tag2")
    assertFalse(result.isValid)
    assertTrue(result.errors.any { it.contains("100") })
  }

  @Test
  fun `youtube upload metadata validator accepts valid inputs`() {
    val repo = YouTubeRepository()
    val result = repo.validateUploadMetadata("Título Otimizado para YouTube", "Descrição com capítulos", "youtube, criador")
    assertTrue(result.isValid)
    assertEquals(0, result.errors.size)
    assertEquals(2, result.tagCount)
  }
}
