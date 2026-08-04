package com.checkmate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Restaurant implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "checkmate_data.ser";

    private String name;
    private List<Table> tables;
    private List<MenuItem> menu;
    private List<Receipt> receipts;
    private int nextOrderId = 100;

    public Restaurant(String name) {
        this.name = name;
        this.tables = new ArrayList<>();
        this.menu = new ArrayList<>();
        this.receipts = new ArrayList<>();
    }

    // --- PERSISTENCE ---

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("Error saving restaurant data: " + e.getMessage());
        }
    }

    public static Restaurant loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Restaurant loaded = (Restaurant) ois.readObject();
            System.out.println("System state loaded from previous session!");
            return loaded;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load previous save file. Starting fresh.");
            return null;
        }
    }

    // --- SETUP & LOOKUPS ---

    public void addTable(Table table) { 
        tables.add(table); 
    }

    public void addMenuItem(MenuItem item) { 
        menu.add(item); 
    }

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

    // --- BUSINESS LOGIC ---

    public boolean seatTable(int tableNumber) {
        Table table = findTable(tableNumber);
        if (table == null) {
            System.out.println("Error: Table " + tableNumber + " does not exist.");
            return false;
        }
        if (table.isOccupied()) {
            System.out.println("Error: Table " + tableNumber + " is already occupied!");
            return false;
        }

        table.seatTable();
        System.out.println("Success: Table " + tableNumber + " is now seated.");
        saveData();
        return true;
    }

    public Order createOrder(int tableNumber) {
        Table table = findTable(tableNumber);
        if (table == null) {
            System.out.println("Error: Table " + tableNumber + " does not exist.");
            return null;
        }
        if (!table.isOccupied()) {
            System.out.println("Error: Cannot create order for Table " + tableNumber + " because it is not occupied.");
            return null;
        }
        if (table.getCurrentOrder() != null) {
            System.out.println("Error: Table " + tableNumber + " already has an active order!");
            return null;
        }

        Order newOrder = new Order(nextOrderId++, tableNumber);
        table.setCurrentOrder(newOrder);
        System.out.println("Success: Order #" + newOrder.getOrderId() + " created for Table " + tableNumber);
        saveData();
        return newOrder;
    }

    public void addItemToOrder(int tableNumber, MenuItem item) {
        Table table = findTable(tableNumber);
        if (table != null && table.getCurrentOrder() != null) {
            table.getCurrentOrder().addItem(item);
            System.out.println("Added " + item.getName() + " to Table " + tableNumber + " order.");
            saveData();
        } else {
            System.out.println("Error: Active order not found for Table " + tableNumber);
        }
    }

    public void requestBill(int tableNumber) {
        Table table = findTable(tableNumber);
        if (table != null && table.getCurrentOrder() != null) {
            table.getCurrentOrder().setStatus(OrderStatus.BILL_REQUESTED);
            System.out.println("Bill requested for Table " + tableNumber + ". Total due: $" + 
                               String.format("%.2f", table.getCurrentOrder().calculateTotal()));
            saveData();
        } else {
            System.out.println("Error: Table " + tableNumber + " has no order to bill.");
        }
    }

    public Receipt recordPayment(int tableNumber) {
        Table table = findTable(tableNumber);
        if (table == null || !table.isOccupied() || table.getCurrentOrder() == null) {
            System.out.println("Error: No active order to pay for Table " + tableNumber);
            return null;
        }

        Order order = table.getCurrentOrder();

        // Prevent payment on empty orders ($0.00 total)
        if (order.getItems().isEmpty() || order.calculateTotal() <= 0) {
            System.out.println("Error: Cannot process payment for an empty order at Table " + tableNumber);
            return null;
        }

        if (order.getStatus() == OrderStatus.PAID) {
            System.out.println("Error: Order for Table " + tableNumber + " has already been paid!");
            return null;
        }

        order.setStatus(OrderStatus.PAID);
        
        // Generate unpredictable 6-character random code (e.g., CM-8F2K9P)
        String randomCode = generateRandomReceiptId();
        Receipt receipt = new Receipt(randomCode, order);
        receipts.add(receipt);

        System.out.println("Payment received for Table " + tableNumber + ". Generated " + receipt.getReceiptId());
        saveData();
        return receipt;
    }

    // Generates an unguessable 6-character alphanumeric receipt token
    private String generateRandomReceiptId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder("CM-");
        Random rnd = new Random();
        
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public boolean verifyReceipt(String receiptId) {
        System.out.println("\n--- Security Verification Gate ---");
        for (Receipt receipt : receipts) {
            if (receipt.getReceiptId().equalsIgnoreCase(receiptId)) {
                // Block already-scanned receipts
                if (receipt.isVerifiedAtExit()) {
                    System.out.println("Status: DENIED | Reason: Receipt " + receiptId + " was already used for exit!");
                    return false;
                }

                // Verify status is PAID and amount > $0.00
                if (receipt.getOrder().getStatus() == OrderStatus.PAID && receipt.getTotalAmount() > 0) {
                    receipt.setVerifiedAtExit(true);
                    
                    // Automatically clear the table upon valid exit verification
                    int tableToClear = receipt.getOrder().getTableNumber();
                    clearTable(tableToClear);

                    System.out.println("Status: APPROVED | Payment Verified. Exit Allowed for Receipt " + receiptId + ". Table " + tableToClear + " automatically cleared!");
                    saveData();
                    return true;
                } else {
                    System.out.println("Status: DENIED | Unpaid or invalid receipt amount.");
                    return false;
                }
            }
        }
        System.out.println("Status: DENIED | Invalid receipt ID: " + receiptId);
        return false;
    }

    public void clearTable(int tableNumber) {
        Table table = findTable(tableNumber);
        if (table == null) {
            System.out.println("Error: Table " + tableNumber + " does not exist.");
            return;
        }
        if (!table.isOccupied()) {
            System.out.println("Error: Table " + tableNumber + " is already available.");
            return;
        }

        table.clearTable();
        System.out.println("Table " + tableNumber + " has been cleared and is now available.");
        saveData();
    }

    public String getName() { return name; }
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
                        System.out.println(indent + "      Receipt: " + r.getReceiptId() + 
                                           " (Verified Exit: " + r.isVerifiedAtExit() + ")");
                        break;
                    }
                }
            }
        }
    }
}