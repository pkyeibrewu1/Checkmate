import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private String name;
    private List<Table> tables;
    private List<MenuItem> menu;
    private List<Receipt> receipts;

    public Restaurant(String name) {
        this.name = name;
        this.tables = new ArrayList<>();
        this.menu = new ArrayList<>();
        this.receipts = new ArrayList<>();
    }

    public void addTable(Table table) { tables.add(table); }
    public void addMenuItem(MenuItem item) { menu.add(item); }
    public void addReceipt(Receipt receipt) { receipts.add(receipt); }

    public String getName() { return name; }
    public List<Table> getTables() { return tables; }
    public List<MenuItem> getMenu() { return menu; }
    public List<Receipt> getReceipts() { return receipts; }

    public void printRestaurantTree() {
        System.out.println(name);
        for (int i = 0; i < tables.size(); i++) {
            Table table = tables.get(i);
            boolean isLastTable = (i == tables.size() - 1);
            String prefix = isLastTable ? "└── " : "├── ";
            System.out.println(prefix + table);

            if (table.getCurrentOrder() != null) {
                Order order = table.getCurrentOrder();
                String indent = isLastTable ? "    " : "│   ";
                System.out.println(indent + "└── Order #" + order.getOrderId());

                List<MenuItem> items = order.getItems();
                for (MenuItem item : items) {
                    System.out.println(indent + "      ├── " + item.getName());
                }

                System.out.println(indent + "      Total: $" + String.format("%.2f", order.calculateTotal()));
                System.out.println(indent + "      Paid: " + (order.isPaid() ? "Yes" : "No"));

                // Find associated receipt if paid
                for (Receipt r : receipts) {
                    if (r.getOrder().getOrderId() == order.getOrderId()) {
                        System.out.println(indent + "      Receipt: " + r.getReceiptId());
                        break;
                    }
                }
            }
        }
    }
}