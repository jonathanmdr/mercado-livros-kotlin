package com.mercadolivro.configuration

import com.mercadolivro.configuration.security.AuthenticationFilter
import com.mercadolivro.configuration.security.AuthorizationFilter
import com.mercadolivro.configuration.security.JtwUtil
import com.mercadolivro.repository.CustomerRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SecurityConfiguration(
    private val customerRepository: CustomerRepository,
    private val userDetailsService: UserDetailsService,
    private val jwtUtil: JtwUtil
) : WebSecurityConfigurerAdapter() {

    private val swaggerResources: Array<String> = arrayOf(
        "/v2/api-docs",
        "/v3/api-docs",
        "/**/v3/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "**/swagger-ui.html",
        "/**/swagger-ui.html**",
        "/swagger-ui.html**",
        "/webjars/**"
    )

    override fun configure(http: HttpSecurity) {
        http.cors()
            .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/customers").permitAll()
                .anyRequest().authenticated()
            .and()
                .addFilter(AuthenticationFilter(authenticationManager(), customerRepository, jwtUtil))
                .addFilter(AuthorizationFilter(authenticationManager(), userDetailsService, jwtUtil))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .csrf().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder())
    }

    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers(*swaggerResources)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

}