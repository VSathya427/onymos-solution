package solution;

import java.util.Scanner;

public class StockExchangeSimulator {
    public static void main(String[] args) {
        // Ask the user for the number of threads to spawn.
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of threads to spawn: ");
        int numThreads = scanner.nextInt();
        scanner.close();

        IOrderMatchingStrategy matchingStrategy = new LowestPriceMatchingStrategy();
        IOrderBook orderBook = new LockFreeOrderBook(matchingStrategy);

        Thread[] threads = new Thread[numThreads];

        // Create and start threads that execute the OrderThread.
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new OrderThread(orderBook));
            threads[i].start();
            try {
                // Optional delay for more interleaved real-time output.
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Wait for all threads to finish.
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
