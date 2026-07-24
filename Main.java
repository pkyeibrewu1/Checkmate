public class Main {
    public static void main(String[] args) {
        // Initialize Restaurant
        Restaurant restaurant = new Restaurant("Checkmate Bistro");

        // Populate Default Tables (1 to 10)
        for (int i = 1; i <= 10; i++) {
            restaurant.addTable(new Table(i));
        }

        // Populate Default Menu Catalog
        restaurant.addMenuItem(new MenuItem(101, "Classic Burger", 12.99));
        restaurant.addMenuItem(new MenuItem(102, "Crispy Fries", 4.50));
        restaurant.addMenuItem(new MenuItem(103, "Fountain Soda", 2.50));
        restaurant.addMenuItem(new MenuItem(104, "Ribeye Steak", 26.99));
        restaurant.addMenuItem(new MenuItem(105, "Caesar Salad", 8.99));

        // Launch Interactive Console UI
        ConsoleUI ui = new ConsoleUI(restaurant);
        ui.start();
    }
}