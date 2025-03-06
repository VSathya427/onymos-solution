package solution;

import java.util.Random;

public class OrderThread implements Runnable {
    private static final int NUM_TICKERS = 1024;
    private final IOrderBook orderBook;
    private final Random random = new Random();

    public OrderThread(IOrderBook orderBook) {
        this.orderBook = orderBook;
    }

    @Override
    public void run() {
        // Generate a random order.
        StockOrder.OrderType type = random.nextBoolean() ? StockOrder.OrderType.BUY : StockOrder.OrderType.SELL;
        String ticker = "TICKER-" + random.nextInt(NUM_TICKERS);
        int quantity = random.nextInt(50) + 1;
        double price = 100 + random.nextDouble() * 20;
        // Add the order.
        orderBook.addOrder(type, ticker, quantity, price);
        // Immediately attempt matching for the ticker.
        orderBook.matchOrder(ticker);
    }
}
