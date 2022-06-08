package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostPurchaseRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/purchases")
class PurchaseController {

    @PostMapping
    fun purchase(@RequestBody @Valid purchase: PostPurchaseRequest) {

    }

}