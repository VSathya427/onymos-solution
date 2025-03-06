package solution;

import java.util.concurrent.atomic.AtomicReference;

public interface IOrderMatchingStrategy {
    void matchOrders(AtomicReference<StockOrder>[] buyHeads,
            AtomicReference<StockOrder>[] sellHeads,
            int tickerIndex);
}
