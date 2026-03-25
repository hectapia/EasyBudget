import database.DatabaseManager
import models.Expense
import java.util.Scanner

fun main() {
    val db = DatabaseManager("data/budget.db")
    val scanner = Scanner(System.`in`)
    var running = true

    println("--- Easy Budget: Sprint 2 CLI ---")

    while (running) {
        println("\n1. View Expenses | 2. Add | 3. Update Amount | 4. Delete | 5. Total | 6. Exit")
        print("Selection: ")
        
        when (scanner.next()) {
            "1" -> {
                println("\nID | Name | Amount | Category")
                db.getAllExpenses().forEach { 
                    println("${it.id} | ${it.name} | $${it.amount} | ${it.category}") 
                }
            }
            "2" -> {
                print("Name: "); val name = scanner.next()
                print("Amount: "); val amount = scanner.nextDouble()
                print("Category: "); val cat = scanner.next()
                db.addExpense(Expense(null, name, amount, cat))
                println("Expense Added!")
            }
            "3" -> {
                print("Enter ID to update: "); val id = scanner.nextInt()
                print("Enter new amount: "); val newAmt = scanner.nextDouble()
                db.updateExpense(id, newAmt)
                println("Updated!")
            }
            "4" -> {
                print("Enter ID to delete: "); val id = scanner.nextInt()
                db.deleteExpense(id)
                println("Deleted!")
            }
            "5" -> {
                val total = db.getTotalSpending()
                println("Total Budget Spent: $$total")
            }
            "6" -> running = false
            else -> println("Invalid option.")
        }
    }
    println("Goodbye!")
}