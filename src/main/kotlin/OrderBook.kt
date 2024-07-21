package com.artaxer

import java.util.PriorityQueue

class OrderBook(private val algorithm: MatchingAlgorithm) {
    private val buyOrders = PriorityQueue(compareByDescending<Order> { it.price }.thenBy { it.timestamp })
    private val sellOrders = PriorityQueue(compareBy<Order> { it.price }.thenBy { it.timestamp })

    fun addOrder(order: Order) {
        when (order.type) {
            OrderType.BUY -> buyOrders.offer(order)
            OrderType.SELL -> sellOrders.offer(order)
        }
    }

    fun matchOrders(): List<Pair<Order, Order>> {
        return when (algorithm) {
            MatchingAlgorithm.PRICE_TIME -> matchOrdersPriceTime(buyOrders, sellOrders)
            MatchingAlgorithm.PRO_RATA -> matchOrdersProRata(buyOrders, sellOrders)
        }
    }
}

enum class MatchingAlgorithm {
    PRICE_TIME, PRO_RATA
}