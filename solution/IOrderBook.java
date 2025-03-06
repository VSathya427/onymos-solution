package solution;

public interface IOrderBook {
    void addOrder(StockOrder.OrderType type, String ticker, int quantity, double price);

    void matchOrder(String ticker);
}
