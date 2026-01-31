// ✅ WITH SINGLETON - One connection, consistent config

class DBConnection {
    private static DBConnection instance;
    private static int count = 0;

    private DBConnection() {  // Private!
        count++;
        System.out.println("Connection #" + count + " created -> localhost:5432");
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public static int getCount() { return count; }
}

public class WithSingleton {
    public static void main(String[] args) {
        System.out.println("=== WITH SINGLETON ===\n");

        // All calls return the SAME instance
        DBConnection db1 = DBConnection.getInstance();
        DBConnection db2 = DBConnection.getInstance();

        System.out.println("\n>> Total connections: " + DBConnection.getCount());
        System.out.println(">> db1 == db2 ? " + (db1 == db2));  // true!
    }
}
