import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private String name;
    private List<Table> tables;
    private List<MenuItem> menu;
    private List<Receipt> receipts;
    private int nextOrderId = 100;
    private int nextReceiptId = 1000;

    public Restaurant(String name) {
        this.name = name;
        this.tables = new ArrayList<>();
        this.menu = new ArrayList<>();
        this.receipts = new ArrayList<>();
    }

    public void addTable(Table table) { tables.add(table); }
    public void addMenuItem(MenuItem item) { menu.add(item); }

    public Table findTable(int tableNumber) {
        for (Table table : tables) {
            if (table.getTableNumber() == tableNumber) {
                return table;
            }
        }
        return null;
    }

    public MenuItem findMenuItemById(int id) {
        for (MenuItem item : menu) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public boolean seatTable(int tableNumber) {
        Table table = findTable(tableNumber);
        if (table != null && !table.isOccupied()) {
            table.seatTable();
            System.out.println("Success: Table " + tableNumber + " is now seated.");
            return true;
        }
        System.out.println("Error: Table " + tableNumber + " is either occupied or invalid.");
        return false;
    }

    public Order createOrder(int tableNumber) {
        Table table = findTable(tableNumber);
        if (table != null && table.isOccupied() && table.getCurrentOrder() == null) {
            Order newOrder = new Order(nextOrderId++, tableNumber);
            table.setCurrentOrder(newOrder);
            System.out.println("Success: Order #" + newOrder.getOrderId() + " created for Table " + tableNumber);
            return newOrder;
        }
        System.out.println("Error: Cannot create order for Table " + tableNumber);
        return null;
    }

    public void addItemToOrder(int tableNumber, MenuItem item) {
        Table table = findTable(tableNumber);
        if (table != null && table.getCurrentOrder() != null) {
            table.getCurrentOrder().addItem(item);
            System.out.println("Added " + item.getName() + " to Table " + tableNumber + " order.");
        }
    }

    public void requestBill(int tableNumber) {
        Table table = findTable(tableNumber);
        if (table != null && table.getCurrentOrder() != null) {
            table.getCurrentOrder().setStatus(OrderStatus.BILL_REQUESTED);
            System.out.println("Bill requested for Table " + tableNumber + ". Total due: $" + 
                               String.format("%.2f", table.getCurrentOrder().calculateTotal()));
        }
    }

    public Receipt recordPayment(int tableNumber) {
        Table table = findTable(tableNumber);
        if (table != null && table.getCurrentOrder() != null) {
            Order order = table.getCurrentOrder();
            order.setStatus(OrderStatus.PAID);

            Receipt receipt = new Receipt("CM-" + nextReceiptId++, order);
            receipts.add(receipt);
            System.out.println("Payment received for Table " + tableNumber + ". Generated " + receipt.getReceiptId());
            return receipt;
        }
        return null;
    }

    public boolean verifyReceipt(String receiptId) {
        System.out.println("\n--- Security Verification Gate ---");
        for (Receipt receipt : receipts) {
            if (receipt.getReceiptId().equalsIgnoreCase(receiptId)) {
                if (receipt.isVerifiedAtExit()) {
                    System.out.println("Status: DENIED | Reason: Receipt already used for exit.");
                    return false;
                }
                if (receipt.getOrder().getStatus() == OrderStatus.PAID) {
                    receipt.setVerifiedAtExit(true);
                    System.out.println("Status: APPROVED | Payment Verified. Exit Allowed for Receipt " + receiptId);
                    return true;
                }
            }
        }
        System.out.println("Status: DENIED | Invalid or unpaid receipt: " + receiptId);
        return false;
    }

    public void clearTable(int tableNumber) {
        Table table = findTable(tableNumber);
        if (table != null && table.isOccupied()) {
            table.clearTable();
            System.out.println("Table " + tableNumber + " has been cleared and is now available.");
        }
    }

    public List<Table> getTables() { return tables; }
    public List<MenuItem> getMenu() { return menu; }

    public void printRestaurantTree() {
        System.out.println("\n=== HIERARCHY TREE ===");
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
                System.out.println(indent + "      Status: " + order.getStatus());

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