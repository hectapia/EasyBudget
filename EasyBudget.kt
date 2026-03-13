/**
 * EasyBudget - Sprint 1 CLI Project
 * ---------------------------------
 * This program demonstrates Kotlin fundamentals while building
 * a simple command-line expense tracker.
 *
 * Concepts covered:
 * - Variables, expressions, operators
 * - Conditionals (if/else, when)
 * - Loops (for, while)
 * - Functions (definition & calling)
 * - Classes & Objects
 * - Properties with getters/setters
 * - Data classes
 * - CLI interaction with readLine()
 *
 * Author: [Your Name]
 * Date: March 2026
 */

// -----------------------------
// Data Class Requirement
// -----------------------------
/**
 * Represents a single financial transaction.
 * Data classes in Kotlin automatically generate useful methods
 * like toString(), equals(), and copy().
 */
data class Expense(
    val id: Int,               // Immutable unique identifier
    var name: String,          // Mutable name of the expense
    var amount: Double,        // Mutable amount spent
    var category: String,      // Mutable category (Food, Housing, etc.)
    val date: String           // Immutable date string (YYYY-MM-DD)
)

// -----------------------------
// Class Requirement
// -----------------------------
/**
 * BudgetTracker manages a list of expenses and provides
 * collections that can grow or shrink dynamically. It includes
 * methods to add, display, and calculate totals.
 */
class BudgetTracker(private val budgetLimit: Double) {
    private val expenseList = mutableListOf<Expense>() // Mutable list (collection) to store expenses

    // Property with custom getter
    val totalSpending: Double
        get() = expenseList.sumOf { it.amount }

    // Function to add a new expense
    fun addExpense(expense: Expense) {
        expenseList.add(expense)
        println("Expense '${expense.name}' added successfully!")
    }

    // Function to display all expenses
    fun showExpenses() {
        println("\n--- Expense List ---")
        if (expenseList.isEmpty()) {
            println("No expenses recorded yet.")
        } else {
            for (item in expenseList) {
                println("${item.id}. ${item.name} - \$${item.amount} [${item.category}] on ${item.date}")
            }
        }
    }

    // Function to check budget status
    fun status(): String {
        return when {
            totalSpending > budgetLimit -> "Over Budget! ⚠️"
            totalSpending == budgetLimit -> "At Limit 🎯"
            else -> "Under Budget ✅"
        }
    }

    // Function to search expenses by category
    fun searchByCategory(category: String) {
        println("\n--- Search Results for '$category' ---")
        val results = expenseList.filter { it.category.equals(category, ignoreCase = true) }
        if (results.isEmpty()) {
            println("No expenses found in this category.")
        } else {
            for (item in results) {
                println("${item.name} - \$${item.amount} on ${item.date}")
            }
        }
    }

    // Function to remove an expense by ID
    fun removeExpenseById(id: Int) {
        val removed = expenseList.removeIf { it.id == id }
        if (removed) {
            println("Expense with ID $id removed successfully.")
        } else {
            println("No expense found with ID $id.")
        }
    }
}

// -----------------------------
// Utility Functions
// -----------------------------

/**
 * Function to safely read a Double from user input.
 * If the input is invalid, returns 0.0.
 */
fun readDouble(prompt: String): Double {
    print(prompt)
    return readLine()?.toDoubleOrNull() ?: 0.0
}

/**
 * Function to safely read a String from user input.
 * If the input is null, returns "Unknown".
 */
fun readString(prompt: String): String {
    print(prompt)
    return readLine() ?: "Unknown"
}

// -----------------------------
// Main Function (CLI Demo)
// -----------------------------
fun main() {
    println("=== Welcome to EasyBudget CLI ===")

    // Initialize tracker with a budget limit
    val tracker = BudgetTracker(500.0)

    // Preloaded sample expenses
    val coffee = Expense(1, "Coffee", 4.50, "Food", "2026-03-04")
    val rent = Expense(2, "Monthly Rent", 450.0, "Housing", "2026-03-01")
    tracker.addExpense(coffee)
    tracker.addExpense(rent)

    // Main loop for CLI interaction
    var running = true
    var nextId = 3

    while (running) {
        println("\n--- Menu ---")
        println("1. Show Expenses")
        println("2. Add Expense")
        println("3. Search by Category")
        println("4. Remove Expense")
        println("5. Show Total & Status")
        println("6. Exit")

        print("Choose an option: ")
        val choice = readLine()?.toIntOrNull() ?: 0

        when (choice) {
            1 -> tracker.showExpenses()
            2 -> {
                val name = readString("Enter name: ")
                val amount = readDouble("Enter amount: ")
                val category = readString("Enter category: ")
                val date = readString("Enter date (YYYY-MM-DD): ")
                val newExpense = Expense(nextId++, name, amount, category, date)
                tracker.addExpense(newExpense)
            }
            3 -> {
                val category = readString("Enter category to search: ")
                tracker.searchByCategory(category)
            }
            4 -> {
                val id = readDouble("Enter expense ID to remove: ").toInt()
                tracker.removeExpenseById(id)
            }
            5 -> {
                println("\nTotal Spending: \$${tracker.totalSpending}")
                println("Current Status: ${tracker.status()}")
            }
            6 -> {
                println("Exiting EasyBudget. Goodbye!")
                running = false
            }
            else -> println("Invalid option. Please try again.")
        }
    }
}
