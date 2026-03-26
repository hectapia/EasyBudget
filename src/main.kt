import database.DatabaseManager
import models.Expense
import java.util.Scanner

fun main() {
    val db = DatabaseManager("data/budget.db")
    val scanner = Scanner(System.`in`)
    
    println("--- Easy Budget: Sprint 2 Multi-User ---")
    print("Enter your username: ")
    val username = scanner.next()
    
    // Simple User Registration/Lookup logic
    var userId = db.addUser(username) // In a real app, you'd check if user exists first
    if (userId == -1) userId = 1 // Fallback for existing users in this simple demo

    println("Welcome, $username! Your User ID is $userId")
    
    var running = true
    while (running) {
        println("\n1. Set Budget | 2. Add Expense | 3. View Summary | 4. Exit")
        print("Selection: ")
        
        when (scanner.next()) {
            "1" -> {
                print("Enter budget limit: ")
                val limit = scanner.nextDouble()
                try {
                    db.setBudget(userId, limit)
                    println("Budget set to $$limit")
                } catch (e: Exception) { println(e.message) }
            }
            "2" -> {
                print("Name: "); val name = scanner.next()
                print("Amount: "); val amount = scanner.nextDouble()
                print("Category: "); val cat = scanner.next()
                print("Date (YYYY-MM-DD): "); val date = scanner.next()
                
                val success = db.addExpense(userId, Expense(null, name, amount, cat, date))
                if (success) println("Expense processed.")
            }
            "3" -> {
                val total = db.getTotalSpending(userId)
                val limit = db.getBudgetLimit(userId)
                println("\n--- Summary ---")
                println("Budget Limit: $$limit")
                println("Total Spent:  $$total")
                println("Remaining:    $${limit - total}")
                
                println("\nHistory:")
                db.getAllExpenses(userId).forEach { 
                    println("${it.date} | ${it.name} | $${it.amount}") 
                }
            }
            "4" -> running = false
        }
    }
}