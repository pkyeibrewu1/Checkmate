import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int tableNumber;
    private List<MenuItem> items;
    private boolean isPaid;

    public Order(int orderId, int tableNumber) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.items = new ArrayList<>();
        this.isPaid = false;
    }

    public void addItem(MenuItem item) {
        this.items.add(item);
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
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }

    @Override
    public String toString() {
        return "Order #" + orderId + " (Total: $" + String.format("%.2f", calculateTotal()) + ")";
    }
}