package solution;

import java.util.concurrent.atomic.AtomicReference;

public class StockOrder {
    public enum OrderType { BUY, SELL }

    public OrderType type;
    public String ticker;
    public int quantity;
    public double price;
    // Using an atomic reference for the next pointer enables lock-free linked list updates.
    public AtomicReference<StockOrder> next;

    public StockOrder(OrderType type, String ticker, int quantity, double price) {
        this.type = type;
        this.ticker = ticker;
        this.quantity = quantity;
        this.price = price;
        this.next = new AtomicReference<>(null);
    }
}
