package models

// Data class representing a budget category with a spending limit
data class Budget(
    val id: Int? = null, 
    val userId: Int, 
    val category: String, 
    val limitAmount: Double
)