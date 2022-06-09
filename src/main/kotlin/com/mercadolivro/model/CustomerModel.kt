package com.mercadolivro.model

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Role
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
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
    var status: CustomerStatus,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "customer_role",
        joinColumns = [JoinColumn(name = "customer_id")]
    )
    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    var roles: Set<Role> = setOf()
)