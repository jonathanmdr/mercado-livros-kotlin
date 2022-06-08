package com.mercadolivro.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [CustomerAvailableValidator::class])
annotation class CustomerAvailable(
    val message: String = "The informed customer is in DEACTIVATED status",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
