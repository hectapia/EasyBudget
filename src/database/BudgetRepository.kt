// src/database/BudgetRepository.kt
package database

import models.Expense

interface BudgetRepository {
    fun addExpense(expense: Expense)
    fun getAllExpenses(): List<Expense>
    fun updateExpense(id: Int, newAmount: Double)
    fun deleteExpense(id: Int)
    fun getTotalSpending(): Double
}