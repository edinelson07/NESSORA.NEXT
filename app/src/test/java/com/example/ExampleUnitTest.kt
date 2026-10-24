package com.example

import com.example.data.model.DraftType
import com.example.data.model.NarrativeSection
import com.example.data.repository.SubscriptionRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testDraftTypeFromString() {
    val longType = DraftType.fromString("LONG_VIDEO")
    assertEquals(DraftType.LONG_VIDEO, longType)

    val shortsType = DraftType.fromString("SHORTS")
    assertEquals(DraftType.SHORTS, shortsType)

    val defaultFallback = DraftType.fromString("INVALIDO")
    assertEquals(DraftType.LONG_VIDEO, defaultFallback)
  }

  @Test
  fun testNarrativeStructureHasAllRetentionBlocks() {
    val sections = NarrativeSection.getDefaultStructure()
    assertTrue(sections.isNotEmpty())
    assertTrue(sections.any { it.id == "hook" })
    assertTrue(sections.any { it.id == "promise" })
    assertTrue(sections.any { it.id == "reengagement" })
    assertTrue(sections.any { it.id == "climax" })
    assertTrue(sections.any { it.id == "cta" })
    assertTrue(sections.any { it.id == "next_hook" })
  }

  @Test
  fun testPixPayloadGeneration() {
    val subRepo = SubscriptionRepository()
    val monthlyPix = subRepo.generatePixPayment(isAnnual = false)
    assertNotNull(monthlyPix)
    assertTrue(monthlyPix.startsWith("00020126580014br.gov.bcb.pix"))
    assertTrue(monthlyPix.contains("NEXORA PLATAFORMA"))

    val annualPix = subRepo.generatePixPayment(isAnnual = true)
    assertTrue(annualPix.contains("349.00"))
  }
}
