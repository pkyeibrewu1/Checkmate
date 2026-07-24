import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Receipt implements Serializable {
    private static final long serialVersionUID = 1L;

    private String receiptId;
    private Order order;
    private double totalAmount;
    private String timestamp;
    private boolean verifiedAtExit;

    public Receipt(String receiptId, Order order) {
        this.receiptId = receiptId;
        this.order = order;
        this.totalAmount = order.calculateTotal();
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.now().format(dtf);
        this.verifiedAtExit = false;
    }

    public String getReceiptId() { return receiptId; }
    public Order getOrder() { return order; }
    public double getTotalAmount() { return totalAmount; }
    public String getTimestamp() { return timestamp; }
    public boolean isVerifiedAtExit() { return verifiedAtExit; }
    public void setVerifiedAtExit(boolean verifiedAtExit) { this.verifiedAtExit = verifiedAtExit; }

    @Override
    public String toString() {
        return "Receipt " + receiptId + " | Paid: $" + String.format("%.2f", totalAmount) + 
               " | Exit Verified: " + (verifiedAtExit ? "Yes" : "No");
    }
}