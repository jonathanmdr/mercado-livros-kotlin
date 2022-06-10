package com.mercadolivro.service

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.helper.buildPurchase
import com.mercadolivro.repository.PurchaseRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {

    @MockK
    private lateinit var purchaseRepository: PurchaseRepository

    @MockK
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @InjectMockKs
    private lateinit var purchaseService: PurchaseService

    private val purchaseEventSlot = slot<PurchaseEvent>()

    @Test
    fun `should be save a purchase and public event`() {
        val purchase =  buildPurchase()

        every {
            purchaseRepository.save(purchase)
        } returns purchase
        every {
            applicationEventPublisher.publishEvent(any())
        } just runs

        purchaseService.savePurchase(purchase)

        verify(exactly = 1) {
            purchaseRepository.save(purchase)
        }
        verify(exactly = 1) {
            applicationEventPublisher.publishEvent(capture(purchaseEventSlot))
        }

        assertThat(purchaseEventSlot.captured.purchase)
            .usingRecursiveComparison()
            .isEqualTo(purchase)
    }

    @Test
    fun `should be update a purchase`() {
        val purchase =  buildPurchase()

        every {
            purchaseRepository.save(purchase)
        } returns purchase

        purchaseService.updatePurchase(purchase)

        verify(exactly = 1) {
            purchaseRepository.save(purchase)
        }
    }

}