import com.artaxer.MatchingAlgorithm
import com.artaxer.Order
import com.artaxer.OrderBook
import com.artaxer.OrderType
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderBookTest {
    @Test
    fun testPricePriority() {
        val orderBook = OrderBook(MatchingAlgorithm.PRICE_TIME)

        orderBook.addOrder(Order(1, OrderType.BUY, 101.0, 10.0, 1000))
        orderBook.addOrder(Order(2, OrderType.BUY, 100.0, 10.0, 1001))
        orderBook.addOrder(Order(3, OrderType.SELL, 100.0, 5.0, 1002))

        val matches = orderBook.matchOrders()

        assertEquals(1, matches.size)
        assertEquals(5.0, matches[0].first.quantity)
        assertEquals(5.0, matches[0].second.quantity)
        assertEquals(1, matches[0].first.id) // Buy order with higher price matched first
    }

    @Test
    fun testTimePriority() {
        val orderBook = OrderBook(MatchingAlgorithm.PRICE_TIME)

        orderBook.addOrder(Order(1, OrderType.BUY, 100.0, 10.0, 1000))
        orderBook.addOrder(Order(2, OrderType.BUY, 100.0, 10.0, 1001))
        orderBook.addOrder(Order(3, OrderType.SELL, 100.0, 5.0, 1002))

        val matches = orderBook.matchOrders()

        assertEquals(1, matches.size)
        assertEquals(5.0, matches[0].first.quantity)
        assertEquals(5.0, matches[0].second.quantity)
        assertEquals(1, matches[0].first.id) // Older buy order matched first
    }

    @Test
    fun testProRataMatching() {
        val orderBook = OrderBook(MatchingAlgorithm.PRO_RATA)

        orderBook.addOrder(Order(1, OrderType.BUY, 100.0, 10.0, 1000))
        orderBook.addOrder(Order(2, OrderType.BUY, 100.0, 20.0, 1001))
        orderBook.addOrder(Order(3, OrderType.SELL, 100.0, 15.0, 1002))

        val matches = orderBook.matchOrders()

        assertEquals(2, matches.size)
        assertEquals(5.0, matches[0].first.quantity)
        assertEquals(5.0, matches[0].second.quantity)
        assertEquals(1, matches[0].first.id)
        assertEquals(10.0, matches[1].first.quantity)
        assertEquals(10.0, matches[1].second.quantity)
        assertEquals(2, matches[1].first.id)
    }

    @Test
    fun testProRataPartialMatching() {
        val orderBook = OrderBook(MatchingAlgorithm.PRO_RATA)

        orderBook.addOrder(Order(1, OrderType.BUY, 100.0, 10.0, 1000))
        orderBook.addOrder(Order(2, OrderType.SELL, 100.0, 25.0, 1001))

        val matches = orderBook.matchOrders()

        assertEquals(1, matches.size)
        assertEquals(10.0, matches[0].first.quantity)
        assertEquals(10.0, matches[0].second.quantity)
        assertEquals(1, matches[0].first.id)
    }
}