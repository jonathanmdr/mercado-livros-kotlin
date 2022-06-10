package com.mercadolivro.events.listener

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.helper.buildPurchase
import com.mercadolivro.service.PurchaseService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
class GenerateNfeListenerTest {

    @MockK
    private lateinit var purchaseService: PurchaseService

    @InjectMockKs
    private lateinit var generateNfeListener: GenerateNfeListener

    @Test
    fun `should update purchase with NF-e number`() {
        val nfe = UUID.randomUUID()
        val purchase = buildPurchase(nfe = null)
        val purchaseWithNfe = purchase.copy(nfe = nfe.toString())
        mockkStatic(UUID::class)

        every {
            UUID.randomUUID()
        } returns nfe
        every {
            purchaseService.updatePurchase(purchaseWithNfe)
        } returns purchaseWithNfe

        generateNfeListener.listen(PurchaseEvent(this, purchase))

        verify(exactly = 1) {
            purchaseService.updatePurchase(purchaseWithNfe)
        }
    }

}