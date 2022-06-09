package com.mercadolivro.service

import com.mercadolivro.model.PurchaseModel
import com.mercadolivro.repository.PurchaseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository
) {

    fun savePurchase(purchaseModel: PurchaseModel): PurchaseModel {
        return purchaseRepository.save(purchaseModel)
    }

}
