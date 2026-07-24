public class Main {
    public static void main(String[] args) {
        // 1. Try loading saved data from file
        Restaurant restaurant = Restaurant.loadData();

        // 2. If no save file exists, set up brand new default data
        if (restaurant == null) {
            System.out.println("Initializing new Checkmate Bistro data setup...");
            restaurant = new Restaurant("Checkmate Bistro");

            // Add 10 tables
            for (int i = 1; i <= 10; i++) {
                restaurant.addTable(new Table(i));
            }

            // Add default menu catalog items
            restaurant.addMenuItem(new MenuItem(101, "Classic Burger", 12.99));
            restaurant.addMenuItem(new MenuItem(102, "Crispy Fries", 4.50));
            restaurant.addMenuItem(new MenuItem(103, "Fountain Soda", 2.50));
            restaurant.addMenuItem(new MenuItem(104, "Ribeye Steak", 26.99));
            restaurant.addMenuItem(new MenuItem(105, "Caesar Salad", 8.99));

            // Initial save
            restaurant.saveData();
        }

        // 3. Launch the Console UI
        ConsoleUI ui = new ConsoleUI(restaurant);
        ui.start();
    }
}