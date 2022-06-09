package com.mercadolivro.model

import com.mercadolivro.enums.CustomerStatus
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "customer")
data class CustomerModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var name: String,
    var email: String,
    var password: String,

    @Enumerated(EnumType.STRING)
    var status: CustomerStatus
)