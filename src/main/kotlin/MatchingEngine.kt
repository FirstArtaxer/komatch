package com.artaxer

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MatchingEngine {
    private val orderBook = OrderBook()
    private val orderChannel = Channel<Order>(Channel.UNLIMITED)
    private val matchedOrdersChannel = Channel<Pair<Order, Order>>(Channel.UNLIMITED)
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    fun start() {
        scope.launch { processOrders() }
        scope.launch { matchOrders() }
    }

    fun stop() {
        job.cancel()
    }

    suspend fun addOrder(order: Order) {
        orderChannel.send(order)
    }

    private suspend fun processOrders() {
        for (order in orderChannel) {
            orderBook.addOrder(order)
        }
    }

    private suspend fun matchOrders() = coroutineScope {
        while (isActive) {
            val matches = orderBook.matchOrders()
            if (matches.isEmpty()) {
                continue
            }
            for (match in matches) {
                matchedOrdersChannel.send(match)
            }
        }
    }

    suspend fun getMatchedOrders(): List<Pair<Order, Order>> {
        val matchedOrders = mutableListOf<Pair<Order, Order>>()
        while (!matchedOrdersChannel.isEmpty) {
            matchedOrders.add(matchedOrdersChannel.receive())
        }
        return matchedOrders
    }
}