/**
 * Sprint 1: Data Class Requirement
 * Represents a single financial transaction.
 */
data class Expense(
    val id: Int,               // Immutable
    var name: String,          // Mutable
    var amount: Double,
    val category: String,
    val date: String
)

fun main() {
    // 1. Variables & Collections
    val budgetLimit = 500.0
    val expenseList = mutableListOf<Expense>()

    // 2. Creating instances of the Data Class
    val coffee = Expense(1, "Coffee", 4.50, "Food", "2026-03-04")
    val rent = Expense(2, "Monthly Rent", 450.0, "Housing", "2026-03-01")

    expenseList.add(coffee)
    expenseList.add(rent)

    // 3. Logic: Loop and Conditional
    var totalSpending = 0.0
    for (item in expenseList) {
        totalSpending += item.amount
    }

    println("Total Spending: \$$totalSpending")

    // 4. Expressions & When Keyword (Bonus Requirement)
    val status = when {
        totalSpending > budgetLimit -> "Over Budget! ⚠️"
        totalSpending == budgetLimit -> "At Limit 🎯"
        else -> "Under Budget ✅"
    }

    println("Current Status: $status")
}