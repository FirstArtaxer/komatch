package com.artaxer

import java.util.PriorityQueue

fun matchOrdersPriceTime(
    buyOrders: PriorityQueue<Order>,
    sellOrders: PriorityQueue<Order>
): List<Pair<Order, Order>> {
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

fun matchOrdersProRata(
    buyOrders: PriorityQueue<Order>,
    sellOrders: PriorityQueue<Order>
): List<Pair<Order, Order>> {
    val matches = mutableListOf<Pair<Order, Order>>()
    val totalBuyQuantity = buyOrders.sumOf { it.quantity }
    val totalSellQuantity = sellOrders.sumOf { it.quantity }

    val initialBuyOrders = ArrayList(buyOrders)
    val initialSellOrders = ArrayList(sellOrders)

    buyOrders.clear()
    sellOrders.clear()

    for (sellOrder in initialSellOrders) {
        for (buyOrder in initialBuyOrders) {
            if (buyOrder.price >= sellOrder.price) {
                val matchedQuantity = minOf(
                    buyOrder.quantity * (sellOrder.quantity / totalSellQuantity),
                    sellOrder.quantity * (buyOrder.quantity / totalBuyQuantity)
                )

                if (matchedQuantity > 0) {
                    matches.add(Pair(buyOrder.copy(quantity = matchedQuantity), sellOrder.copy(quantity = matchedQuantity)))

                    val remainingBuyQuantity = buyOrder.quantity - matchedQuantity
                    if (remainingBuyQuantity > 0) {
                        buyOrders.offer(buyOrder.copy(quantity = remainingBuyQuantity))
                    }

                    val remainingSellQuantity = sellOrder.quantity - matchedQuantity
                    if (remainingSellQuantity > 0) {
                        sellOrders.offer(sellOrder.copy(quantity = remainingSellQuantity))
                    }
                }
            }
        }
    }

    return matches
}