# Overview

## Sprin 1
EasyBudget is a simple command-line expense tracker built in Kotlin. The goal of this project is to strengthen my skills as a software engineer by exploring the fundamentals of the Kotlin language while building a practical tool. Through this project, I aimed to understand how Kotlin handles variables, conditionals, loops, functions, classes, properties, and data classes, and how these concepts can be applied to real-world software.

The software allows users to record expenses, categorize them, and compare total spending against a budget limit. It demonstrates how Kotlin’s syntax and features can be used to implement clean, concise, and expressive code. My purpose in writing this software was to deepen my knowledge of Kotlin’s object-oriented programming capabilities and practice building a small but functional CLI application.

## Sprin 2
As a software engineer, I am developing **Easy Budget** to master the architectural challenges of "Offline-First" mobile development. My focus for this stage of learning is implementing a robust data abstraction layer that allows a Kotlin application to switch seamlessly between a local SQLite database and a cloud-hosted PostgreSQL database via Supabase.

Easy Budget is a financial management system designed to provide real-time budget tracking regardless of connectivity. This current module serves as the logic and data engine, demonstrating a "Repository Pattern" in Kotlin. It handles complex SQL operations, including data persistence, multi-table schema management, and aggregate calculations to provide users with a clear view of their financial health.

The purpose of this software is to solve the problem of data availability in mobile environments. By writing a hybrid storage engine, I am learning how to manage connection states, handle SQL dialect differences between SQLite and PostgreSQL, and maintain data integrity across different environments. This groundwork is essential for the final phase of building a fully synchronized Android application.

[Software Demo Video](https://youtu.be/QyvKhQnDOjU) Sprin 1
[Software Demo Video](http://youtube.link.goes.here) Sprin 2

# Development Environment

- **IDE/Editor:** Visual Studio Code & Kotlin Playground (for quick testing)
- **Programming Language:** Kotlin
- **SQLite:** Used for local, serverless data persistence on the device.

The software is built using **Kotlin 1.9** and leverages:
* **JDBC (Java Database Connectivity):** To build and submit dynamic SQL commands.
* **The Repository Pattern:** An architectural design that decouples the application logic from the specific database implementation.

### How to run Sprin 1
```
kotlinc EasyBudget.kt -include-runtime -d EasyBudget.jar
java -jar CLI-EasyBudget.jar
```

### How to run Sprin 2
```
kotlinc src/main.kt src/models/Expense.kt src/database/DatabaseManager.kt -cp "lib/sqlite-jdbc-3.51.2.0.jar" -include-runtime -d EasyBudget.jar
java -cp "EasyBudget.jar;lib/sqlite-jdbc-3.51.2.0.jar" MainKt
```

# Useful Websites

- [Kotlin Official Documentation](https://kotlinlang.org/docs/home.html)  
- [Kotlin Playground](https://play.kotlinlang.org/)  
- [JetBrains Blog on Kotlin](https://blog.jetbrains.com/kotlin/)  
- [Stack Overflow](https://stackoverflow.com/questions/tagged/kotlin)  
- [GeeksforGeeks Kotlin Tutorials](https://www.geeksforgeeks.org/kotlin-programming-language/)  
