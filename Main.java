public class Main {
    public static void main(String[] args) {
        // 1. Initialize Restaurant
        Restaurant restaurant = new Restaurant("Bistro Safeguard");

        // 2. Manually create 10 tables
        for (int i = 1; i <= 10; i++) {
            restaurant.addTable(new Table(i, i % 2 == 0 ? 4 : 2));
        }

        // 3. Manually create 5 menu items
        MenuItem burger = new MenuItem(101, "Classic Burger", 12.99);
        MenuItem pizza = new MenuItem(102, "Margherita Pizza", 14.50);
        MenuItem pasta = new MenuItem(103, "Penne Alfredo", 13.00);
        MenuItem soda = new MenuItem(104, "Fountain Soda", 2.50);
        MenuItem cake = new MenuItem(105, "Chocolate Cake", 6.50);

        restaurant.addMenuItem(burger);
        restaurant.addMenuItem(pizza);
        restaurant.addMenuItem(pasta);
        restaurant.addMenuItem(soda);
        restaurant.addMenuItem(cake);

        // 4. Create 1 order and add items to it
        Order order1 = new Order(5001);
        order1.addItem(burger);
        order1.addItem(soda);
        order1.addItem(cake);

        // 5. Assign order to Table 3 and set occupied
        Table table3 = restaurant.getTables().get(2); // Table 3
        table3.setOccupied(true);
        table3.setCurrentOrder(order1);

        // 6. Simulate payment and generate receipt
        order1.setPaid(true);
        Receipt receipt1 = new Receipt("REC-10001", order1);

        // 7. Print setup and receipt output
        System.out.println("=== RESTAURANT SETUP ===");
        System.out.println(restaurant);
        System.out.println("\n=== TABLE STATUS ===");
        System.out.println(table3);
        System.out.println("\n=== ORDER STATUS ===");
        System.out.println(order1);
        System.out.println("\n=== GENERATED RECEIPT ===");
        System.out.println(receipt1);
    }
}