package com.mercadolivro.configuration.security

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.model.CustomerModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserCustomDetails(
    private val customerModel: CustomerModel
): UserDetails {

    val id: Int = customerModel.id!!

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return customerModel.roles.map {
            SimpleGrantedAuthority(it.role)
        }.toMutableList()
    }

    override fun getPassword(): String {
        return customerModel.password
    }

    override fun getUsername(): String {
        return customerModel.id.toString()
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return CustomerStatus.ACTIVE == customerModel.status
    }

}