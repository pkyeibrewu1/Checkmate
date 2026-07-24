import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int tableNumber;
    private List<MenuItem> items;
    private OrderStatus status;

    public Order(int orderId, int tableNumber) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.items = new ArrayList<>();
        this.status = OrderStatus.IN_PROGRESS;
    }

    public void addItem(MenuItem item) {
        if (status == OrderStatus.IN_PROGRESS) {
            this.items.add(item);
        }
    }

    public boolean removeItem(MenuItem item) {
        if (status == OrderStatus.IN_PROGRESS) {
            return this.items.remove(item);
        }
        return false;
    }

    public double calculateTotal() {
        double total = 0.0;
        for (MenuItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    public int getOrderId() { return orderId; }
    public int getTableNumber() { return tableNumber; }
    public List<MenuItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Order #" + orderId + " | Status: " + status + " | Total: $" + String.format("%.2f", calculateTotal());
    }
}