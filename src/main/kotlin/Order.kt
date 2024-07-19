package com.artaxer

enum class OrderType {
    BUY, SELL
}

data class Order(
    val id: Long,
    val type: OrderType,
    val price: Double,
    val quantity: Double
)