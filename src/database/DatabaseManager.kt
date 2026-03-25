package database

import models.Expense
import java.sql.DriverManager
import java.sql.Connection
import java.sql.PreparedStatement

class DatabaseManager(dbPath: String) : BudgetRepository {
    private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")

    init {
        // Requirement: Create a database table
        val statement = connection.createStatement()
        statement.execute("""
            CREATE TABLE IF NOT EXISTS expenses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                amount REAL NOT NULL,
                category TEXT NOT NULL
            )
        """)
    }

    // Requirement: Insert Data
    override fun addExpense(expense: Expense) {
        val sql = "INSERT INTO expenses (name, amount, category) VALUES (?, ?, ?)"
        val pstmt: PreparedStatement = connection.prepareStatement(sql)
        pstmt.setString(1, expense.name)
        pstmt.setDouble(2, expense.amount)
        pstmt.setString(3, expense.category)
        pstmt.executeUpdate()
    }

    // Requirement: Retrieve/Query Data
    override fun getAllExpenses(): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val rs = connection.createStatement().executeQuery("SELECT * FROM expenses")
        while (rs.next()) {
            expenses.add(Expense(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("amount"),
                rs.getString("category")
            ))
        }
        return expenses
    }

    // Requirement: Modify Data
    override fun updateExpense(id: Int, newAmount: Double) {
        val sql = "UPDATE expenses SET amount = ? WHERE id = ?"
        val pstmt = connection.prepareStatement(sql)
        pstmt.setDouble(1, newAmount)
        pstmt.setInt(2, id)
        pstmt.executeUpdate()
    }

    // Requirement: Delete Data
    override fun deleteExpense(id: Int) {
        val sql = "DELETE FROM expenses WHERE id = ?"
        val pstmt = connection.prepareStatement(sql)
        pstmt.setInt(1, id)
        pstmt.executeUpdate()
    }

    // Bonus Requirement: Aggregate Function (SUM)
    override fun getTotalSpending(): Double {
        val rs = connection.createStatement().executeQuery("SELECT SUM(amount) FROM expenses")
        return if (rs.next()) rs.getDouble(1) else 0.0
    }
}