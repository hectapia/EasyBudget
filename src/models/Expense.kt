package models

/**
 * Sprint 2: Data Class Requirement
 * This represents a single row in the 'expenses' table.
 */
data class Expense(
    val id: Int? = null,    // Primary Key (null for new items)
    val name: String,
    val amount: Double,
    val category: String
)