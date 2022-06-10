package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Role
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.Optional
import java.util.UUID
import javax.persistence.EntityNotFoundException
import kotlin.random.Random

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @SpyK
    @InjectMockKs
    private lateinit var customerService: CustomerService

    @Test
    fun `should return all customers`() {
        val customersData = listOf(buildCustomer(), buildCustomer())

        every {
            customerRepository.findAll()
        } returns customersData

        val customers = customerService.getAllCustomers(null)

        assertThat(customers)
            .usingRecursiveComparison()
            .isEqualTo(customersData)

        verify(exactly = 1) {
            customerRepository.findAll()
        }
        verify(exactly = 0) {
            customerRepository.findByNameContaining(any())
        }
    }

    @Test
    fun `should return one customer when name is informed`() {
        val customerName = UUID.randomUUID().toString()
        val customersData = listOf(buildCustomer())

        every {
            customerRepository.findByNameContaining(customerName)
        } returns customersData

        val actual = customerService.getAllCustomers(customerName)

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(customersData)

        verify(exactly = 0) {
            customerRepository.findAll()
        }
        verify(exactly = 1) {
            customerRepository.findByNameContaining(customerName)
        }
    }

    @Test
    fun `should return customer by id`() {
        val id = Random.nextInt()
        val customer = buildCustomer(id = id)

        every {
            customerRepository.findById(id)
        } returns Optional.of(customer)

        val actual = customerService.getCustomerById(id)

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(customer)

        verify(exactly = 1) {
            customerRepository.findById(id)
        }
    }

    @Test
    fun `should throw EntityNotFoundException when customer not exists`() {
        val id = Random.nextInt()

        every {
            customerRepository.findById(id)
        } returns Optional.empty()

        assertThatThrownBy {
            customerService.getCustomerById(id)
        }.isInstanceOf(EntityNotFoundException::class.java)
            .hasMessageContaining("Customer $id not found")

        verify(exactly = 1) {
            customerRepository.findById(id)
        }
    }

    @Test
    fun `should create customer`() {
        val password = Random.nextInt().toString()
        val encryptedPassword = UUID.randomUUID().toString()
        val customer = buildCustomer(password = password)
        val customerWithEncryptedPassword = customer.copy(password = encryptedPassword)

        every {
            customerRepository.save(customerWithEncryptedPassword)
        } returns customerWithEncryptedPassword
        every {
            passwordEncoder.encode(password)
        } returns encryptedPassword

        val actual = customerService.saveCustomer(customer)

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(customerWithEncryptedPassword)
        verify(exactly = 1) {
            customerRepository.save(customerWithEncryptedPassword)
        }
        verify(exactly = 1) {
            passwordEncoder.encode(password)
        }
    }

    @Test
    fun `should update customer`() {
        val id = Random.nextInt()
        val customer = buildCustomer(id = id)

        every {
            customerRepository.existsById(id)
        } returns true
        every {
            customerRepository.save(customer)
        } returns customer

        val actual = customerService.updateCustomer(customer)

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(customer)

        verify(exactly = 1) {
            customerRepository.existsById(id)
        }
        verify(exactly = 1) {
            customerRepository.save(customer)
        }
    }

    @Test
    fun `should throw EntityNotFoundException when updating not found customer`() {
        val id = Random.nextInt()
        val customer = buildCustomer(id = id)

        every {
            customerRepository.existsById(id)
        } returns false

        assertThatThrownBy {
            customerService.updateCustomer(customer)
        }.isInstanceOf(EntityNotFoundException::class.java)
            .hasMessageContaining("Customer $id not found")

        verify(exactly = 1) {
            customerRepository.existsById(id)
        }
        verify(exactly = 0) {
            customerRepository.save(any())
        }
    }

    @Test
    fun `should delete customer`() {
        val id = Random.nextInt()
        val customer = buildCustomer(id = id)
        val customerWithDeactivatedStatus = customer.copy(status = CustomerStatus.DEACTIVATED)

        every {
            customerService.getCustomerById(id)
        } returns customer
        every {
            bookService.deleteByCustomer(customer)
        } just runs
        every {
            customerRepository.save(customerWithDeactivatedStatus)
        } returns customerWithDeactivatedStatus

        customerService.deleteCustomer(id)

        verify(exactly = 1) {
            bookService.deleteByCustomer(customer)
        }
        verify(exactly = 1) {
            customerRepository.save(customerWithDeactivatedStatus)
        }
    }

    @Test
    fun `should throw EntityNotFoundException when deleting not found customer`() {
        val id = Random.nextInt()

        every {
            customerService.getCustomerById(id)
        } throws EntityNotFoundException("Customer $id not found")

        assertThatThrownBy {
            customerService.deleteCustomer(id)
        }.isInstanceOf(EntityNotFoundException::class.java)
            .hasMessageContaining("Customer $id not found")

        verify(exactly = 1) {
            customerService.getCustomerById(id)
        }
        verify(exactly = 0) {
            bookService.deleteByCustomer(any())
        }
        verify(exactly = 0) {
            customerRepository.save(any())
        }
    }

    @Test
    fun `should return true when email is unavailable`() {
        val email = "${UUID.randomUUID()}@test.com"

        every {
            customerRepository.existsByEmail(email)
        } returns true

        val actual = customerService.emailIsAvailable(email)

        assertThat(actual).isFalse
    }

    @Test
    fun `should return false when email is available`() {
        val email = "${UUID.randomUUID()}@test.com"

        every {
            customerRepository.existsByEmail(email)
        } returns false

        val actual = customerService.emailIsAvailable(email)

        assertThat(actual).isTrue
    }

    private fun buildCustomer(
        id: Int? = null,
        name: String = "John",
        email: String = "${UUID.randomUUID()}@test.com",
        password: String = "1234"
    ) = CustomerModel(
        id = id,
        name = name,
        email = email,
        status = CustomerStatus.ACTIVE,
        password = password,
        roles = setOf(Role.CUSTOMER)
    )

}