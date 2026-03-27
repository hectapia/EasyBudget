import database.DatabaseManager
import models.Expense
import java.util.Scanner

// Main entry point for the EasyBudget application
fun main() {
    val db = DatabaseManager("data/budget.db")
    val scanner = Scanner(System.`in`)
    
    println("--- 📊 EasyBudget: Sprint 2 ---")
    print("Login - Enter Username: ")
    val username = scanner.next()
    
    val userId = db.addUser(username)
    if (userId == -1) {
        println("❌ Error: Could not verify user.")
        return 
    }

    println("Debug: Logged in as $username (ID: $userId)")

    var running = true
    while (running) {
        println("\nMain Menu")
        println("1. Manage Budgets (Categories)")
        println("2. Manage Expenses (Tracking)")
        println("3. View Summary (Join Query)")
        println("4. Exit")
        print("Selection: ")

        when (scanner.next()) {
            "1" -> manageBudgetsMenu(db, userId, scanner)
            "2" -> manageExpensesMenu(db, userId, scanner)
            "3" -> db.getBudgetStatus(userId)
            "4" -> running = false
            else -> println("Invalid selection.")
        }
    }
    println("Goodbye!")
}

// Menu for managing budgets (categories)
fun manageBudgetsMenu(db: DatabaseManager, userId: Int, scanner: Scanner) {
    var inMenu = true
    while (inMenu) {
        println("\n--- Budget Management ---")
        println("1. Set Category Limit | 2. Back")
        print("Selection: ")
        when (scanner.next()) {
            "1" -> {
                print("Category: "); val cat = scanner.next()
                print("Limit: "); val lim = scanner.nextDouble()
                db.createBudget(userId, cat, lim)
                println("✅ Budget Set.")
            }
            "2" -> inMenu = false
        }
    }
}

// Menu for managing expenses (tracking)
fun manageExpensesMenu(db: DatabaseManager, userId: Int, scanner: Scanner) {
    var inMenu = true
    while (inMenu) {
        println("\n--- Expense Tracking ---")
        println("1. View History | 2. Add | 3. Update | 4. Delete | 5. Back")
        print("Selection: ")
        when (scanner.next()) {
            "1" -> db.getViewExpenseHistory(userId)
            "2" -> {
                print("Budget ID: "); val bId = scanner.nextInt()
                print("Name: "); val name = scanner.next()
                print("Amount: "); val amt = scanner.nextDouble()
                print("Date (YYYY-MM-DD): "); val date = scanner.next()
                if (db.addExpense(userId, bId, name, amt, date)) println("✅ Added.")
            }
            "3" -> {
                print("Expense ID: "); val eId = scanner.nextInt()
                print("New Amount: "); val nAmt = scanner.nextDouble()
                if (db.updateExpenseAmount(eId, nAmt)) println("✅ Updated.")
            }
            "4" -> {
                print("Expense ID: "); val eId = scanner.nextInt()
                db.deleteExpense(eId)
                println("🗑️ Deleted.")
            }
            "5" -> inMenu = false
        }
    }
}