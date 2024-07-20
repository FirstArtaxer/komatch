package com.artaxer

import java.util.PriorityQueue

class OrderBook {
    private val buyOrders = PriorityQueue(compareByDescending<Order> { it.price }.thenBy { it.timestamp })
    private val sellOrders = PriorityQueue(compareBy<Order> { it.price }.thenBy { it.timestamp })

    fun addOrder(order: Order) {
        when (order.type) {
            OrderType.BUY -> buyOrders.offer(order)
            OrderType.SELL -> sellOrders.offer(order)
        }
    }

    fun matchOrders(): List<Pair<Order, Order>> {
        val matches = mutableListOf<Pair<Order, Order>>()

        while (buyOrders.isNotEmpty() && sellOrders.isNotEmpty()) {
            val buyOrder = buyOrders.peek()
            val sellOrder = sellOrders.peek()

            if (buyOrder.price >= sellOrder.price) {
                val matchedQuantity = minOf(buyOrder.quantity, sellOrder.quantity)
                matches.add(Pair(buyOrder.copy(quantity = matchedQuantity), sellOrder.copy(quantity = matchedQuantity)))

                if (buyOrder.quantity > matchedQuantity) {
                    buyOrders.offer(buyOrder.copy(quantity = buyOrder.quantity - matchedQuantity))
                }
                buyOrders.poll()

                if (sellOrder.quantity > matchedQuantity) {
                    sellOrders.offer(sellOrder.copy(quantity = sellOrder.quantity - matchedQuantity))
                }
                sellOrders.poll()
            } else {
                break
            }
        }
        return matches
    }
}