package com.mercadolivro.repository

import com.mercadolivro.helper.buildCustomer
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerRepositoryTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @Test
    fun `should return customers when name containing a value informed`() {
        customerRepository.save(buildCustomer(name = "Rick"))

        val customers = customerRepository.saveAll(
            listOf(
                buildCustomer(name = "John"),
                buildCustomer(name = "Jonathan")
            )
        )

        val actual = customerRepository.findByNameContaining("Jo")

        assertThat(actual)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(customers)
    }

    @Nested
    inner class ExistsByEmail {

        @Test
        fun `should return true when email exists`() {
            val email = "test@test.com"
            customerRepository.save(buildCustomer(email = email))

            val actual = customerRepository.existsByEmail(email)

            assertThat(actual).isTrue
        }

        @Test
        fun `should return false when email do not exists`() {
            val email = "test@test.com"

            val actual = customerRepository.existsByEmail(email)

            assertThat(actual).isFalse
        }

    }

    @Nested
    inner class FindByEmail {

        @Test
        fun `should return customer when email exists`() {
            val email = "test@test.com"
            val customer = customerRepository.save(buildCustomer(email = email))

            val actual = customerRepository.findByEmail(email)

            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(customer)
        }

        @Test
        fun `should return null when email do not exists`() {
            val email = "test@test.com"

            val actual = customerRepository.findByEmail(email)

            assertThat(actual).isNull()
        }

    }

}