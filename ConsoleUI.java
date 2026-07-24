import java.util.Scanner;

public class ConsoleUI {
    private Restaurant restaurant;
    private Scanner scanner;

    public ConsoleUI(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMainMenu();
            System.out.print("Select an option (1-8): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    handleSeatTable();
                    break;
                case "2":
                    handleViewTables();
                    break;
                case "3":
                    handleManageOrder();
                    break;
                case "4":
                    handleProcessPayment();
                    break;
                case "5":
                    handleVerifyReceipt();
                    break;
                case "6":
                    handleClearTable();
                    break;
                case "7":
                    restaurant.printRestaurantTree();
                    break;
                case "8":
                    System.out.println("Exiting CheckMate. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 8.");
            }
            System.out.println();
        }
    }

    private void displayMainMenu() {
        System.out.println("=============================");
        System.out.println("         CHECKMATE           ");
        System.out.println("=============================");
        System.out.println("1. Seat a Table");
        System.out.println("2. View Restaurant Status");
        System.out.println("3. Manage Order (Add/Remove Items)");
        System.out.println("4. Process Payment");
        System.out.println("5. Verify Receipt (Exit Gate)");
        System.out.println("6. Clear Table");
        System.out.println("7. View Full Restaurant Hierarchy");
        System.out.println("8. Exit");
        System.out.println("=============================");
    }

    private void handleSeatTable() {
        System.out.print("Enter Table Number to seat: ");
        int tableNum = readInt();
        if (tableNum == -1) return;

        boolean success = restaurant.seatTable(tableNum);
        if (success) {
            restaurant.createOrder(tableNum);
        }
    }

    private void handleViewTables() {
        System.out.println("\n--- RESTAURANT DASHBOARD ---");
        for (Table table : restaurant.getTables()) {
            String statusStr = "Available";
            if (table.isOccupied()) {
                Order order = table.getCurrentOrder();
                if (order == null) {
                    statusStr = "Occupied";
                } else {
                    switch (order.getStatus()) {
                        case IN_PROGRESS:
                            statusStr = "Occupied (Ordering)";
                            break;
                        case BILL_REQUESTED:
                            statusStr = "Awaiting Payment";
                            break;
                        case PAID:
                            statusStr = "Paid (Pending Exit)";
                            break;
                    }
                }
            }
            System.out.println("Table " + table.getTableNumber() + " - " + statusStr);
        }
    }

    private void handleManageOrder() {
        System.out.print("Enter Table Number: ");
        int tableNum = readInt();
        if (tableNum == -1) return;

        Table table = restaurant.findTable(tableNum);
        if (table == null || !table.isOccupied() || table.getCurrentOrder() == null) {
            System.out.println("Error: Table is either not occupied or has no active order.");
            return;
        }

        Order order = table.getCurrentOrder();
        boolean managing = true;

        while (managing) {
            System.out.println("\n--- Managing Table " + tableNum + " ---");
            System.out.println("Current Order Items: " + order.getItems().size());
            System.out.println("Current Total: $" + String.format("%.2f", order.calculateTotal()));
            System.out.println("1. Add Menu Item");
            System.out.println("2. Remove Menu Item");
            System.out.println("3. View Current Order Details");
            System.out.println("4. Request Bill");
            System.out.println("5. Back to Main Menu");
            System.out.print("Select choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    displayMenuCatalog();
                    System.out.print("Enter Item ID to add: ");
                    int addId = readInt();
                    MenuItem itemToAdd = restaurant.findMenuItemById(addId);
                    if (itemToAdd != null) {
                        restaurant.addItemToOrder(tableNum, itemToAdd);
                    } else {
                        System.out.println("Invalid Menu Item ID.");
                    }
                    break;

                case "2":
                    if (order.getItems().isEmpty()) {
                        System.out.println("Order has no items to remove.");
                        break;
                    }
                    System.out.println("Current Items in Order:");
                    for (int i = 0; i < order.getItems().size(); i++) {
                        System.out.println((i + 1) + ". " + order.getItems().get(i));
                    }
                    System.out.print("Enter number of item to remove: ");
                    int removeIdx = readInt() - 1;
                    if (removeIdx >= 0 && removeIdx < order.getItems().size()) {
                        MenuItem removed = order.getItems().remove(removeIdx);
                        System.out.println("Removed " + removed.getName() + " from order.");
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case "3":
                    System.out.println("\n--- ORDER DETAILS ---");
                    if (order.getItems().isEmpty()) {
                        System.out.println("(No items added yet)");
                    } else {
                        for (MenuItem mi : order.getItems()) {
                            System.out.println(" - " + mi);
                        }
                    }
                    System.out.println("Total: $" + String.format("%.2f", order.calculateTotal()));
                    break;

                case "4":
                    restaurant.requestBill(tableNum);
                    managing = false;
                    break;

                case "5":
                    managing = false;
                    break;

                default:
                    System.out.println("Invalid selection.");
            }
        }
    }

    private void displayMenuCatalog() {
        System.out.println("\n--- MENU CATALOG ---");
        for (MenuItem item : restaurant.getMenu()) {
            System.out.println("ID " + item.getId() + ": " + item.getName() + " - $" + String.format("%.2f", item.getPrice()));
        }
    }

    private void handleProcessPayment() {
        System.out.print("Enter Table Number to pay: ");
        int tableNum = readInt();
        if (tableNum == -1) return;

        Receipt receipt = restaurant.recordPayment(tableNum);
        if (receipt != null) {
            System.out.println("\n=============================");
            System.out.println("    PAYMENT SUCCESSFUL      ");
            System.out.println(" Receipt ID: " + receipt.getReceiptId());
            System.out.println(" Total Paid: $" + String.format("%.2f", receipt.getTotalAmount()));
            System.out.println("=============================");
        }
    }

    private void handleVerifyReceipt() {
        System.out.print("Enter Receipt ID to verify at exit: ");
        String receiptId = scanner.nextLine().trim();
        
        boolean verified = restaurant.verifyReceipt(receiptId);
        if (verified) {
            // Find which table had this receipt and offer to clear it
            System.out.print("Would you like to clear this table now? (y/n): ");
            String ans = scanner.nextLine().trim();
            if (ans.equalsIgnoreCase("y")) {
                // Find table matching order in receipt and clear
                for (Table table : restaurant.getTables()) {
                    if (table.getCurrentOrder() != null && table.getCurrentOrder().isPaid()) {
                        restaurant.clearTable(table.getTableNumber());
                        break;
                    }
                }
            }
        }
    }

    private void handleClearTable() {
        System.out.print("Enter Table Number to clear: ");
        int tableNum = readInt();
        if (tableNum == -1) return;

        restaurant.clearTable(tableNum);
    }

    private int readInt() {
    while (true) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.print("Invalid entry. Please enter a valid number: ");
        }
    }
}
}