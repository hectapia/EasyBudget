package utils

import java.net.HttpURLConnection
import java.net.URL

object NetworkUtils {
    /**
     * Simulates a ping to the Supabase host to check network connectivity
     * Returns true if the host is reachable, false otherwise
     */
    fun isOnline(): Boolean {
        return try {
            val url = URL("https://outdecjeezousqqvaidt.supabase.co")
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 5000 // 5 second timeout
            connection.requestMethod = "HEAD"
            val responseCode = connection.responseCode
            responseCode in 200..399 // Success if response code is 2xx or 3xx
        } catch (e: Exception) {
            false // If any exception occurs, assume offline
        }
    }
}