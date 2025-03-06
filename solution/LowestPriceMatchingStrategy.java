package solution;

import java.util.concurrent.atomic.AtomicReference;

public class LowestPriceMatchingStrategy implements IOrderMatchingStrategy {

    @Override
    public void matchOrders(AtomicReference<StockOrder>[] buyHeads,
            AtomicReference<StockOrder>[] sellHeads,
            int index) {
        while (true) {
            // Find the SELL order with the lowest price.
            StockOrder lowestSell = findLowestSell(sellHeads, index);
            if (lowestSell == null)
                break;
            // Find a BUY order with a price that meets or exceeds the SELL order's price.
            StockOrder matchingBuy = findMatchingBuy(buyHeads, index, lowestSell.price);
            if (matchingBuy == null)
                break;
            int matchedQuantity = Math.min(matchingBuy.quantity, lowestSell.quantity);
            System.out.println(
                    "Matched " + matchedQuantity + " shares of " + matchingBuy.ticker + " at $" + lowestSell.price);
            System.out.flush();
            // Adjust quantities and remove fully matched orders.
            if (matchingBuy.quantity > lowestSell.quantity) {
                matchingBuy.quantity -= lowestSell.quantity;
                removeOrder(sellHeads, index, lowestSell);
            } else if (lowestSell.quantity > matchingBuy.quantity) {
                lowestSell.quantity -= matchingBuy.quantity;
                removeOrder(buyHeads, index, matchingBuy);
            } else {
                removeOrder(buyHeads, index, matchingBuy);
                removeOrder(sellHeads, index, lowestSell);
            }
        }
    }

    private StockOrder findLowestSell(AtomicReference<StockOrder>[] sellHeads, int index) {
        StockOrder current = sellHeads[index].get();
        StockOrder lowest = null;
        while (current != null) {
            if (lowest == null || current.price < lowest.price) {
                lowest = current;
            }
            current = current.next.get();
        }
        return lowest;
    }

    private StockOrder findMatchingBuy(AtomicReference<StockOrder>[] buyHeads, int index, double sellPrice) {
        StockOrder current = buyHeads[index].get();
        while (current != null) {
            if (current.price >= sellPrice) {
                return current;
            }
            current = current.next.get();
        }
        return null;
    }

    private void removeOrder(AtomicReference<StockOrder>[] heads, int index, StockOrder target) {
        while (true) {
            StockOrder head = heads[index].get();
            if (head == null)
                return;
            if (head == target) {
                if (heads[index].compareAndSet(head, head.next.get())) {
                    return;
                }
            } else {
                StockOrder prev = head;
                StockOrder curr = head.next.get();
                while (curr != null && curr != target) {
                    prev = curr;
                    curr = curr.next.get();
                }
                if (curr == null)
                    return;
                if (prev.next.compareAndSet(curr, curr.next.get())) {
                    return;
                }
            }
        }
    }
}
