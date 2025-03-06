package solution;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeOrderBook implements IOrderBook {
    private static final int TICKER_SIZE = 1024;
    // Two arrays—one for BUY orders and one for SELL orders—for all tickers.
    private final AtomicReference<StockOrder>[] buyHeads;
    private final AtomicReference<StockOrder>[] sellHeads;
    private final IOrderMatchingStrategy matchingStrategy;

    @SuppressWarnings("unchecked")
    public LockFreeOrderBook(IOrderMatchingStrategy matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
        buyHeads = new AtomicReference[TICKER_SIZE];
        sellHeads = new AtomicReference[TICKER_SIZE];
        for (int i = 0; i < TICKER_SIZE; i++) {
            buyHeads[i] = new AtomicReference<>(null);
            sellHeads[i] = new AtomicReference<>(null);
        }
    }

    // Maps a ticker symbol to an index between 0 and 1023.
    private int getTickerIndex(String ticker) {
        return Math.abs(ticker.hashCode()) % TICKER_SIZE;
    }

    @Override
    public void addOrder(StockOrder.OrderType type, String ticker, int quantity, double price) {
        StockOrder newOrder = new StockOrder(type, ticker, quantity, price);
        int index = getTickerIndex(ticker);

        if (type == StockOrder.OrderType.BUY) {
            // Insert at the head of the BUY list using CAS.
            while (true) {
                StockOrder head = buyHeads[index].get();
                newOrder.next.set(head);
                if (buyHeads[index].compareAndSet(head, newOrder)) {
                    break;
                }
            }
        } else {
            // Insert at the head of the SELL list using CAS.
            while (true) {
                StockOrder head = sellHeads[index].get();
                newOrder.next.set(head);
                if (sellHeads[index].compareAndSet(head, newOrder)) {
                    break;
                }
            }
        }
        // Print a message as soon as the order is added.
        System.out.println("Order added: " + type + " " + ticker + " " + quantity + " at $" + price);
        System.out.flush();
    }

    @Override
    public void matchOrder(String ticker) {
        int index = getTickerIndex(ticker);
        matchingStrategy.matchOrders(buyHeads, sellHeads, index);
    }
}
