package database

import models.Expense
import java.sql.DriverManager
import java.sql.Connection
import java.sql.PreparedStatement

class SupabaseManager : BudgetRepository {
    // Actual Supabase Project Settings > Database details
    private val host = "https://outdecjeezousqqvaidt.supabase.co"
    private val port = "5432"
    private val dbName = "postgres"
    private val user = "postgres"
    private val password = "Supabase_2020"
    
    private val url = "jdbc:postgresql://$host:$port/$dbName"

    private fun getConnection(): Connection {
        return DriverManager.getConnection(url, user, password)
    }

    override fun addExpense(expense: Expense) {
        val sql = "INSERT INTO expenses (name, amount, category) VALUES (?, ?, ?)"
        getConnection().use { conn ->
            val pstmt = conn.prepareStatement(sql)
            pstmt.setString(1, expense.name)
            pstmt.setDouble(2, expense.amount)
            pstmt.setString(3, expense.category)
            pstmt.executeUpdate()
        }
    }

    override fun getAllExpenses(): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val sql = "SELECT id, name, amount, category FROM expenses ORDER BY id ASC"
        getConnection().use { conn ->
            val rs = conn.createStatement().executeQuery(sql)
            while (rs.next()) {
                expenses.add(Expense(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("amount"),
                    rs.getString("category")
                ))
            }
        }
        return expenses
    }

    override fun updateExpense(id: Int, newAmount: Double) {
        val sql = "UPDATE expenses SET amount = ? WHERE id = ?"
        getConnection().use { conn ->
            val pstmt = conn.prepareStatement(sql)
            pstmt.setDouble(1, newAmount)
            pstmt.setInt(2, id)
            pstmt.executeUpdate()
        }
    }

    override fun deleteExpense(id: Int) {
        val sql = "DELETE FROM expenses WHERE id = ?"
        getConnection().use { conn ->
            val pstmt = conn.prepareStatement(sql)
            pstmt.setInt(1, id)
            pstmt.executeUpdate()
        }
    }

    override fun getTotalSpending(): Double {
        val sql = "SELECT SUM(amount) FROM expenses"
        getConnection().use { conn ->
            val rs = conn.createStatement().executeQuery(sql)
            return if (rs.next()) rs.getDouble(1) else 0.0
        }
    }
}