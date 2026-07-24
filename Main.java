public class Main {
    public static void main(String[] args) {
        // 1. Create Restaurant
        Restaurant restaurant = new Restaurant("Checkmate Bistro");

        // 2. Add 10 tables
        for (int i = 1; i <= 10; i++) {
            restaurant.addTable(new Table(i));
        }

        // 3. Create 5 menu items
        MenuItem burger = new MenuItem(1, "Burger", 12.50);
        MenuItem fries = new MenuItem(2, "Fries", 4.50);
        MenuItem coke = new MenuItem(3, "Coke", 2.50);
        MenuItem steak = new MenuItem(4, "Steak", 24.99);
        MenuItem salad = new MenuItem(5, "Salad", 8.99);

        restaurant.addMenuItem(burger);
        restaurant.addMenuItem(fries);
        restaurant.addMenuItem(coke);
        restaurant.addMenuItem(steak);
        restaurant.addMenuItem(salad);

        // 4. Create an order for Table 2
        Table table2 = restaurant.getTables().get(1); // Index 1 is Table 2
        Order order101 = new Order(101, table2.getTableNumber());

        // 5. Add menu items to order
        order101.addItem(burger);
        order101.addItem(fries);
        order101.addItem(coke);

        // 6. Assign order to table
        table2.setCurrentOrder(order101);

        // 7. Process payment & create receipt
        order101.setPaid(true);
        Receipt receipt = new Receipt("CM-0001", order101);
        restaurant.addReceipt(receipt);

        // 8. Print tree output
        restaurant.printRestaurantTree();
    }
}