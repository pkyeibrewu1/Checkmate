public class Main {
    public static void main(String[] args) {
        // Setup Restaurant & Menu
        Restaurant restaurant = new Restaurant("CheckMate Diner");
        for (int i = 1; i <= 5; i++) {
            restaurant.addTable(new Table(i));
        }

        MenuItem burger = new MenuItem(1, "Burger", 11.99);
        MenuItem fries = new MenuItem(2, "Fries", 3.99);
        MenuItem drink = new MenuItem(3, "Drink", 2.49);

        System.out.println("=== STARTING SERVER SHIFT WORKFLOW ===\n");

        // 1. Seat Table 4
        restaurant.seatTable(4);

        // 2. Create Order
        restaurant.createOrder(4);

        // 3. Add Items
        restaurant.addItemToOrder(4, burger);
        restaurant.addItemToOrder(4, fries);
        restaurant.addItemToOrder(4, drink);

        // 4. Request Bill
        restaurant.requestBill(4);

        // 5. Pay Bill & Generate Receipt
        Receipt receipt = restaurant.recordPayment(4);

        // 6. Verify Receipt at Exit
        if (receipt != null) {
            restaurant.verifyReceipt(receipt.getReceiptId());
            
            // Testing double-use exit prevention
            restaurant.verifyReceipt(receipt.getReceiptId()); 
        }

        // 7. Clear Table
        restaurant.clearTable(4);

        System.out.println("\n=== WORKFLOW COMPLETE ===");
    }
}