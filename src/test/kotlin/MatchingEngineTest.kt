import com.artaxer.MatchingEngine
import com.artaxer.Order
import com.artaxer.OrderType
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class MatchingEngineTest {
    private lateinit var engine: MatchingEngine

    @BeforeTest
    fun setup() {
        engine = MatchingEngine()
        engine.start()
    }

    @AfterTest
    fun teardown() {
        engine.stop()
    }

    @Test
    fun testMatchingEngine() = runBlocking {
        engine.addOrder(Order(1, OrderType.BUY, 100.0, 10.0))
        engine.addOrder(Order(2, OrderType.SELL, 100.0, 5.0))

        // delay for the matching engine to process orders
        delay(100)

        val matchedOrders = engine.getMatchedOrders()

        assertEquals(1, matchedOrders.size)
        assertEquals(5.0, matchedOrders[0].first.quantity)
        assertEquals(5.0, matchedOrders[0].second.quantity)
    }
}