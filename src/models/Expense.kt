package models

// Data class representing an expense entry
data class Expense(
    val id: Int? = null, 
    val userId: Int, 
    val budgetId: Int, 
    val name: String, 
    val amount: Double, 
    val date: String
)