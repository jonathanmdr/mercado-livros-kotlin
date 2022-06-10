package com.mercadolivro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.extension.toResponseModel
import com.mercadolivro.helper.buildCustomer
import com.mercadolivro.repository.CustomerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockUser
class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should return all customers`() {
        val customers = customerRepository.saveAll(
            listOf(
                buildCustomer(name = "John"),
                buildCustomer(name = "Jonathan")
            )
        )

        val expected = objectMapper.writeValueAsString(
            customers.map {
                it.toResponseModel()
            }
        )

        val actual = mockMvc.perform(
            get("/customers")
        ).andExpect(status().isOk)
            .andReturn()

        assertThat(actual.response.contentAsString)
            .isEqualTo(expected)
    }

    @Test
    fun `should return all customers filtering customers by name`() {
        val customers = customerRepository.saveAll(
            listOf(
                buildCustomer(name = "Rick"),
                buildCustomer(name = "Jonathan")
            )
        )

        customers.removeFirst()

        val expected = objectMapper.writeValueAsString(
            customers.map {
                it.toResponseModel()
            }
        )

        val actual = mockMvc.perform(
            get("/customers")
                .param("name", "Jo")
        ).andExpect(status().isOk)
            .andReturn()

        assertThat(actual.response.contentAsString)
            .isEqualTo(expected)
    }

    @Test
    fun `should return user when user has the same id`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(
            get("/customers/{id}", customer.id)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.status").value(CustomerStatus.ACTIVE.name))
    }

    @Test
    fun `should return not found when user do not exists`() {
        mockMvc.perform(
            get("/customers/{id}", -1)
        ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
    }

    @Test
    fun `should save a new customer`() {
        val customer = PostCustomerRequest(
            name = "Jonathan",
            email = "${UUID.randomUUID()}@test.com",
            password = "1234"
        )

        val requestBody = objectMapper.writeValueAsString(customer)

        val actual = mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.status").value(CustomerStatus.ACTIVE.name))
            .andReturn()

        assertThat(actual.response.headerNames)
            .contains(HttpHeaders.LOCATION)
    }

    @Test
    fun `should update a customer`() {
        val randomUuid = UUID.randomUUID().toString()
        val name = "Jonathan"
        val customer = customerRepository.save(buildCustomer(name = name, email = "$randomUuid@test.com"))
        val customerRequest = PutCustomerRequest(name = name, email = "$randomUuid-updated@test.com.br")
        val requestBody = objectMapper.writeValueAsString(customerRequest)

        mockMvc.perform(
            put("/customers/{id}", customer.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.email").value(customerRequest.email))
            .andExpect(jsonPath("$.status").value(CustomerStatus.ACTIVE.name))
    }

    @Test
    fun `should return not found when update a not found customer`() {
        val requestBody = objectMapper.writeValueAsString(
            PutCustomerRequest(name = "John", email = "john@test.com")
        )

        mockMvc.perform(
            put("/customers/{id}", -1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
    }

    @Test
    fun `should delete a customer`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(
            delete("/customers/{id}", customer.id)
        ).andExpect(status().isNoContent)

        val actual = customerRepository.findById(customer.id!!.toInt())

        assertThat(actual.get().status)
            .isEqualTo(CustomerStatus.DEACTIVATED)
    }

    @Test
    fun `should return not found when delete a not found customer`() {
        mockMvc.perform(
            delete("/customers/{id}", -1)
        ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
    }

}