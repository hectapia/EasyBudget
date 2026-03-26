package models

/**
 * Sprint 2 Refactored: Added date field for filtering.
 */
data class Expense(
    val id: Int? = null,
    val name: String,
    val amount: Double,
    val category: String,
    val date: String  // Format: YYYY-MM-DD
)