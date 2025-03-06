# onymos-solution

## Overview

This project implements a real-time stock trading engine in Java that simulates matching buy and sell orders concurrently. The engine supports up to 1,024 unique tickers and uses lock-free data structures to ensure high performance in a concurrent environment. The design leverages interfaces and the Strategy pattern to decouple components and improve extensibility.

## Features

- **Lock-Free Order Book:**  
  Uses atomic references to maintain per-ticker linked lists without traditional locking mechanisms.

- **Per-Ticker Order Books:**  
  Orders are stored in separate arrays for BUY and SELL orders, indexed by a simple hash function to support up to 1,024 tickers.

- **Real-Time Order Matching:**  
  Orders are added and immediately matched based on price criteria. Buy orders with prices greater than or equal to the lowest sell order are executed immediately.

- **Concurrent Processing:**  
  The system simulates a real-time trading environment by spawning multiple threads to process orders concurrently.

- **Strategy Pattern:**  
  The matching logic is encapsulated in a matching strategy interface, allowing easy replacement or extension of the matching algorithm.

## Project Structure

- **StockOrder.java:**  
  Defines the `StockOrder` class representing a stock order with a type (BUY or SELL), ticker, quantity, and price. An `AtomicReference` is used for the linked list pointer to support lock-free operations.

- **IOrderBook.java:**  
  An interface that declares methods for adding and matching orders.

- **IOrderMatchingStrategy.java:**  
  An interface that defines the contract for matching orders.

- **LockFreeOrderBook.java:**  
  Implements the `IOrderBook` interface using arrays of `AtomicReference` for managing per-ticker order books.

- **LowestPriceMatchingStrategy.java:**  
  Implements the `IOrderMatchingStrategy` interface and contains the logic to match orders based on the lowest available sell price.

- **OrderThread.java:**  
  Implements the `Runnable` interface and encapsulates the process of creating an order, adding it to the order book, and triggering matching.

- **StockExchangeSimulator.java:**  
  The main class that prompts the user for the number of threads to spawn and then creates those threads to simulate the trading environment.

## Getting Started

### Prerequisites

- Java JDK 8 or later
- A terminal or IDE for running Java applications

### Compilation and Execution
1. **Clone the project:**

   Clone the repository. For example, using the command line:

   ```bash
   git clone <project-url>
   ```

2. **Compile the project:**

   Navigate to the project's root directory and compile the source files. For example, using the command line:

   ```bash
   javac solution/*.java
   ```

1. **Run the project:**

   Execute the compiled program:

   ```bash
   java solution/StockExchangeSimulator
   ```

   The simulator will prompt you to enter the number of threads to spawn. Enter a numeric value and press Enter. The simulation will then start, and you will see real-time output for order additions and matching events.

## Design Decisions

- **Lock-Free Concurrency:**  
  The engine uses atomic references to manage order insertion and removal, avoiding traditional locks and ensuring high performance under concurrent access.

- **Per-Ticker Data Organization:**  
  Instead of using dynamic collections like maps, the order book is organized into fixed-size arrays to support 1,024 tickers, with a simple hash function mapping tickers to array indices.

- **Modular Design with Interfaces:**  
  Interfaces such as `IOrderBook` and `IOrderMatchingStrategy` decouple the core logic from its implementation, making the code easier to test, extend, and maintain.

## Future Enhancements

- Implement more sophisticated matching algorithms.
- Integrate persistence and logging mechanisms.
- Expand the simulation to include additional order types and trading rules.
- Enhance error handling and system robustness.
