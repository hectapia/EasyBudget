package database

import models.*
import java.sql.DriverManager
import java.sql.Connection

class DatabaseManager(dbPath: String) {
    private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")

    init {
        val statement = connection.createStatement()
        statement.execute("PRAGMA foreign_keys = ON;") // Enable relational constraints

        // Users Table
        statement.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE)")

        // Budgets Table (Linked to User)
        statement.execute("""
            CREATE TABLE IF NOT EXISTS budgets (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                user_id INTEGER, 
                limit_amount REAL NOT NULL,
                FOREIGN KEY(user_id) REFERENCES users(id)
            )
        """)

        // Expenses Table (Updated with user_id)
        statement.execute("""
            CREATE TABLE IF NOT EXISTS expenses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                name TEXT NOT NULL,
                amount REAL NOT NULL,
                category TEXT NOT NULL,
                date TEXT NOT NULL,
                FOREIGN KEY(user_id) REFERENCES users(id)
            )
        """)
    }

    // --- User & Budget Logic ---
    fun addUser(username: String): Int {
        val pstmt = connection.prepareStatement("INSERT INTO users (username) VALUES (?)", java.sql.Statement.RETURN_GENERATED_KEYS)
        pstmt.setString(1, username)
        pstmt.executeUpdate()
        val rs = pstmt.generatedKeys
        return if (rs.next()) rs.getInt(1) else -1
    }

    fun setBudget(userId: Int, limit: Double) {
        if (limit < 0) throw IllegalArgumentException("Budget limit cannot be negative.")
        val pstmt = connection.prepareStatement("INSERT INTO budgets (user_id, limit_amount) VALUES (?, ?)")
        pstmt.setInt(1, userId)
        pstmt.setDouble(2, limit)
        pstmt.executeUpdate()
    }

    fun getBudgetLimit(userId: Int): Double {
        val pstmt = connection.prepareStatement("SELECT limit_amount FROM budgets WHERE user_id = ?")
        pstmt.setInt(1, userId)
        val rs = pstmt.executeQuery()
        return if (rs.next()) rs.getDouble(1) else 0.0
    }

    // --- Expense Logic with Validation ---
    fun addExpense(userId: Int, expense: Expense): Boolean {
        if (expense.amount < 0) {
            println("Error: Expense amount cannot be negative.")
            return false
        }

        val currentTotal = getTotalSpending(userId)
        val limit = getBudgetLimit(userId)

        if (currentTotal + expense.amount > limit) {
            println("⚠️ WARNING: This expense will exceed your budget of $$limit!")
        }

        val sql = "INSERT INTO expenses (user_id, name, amount, category, date) VALUES (?, ?, ?, ?, ?)"
        val pstmt = connection.prepareStatement(sql)
        pstmt.setInt(1, userId)
        pstmt.setString(2, expense.name)
        pstmt.setDouble(3, expense.amount)
        pstmt.setString(4, expense.category)
        pstmt.setString(5, expense.date)
        pstmt.executeUpdate()
        return true
    }

    fun getTotalSpending(userId: Int): Double {
        val pstmt = connection.prepareStatement("SELECT SUM(amount) FROM expenses WHERE user_id = ?")
        pstmt.setInt(1, userId)
        val rs = pstmt.executeQuery()
        return if (rs.next()) rs.getDouble(1) else 0.0
    }

    fun getAllExpenses(userId: Int): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val pstmt = connection.prepareStatement("SELECT * FROM expenses WHERE user_id = ?")
        pstmt.setInt(1, userId)
        val rs = pstmt.executeQuery()
        while (rs.next()) {
            expenses.add(Expense(rs.getInt("id"), rs.getString("name"), rs.getDouble("amount"), rs.getString("category"), rs.getString("date")))
        }
        return expenses
    }
}