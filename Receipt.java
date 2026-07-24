import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Receipt {
    private String receiptId;
    private Order order;
    private double totalAmount;
    private String timestamp;

    public Receipt(String receiptId, Order order) {
        this.receiptId = receiptId;
        this.order = order;
        this.totalAmount = order.calculateTotal();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.now().format(dtf);
    }

    public String getReceiptId() { return receiptId; }
    public Order getOrder() { return order; }
    public double getTotalAmount() { return totalAmount; }
    public String getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "=============================\n" +
               "       RECEIPT #" + receiptId + "\n" +
               "       Time: " + timestamp + "\n" +
               "=============================\n" +
               "Order Items:\n" + getFormattedItems() +
               "Total Paid: $" + String.format("%.2f", totalAmount) + "\n" +
               "STATUS: VERIFIED & PAID\n" +
               "=============================";
    }

    private String getFormattedItems() {
        StringBuilder sb = new StringBuilder();
        for (MenuItem item : order.getItems()) {
            sb.append(" - ").append(item.getName()).append(": $").append(String.format("%.2f", item.getPrice())).append("\n");
        }
        return sb.toString();
    }
}