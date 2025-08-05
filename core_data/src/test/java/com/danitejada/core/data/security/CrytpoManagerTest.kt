package com.danitejada.core.data.security

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.robolectric.RobolectricTestRunner
import org.junit.runner.RunWith
import android.os.Build
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P]) // Use API 28 for Keystore support in tests
class CryptoManagerTest {

  private lateinit var cryptoManager: CryptoManager
  private val testKeyAlias = "test_key_alias"
  private val testData = "563492ad6f91700001000001abc123def456" // Mock API key

  @Before
  fun setup() {
    cryptoManager = CryptoManager()
  }

  @Test
  fun `encrypt and decrypt returns original data`() {
    // Given
    val originalData = testData

    // When
    val encryptedData = cryptoManager.encrypt(originalData, testKeyAlias)
    val decryptedData = cryptoManager.decrypt(encryptedData, testKeyAlias)

    // Then
    assertEquals(originalData, decryptedData)
    assertNotEquals(originalData, String(encryptedData.data)) // Ensure it's actually encrypted
    assertTrue("IV should not be empty", encryptedData.iv.isNotEmpty())
  }

  @Test
  fun `encrypt produces different ciphertext for same data`() {
    // Given
    val originalData = testData

    // When
    val encryptedData1 = cryptoManager.encrypt(originalData, testKeyAlias)
    val encryptedData2 = cryptoManager.encrypt(originalData, testKeyAlias)

    // Then
    assertFalse("Encrypted data should be different due to random IV",
      encryptedData1.data.contentEquals(encryptedData2.data))
    assertFalse("IVs should be different",
      encryptedData1.iv.contentEquals(encryptedData2.iv))

    // But both should decrypt to the same original data
    assertEquals(originalData, cryptoManager.decrypt(encryptedData1, testKeyAlias))
    assertEquals(originalData, cryptoManager.decrypt(encryptedData2, testKeyAlias))
  }

  @Test
  fun `empty string can be encrypted and decrypted`() {
    // Given
    val emptyData = ""

    // When
    val encryptedData = cryptoManager.encrypt(emptyData, testKeyAlias)
    val decryptedData = cryptoManager.decrypt(encryptedData, testKeyAlias)

    // Then
    assertEquals(emptyData, decryptedData)
  }
}