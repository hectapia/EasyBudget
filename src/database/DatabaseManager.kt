package database

import models.*
import java.sql.DriverManager
import java.sql.Connection
import java.sql.PreparedStatement

class DatabaseManager(dbPath: String) {
    private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")

    init {
        val stmt = connection.createStatement()
        // Enable Foreign Key constraints in SQLite
        stmt.execute("PRAGMA foreign_keys = ON;")

        // 1. Users Table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                username TEXT UNIQUE
            )
        """)

        // 2. Budgets Table (Categorized per User)
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS budgets (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                category TEXT NOT NULL,
                limit_amount REAL NOT NULL CHECK(limit_amount >= 0),
                FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
            )
        """)

        // 3. Expenses Table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS expenses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                budget_id INTEGER,
                name TEXT NOT NULL,
                amount REAL NOT NULL CHECK(amount > 0),
                date TEXT NOT NULL,
                FOREIGN KEY(budget_id) REFERENCES budgets(id) ON DELETE CASCADE,
                FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
            )
        """)
    }

    // Requirement: Create/Retrieve User
    fun addUser(username: String): Int {
        val selectSql = "SELECT id FROM users WHERE username = ?"
        val selectPstmt = connection.prepareStatement(selectSql)
        selectPstmt.setString(1, username)
        val rs = selectPstmt.executeQuery()
        
        if (rs.next()) return rs.getInt("id")

        val insertSql = "INSERT INTO users (username) VALUES (?)"
        val insertPstmt = connection.prepareStatement(insertSql, java.sql.Statement.RETURN_GENERATED_KEYS)
        insertPstmt.setString(1, username)
        insertPstmt.executeUpdate()
        
        val keys = insertPstmt.generatedKeys
        return if (keys.next()) keys.getInt(1) else -1
    }

    // Requirement: Create Budget Category with Limit
    fun createBudget(userId: Int, category: String, limit: Double) {
        val pstmt = connection.prepareStatement("INSERT INTO budgets (user_id, category, limit_amount) VALUES (?, ?, ?)")
        pstmt.setInt(1, userId)
        pstmt.setString(2, category)
        pstmt.setDouble(3, limit)
        pstmt.executeUpdate()
    }

    // Requirement: Add Expense with Budget Validation
    fun addExpense(userId: Int, budgetId: Int, name: String, amount: Double, date: String): Boolean {
        if (amount <= 0) return false

        val limit = getBudgetLimit(budgetId)
        val spent = getTotalSpentInBudget(budgetId)
        
        if (spent + amount > limit) {
            println("❌ TRANSACTION REJECTED: Expense ($$amount) exceeds remaining budget ($${limit - spent}).")
            return false
        }

        val sql = "INSERT INTO expenses (user_id, budget_id, name, amount, date) VALUES (?, ?, ?, ?, ?)"
        val pstmt = connection.prepareStatement(sql)
        pstmt.setInt(1, userId)
        pstmt.setInt(2, budgetId)
        pstmt.setString(3, name)
        pstmt.setDouble(4, amount)
        pstmt.setString(5, date)
        pstmt.executeUpdate()
        return true
    }

    // Requirement: JOIN query to summarize categorized data
    fun getBudgetStatus(userId: Int) {
        val sql = """
            SELECT b.id, b.category, b.limit_amount, IFNULL(SUM(e.amount), 0) as total_spent
            FROM budgets b
            LEFT JOIN expenses e ON b.id = e.budget_id
            WHERE b.user_id = ?
            GROUP BY b.id, b.category, b.limit_amount
        """
        val pstmt = connection.prepareStatement(sql)
        pstmt.setInt(1, userId)
        val rs = pstmt.executeQuery()
        
        println("\nID | Category | Limit | Spent | Remaining")
        println("-------------------------------------------")
        while (rs.next()) {
            val bId = rs.getInt("id")
            val limit = rs.getDouble("limit_amount")
            val spent = rs.getDouble("total_spent")
            println("$bId | ${rs.getString("category")} | $$limit | $$spent | $${limit - spent}")
        }
    }

    // CRUD: Read History (Needed to find Expense IDs)
    fun getViewExpenseHistory(userId: Int) {
        val sql = "SELECT id, name, amount, date, budget_id FROM expenses WHERE user_id = ?"
        val pstmt = connection.prepareStatement(sql)
        pstmt.setInt(1, userId)
        val rs = pstmt.executeQuery()

        println("\nExp ID | Name | Amount | Date | Budget ID")
        println("-------------------------------------------")
        while (rs.next()) {
            println("${rs.getInt("id")} | ${rs.getString("name")} | $${rs.getDouble("amount")} | ${rs.getString("date")} | ${rs.getInt("budget_id")}")
        }
    }

    // CRUD: Update Expense Amount
    fun updateExpenseAmount(expenseId: Int, newAmount: Double): Boolean {
        if (newAmount <= 0) return false
        val sql = "UPDATE expenses SET amount = ? WHERE id = ?"
        val pstmt = connection.prepareStatement(sql)
        pstmt.setDouble(1, newAmount)
        return pstmt.executeUpdate() > 0
    }

    // CRUD: Delete Expense
    fun deleteExpense(expenseId: Int) {
        val pstmt = connection.prepareStatement("DELETE FROM expenses WHERE id = ?")
        pstmt.setInt(1, expenseId)
        pstmt.executeUpdate()
    }

    // Helper Methods to Calculate Total Spent in a Budget for Validation
    private fun getTotalSpentInBudget(budgetId: Int): Double {
        val pstmt = connection.prepareStatement("SELECT SUM(amount) FROM expenses WHERE budget_id = ?")
        pstmt.setInt(1, budgetId)
        val rs = pstmt.executeQuery()
        return if (rs.next()) rs.getDouble(1) else 0.0
    }

    // Helper Method to get Budget Limit for Validation
    private fun getBudgetLimit(budgetId: Int): Double {
        val pstmt = connection.prepareStatement("SELECT limit_amount FROM budgets WHERE id = ?")
        pstmt.setInt(1, budgetId)
        val rs = pstmt.executeQuery()
        return if (rs.next()) rs.getDouble(1) else 0.0
    }
}