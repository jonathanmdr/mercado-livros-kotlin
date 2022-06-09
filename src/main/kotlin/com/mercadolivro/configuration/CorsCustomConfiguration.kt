package com.mercadolivro.configuration

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsCustomConfiguration {

    @Bean
    fun corsFilterRegistrationBean(): FilterRegistrationBean<CorsFilter> {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.allowedOrigins = listOf("*")
        config.allowedMethods = listOf("*")
        config.allowedHeaders = listOf("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        val bean = FilterRegistrationBean<CorsFilter>()
        bean.filter = CorsFilter(source)
        bean.order = Ordered.HIGHEST_PRECEDENCE

        return bean
    }

}