import com.andriybobchuk.cocreate.util.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class UtilFunctionsTest {

    private lateinit var currentTimestamp: String

    @Before
    fun setup() {
        val dateFormat = SimpleDateFormat("MMM d, ''yy 'at' h:mm a", Locale.ENGLISH)
        currentTimestamp = dateFormat.format(Date())
    }

    @Test
    fun `getCurrentDateTime returns formatted current date and time`() {
        // Call the function to get the current date and time
        val result = getCurrentDateTime()

        // Check if the result matches the expected format (Jan 15, '23 at 2:30 PM)
        val regex = Regex("""[A-Z][a-z]{2} \d{1,2}, '\d{2} at \d{1,2}:\d{2} [AP]M""")
        assertEquals(true, regex.matches(result))
    }

    @Test
    fun `toEpochMillis parses date string into epoch time correctly`() {
        val dateString = "Jan 1, '20 at 12:00 PM"
        val expectedEpochTime = SimpleDateFormat("MMM d, ''yy 'at' h:mm a", Locale.ENGLISH).parse(dateString).time
        assertEquals(expectedEpochTime, toEpochMillis(dateString))
    }

    @Test
    fun `formatShortTimeAgo returns correct time difference string`() {
        // This will require controlling the "current time" and the input timestamp.
        // Consider using a fixed date for both the current time and the timestamp and verify the output.
    }

    @Test
    fun `generateShortUserDescription generates description correctly`() {
        assertEquals("Developer in London", generateShortUserDescription("developer", "London"))
        assertEquals("Based in London", generateShortUserDescription(null, "London"))
        assertEquals("Developer", generateShortUserDescription("developer", null))
        assertEquals("", generateShortUserDescription(null, null))
    }

    @Test
    fun `formatShortTimeAgo formats minutes correctly`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, -1)
        val timestamp = SimpleDateFormat("MMM d, ''yy 'at' h:mm a", Locale.ENGLISH).format(calendar.time)
        val result = formatShortTimeAgo(timestamp)
        assertEquals("1m", result)
    }

    @Test
    fun `formatShortTimeAgo formats hours correctly`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, -1)
        val timestamp = SimpleDateFormat("MMM d, ''yy 'at' h:mm a", Locale.ENGLISH).format(calendar.time)
        val result = formatShortTimeAgo(timestamp)
        assertEquals("1h", result)
    }

    @Test
    fun `formatShortTimeAgo formats days correctly`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val timestamp = SimpleDateFormat("MMM d, ''yy 'at' h:mm a", Locale.ENGLISH).format(calendar.time)
        val result = formatShortTimeAgo(timestamp)
        assertEquals("1d", result)
    }

    @Test
    fun `formatShortTimeAgo formats weeks correctly`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val timestamp = SimpleDateFormat("MMM d, ''yy 'at' h:mm a", Locale.ENGLISH).format(calendar.time)
        val result = formatShortTimeAgo(timestamp)
        assertEquals("1w", result)
    }

    @Test
    fun `formatShortTimeAgo formats months correctly`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val timestamp = SimpleDateFormat("MMM d, ''yy 'at' h:mm a", Locale.ENGLISH).format(calendar.time)
        val result = formatShortTimeAgo(timestamp)
        assertEquals("1mo", result)
    }

    @Test
    fun `formatShortTimeAgo formats years correctly`() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -1)
        val timestamp = SimpleDateFormat("MMM d, ''yy 'at' h:mm a", Locale.ENGLISH).format(calendar.time)
        val result = formatShortTimeAgo(timestamp)
        assertEquals("1y", result)
    }
}
