# Kotlin Trade Matching Engine

## Overview
A simple trade orders matching engine for a crypto exchange implemented in Kotlin using coroutines and channels.
This sample supports limit buy and sell orders and matches them concurrently using the Price/Time algorithm (or First-in-First-out).

## Usage

### Example

```kotlin
import com.yourusername.matchingengine.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

fun main() = runBlocking {
    val engine = MatchingEngine()
    engine.start()

    engine.addOrder(Order(1, OrderType.BUY, 100.0, 10.0))
    engine.addOrder(Order(2, OrderType.SELL, 100.0, 5.0))

    // delay for the matching engine to process orders
    delay(100)

    val matchedOrders = engine.getMatchedOrders()
    println(matchedOrders) // Output: [(Order(id=1, type=BUY, price=100.0, quantity=5.0), Order(id=2, type=SELL, price=100.0, quantity=5.0))]

    engine.stop()
}
