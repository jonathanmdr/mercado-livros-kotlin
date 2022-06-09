package com.mercadolivro.service

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.model.PurchaseModel
import com.mercadolivro.repository.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun savePurchase(purchaseModel: PurchaseModel): PurchaseModel {
        val purchase = purchaseRepository.save(purchaseModel)

        applicationEventPublisher.publishEvent(PurchaseEvent(this, purchase))

        return purchase
    }

    fun updatePurchase(purchaseModel: PurchaseModel): PurchaseModel {
        return purchaseRepository.save(purchaseModel)
    }

}
