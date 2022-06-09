package com.mercadolivro.events

import com.mercadolivro.model.PurchaseModel
import org.springframework.context.ApplicationEvent

class PurchaseEvent(
    source: Any,
    val purchase: PurchaseModel
): ApplicationEvent(source)