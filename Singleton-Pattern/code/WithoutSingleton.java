// WITHOUT SINGLETON - Multiple connections, inconsistent configs

class DBConnection {
    private static int count = 0;

    public DBConnection(String host, int port) {
        count++;
        System.out.println("Connection #" + count + " created -> " + host + ":" + port);
    }

    public static int getCount() { return count; }
}

public class WithoutSingleton {
    public static void main(String[] args) {
        System.out.println("=== WITHOUT SINGLETON ===\n");

        // Problem 1: Multiple connections created
        DBConnection db1 = new DBConnection("localhost", 5432); //user service
        DBConnection db2 = new DBConnection("localhost", 5432); //order service
        // DBConnection db3 = new DBConnection("localhost", 5432); //payment service

        // Problem 2: Someone uses wrong config!
        // DBConnection db4 = new DBConnection("prod-server", 3306);  // Oops!

        System.out.println("\n>> Total connections: " + DBConnection.getCount());
        System.out.println(">> db1 == db2 ? " + (db1 == db2));  // false!
    }
}
