import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private String name;
    private List<Table> tables;
    private List<MenuItem> menu;

    public Restaurant(String name) {
        this.name = name;
        this.tables = new ArrayList<>();
        this.menu = new ArrayList<>();
    }

    public void addTable(Table table) { tables.add(table); }
    public void addMenuItem(MenuItem item) { menu.add(item); }

    public String getName() { return name; }
    public List<Table> getTables() { return tables; }
    public List<MenuItem> getMenu() { return menu; }

    @Override
    public String toString() {
        return "Restaurant: " + name + " | Total Tables: " + tables.size() + " | Menu Items: " + menu.size();
    }
}