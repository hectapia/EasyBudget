/**
 * Sprint 1: CLI-EasyBudget
 * Implements basics, OOP, and demo logic.
 */

// Data Class Requirement (Week 2 - Wed)
data class Expense(
    val id: Int,               // Immutable
    var name: String,          // Mutable
    var amount: Double,
    var category: String,
    val date: String
)

// Class Requirement (Week 2 - Mon)
class BudgetTracker(private val budgetLimit: Double) {
    private val expenseList = mutableListOf<Expense>()

    // Properties with custom getter (Week 2 - Tue)
    val totalSpending: Double
        get() = expenseList.sumOf { it.amount }

    // Function Requirement (Week 2 - Thu)
    fun addExpense(expense: Expense) {
        expenseList.add(expense)
    }

    fun showExpenses() {
        println("\n--- Expense List ---")
        for (item in expenseList) {
            println("${item.id}. ${item.name} - \$${item.amount} [${item.category}] on ${item.date}")
        }
    }

    fun status(): String {
        return when {
            totalSpending > budgetLimit -> "Over Budget! ⚠️"
            totalSpending == budgetLimit -> "At Limit 🎯"
            else -> "Under Budget ✅"
        }
    }
}

fun main() {
    // Variables & Expressions (Week 1 - Wed)
    val tracker = BudgetTracker(500.0)

    // Creating instances of the Data Class (Week 2 - Wed)
    val coffee = Expense(1, "Coffee", 4.50, "Food", "2026-03-04")
    val rent = Expense(2, "Monthly Rent", 450.0, "Housing", "2026-03-01")

    // Functions & Loops (Week 1 - Thu)
    tracker.addExpense(coffee)
    tracker.addExpense(rent)

    // Review & Practice (Week 1 - Fri/Sat)
    tracker.showExpenses()
    println("\nTotal Spending: \$${tracker.totalSpending}")
    println("Current Status: ${tracker.status()}")

    // Demo CLI Interaction (Week 2 - Fri/Sat)
    println("\n--- Add a new expense ---")
    print("Enter name: ")
    val name = readLine() ?: "Unknown"
    print("Enter amount: ")
    val amount = readLine()?.toDoubleOrNull() ?: 0.0
    print("Enter category: ")
    val category = readLine() ?: "Misc"
    print("Enter date (YYYY-MM-DD): ")
    val date = readLine() ?: "2026-03-12"

    val newExpense = Expense(tracker.hashCode(), name, amount, category, date)
    tracker.addExpense(newExpense)

    println("\nUpdated Expenses:")
    tracker.showExpenses()
    println("Total Spending: \$${tracker.totalSpending}")
    println("Current Status: ${tracker.status()}")
}
