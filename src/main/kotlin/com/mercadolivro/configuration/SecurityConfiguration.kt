package com.mercadolivro.configuration

import com.mercadolivro.configuration.security.AuthenticationFilter
import com.mercadolivro.repository.CustomerRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SecurityConfiguration(
    private val customerRepository: CustomerRepository,
    private val userDetailsService: UserDetailsService
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.cors()
            .and()
            .csrf().disable()
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST,"/customers").permitAll()
            .anyRequest().authenticated()
        http.addFilter(AuthenticationFilter(authenticationManager(), customerRepository))
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}