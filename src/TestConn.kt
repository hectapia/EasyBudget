import java.sql.DriverManager
import java.sql.Connection
import java.sql.SQLException

fun main() {
    // 1. Update these with your exact Supabase Dashboard values
    val host = "outdecjeezousqqvaidt.supabase.co"
    val port = "5432"
    val dbName = "postgres"
    val user = "postgres"
    val password = "Supabase_2020" // NOT your login password
    
    // val url = "jdbc:postgresql://$host:$port/$dbName"
    val url = "jdbc:postgresql://$host:$port/$dbName?sslmode=require"

    println("Attempting to connect to: $url")
    println("Using user: $user")

    var connection: Connection? = null

    try {
        // 2. Explicitly load the driver (helps identify if the JAR is missing)
        Class.forName("org.postgresql.Driver")
        println("✅ PostgreSQL Driver found.")

        // 3. Attempt the connection
        connection = DriverManager.getConnection(url, user, password)
        
        if (connection != null && !connection.isClosed) {
            println("🚀 SUCCESS: Connection established to Supabase!")
            
            // 4. Test a simple query
            val rs = connection.createStatement().executeQuery("SELECT version();")
            if (rs.next()) {
                println("PostgreSQL Version: ${rs.getString(1)}")
            }
        }
    } catch (e: ClassNotFoundException) {
        println("❌ ERROR: PostgreSQL Driver NOT found in classpath. Check your lib/ folder.")
    } catch (e: SQLException) {
        println("❌ SQL ERROR: ${e.message}")
        println("Error Code: ${e.errorCode}")
        println("SQL State: ${e.sqlState}")
        
        when (e.sqlState) {
            "28P01" -> println("👉 Hint: Invalid password. Check your Supabase Database Password.")
            "08001" -> println("👉 Hint: Network timeout. Check your host URL or Firewall (Port 5432).")
            "3D000" -> println("👉 Hint: Database name '$dbName' is incorrect.")
            else -> println("👉 Hint: Check your Supabase Dashboard 'Database' settings.")
        }
    } catch (e: Exception) {
        println("❌ UNKNOWN ERROR: ${e.localizedMessage}")
    } finally {
        connection?.close()
        println("Connection closed.")
    }
}