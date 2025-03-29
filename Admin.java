public class Admin extends User {
    
    public Admin() {
        setUserName("admin");
        setPIN("0000");
    }
    
    @Override
    public String menu() {
        return "Admin Menu\n\n" +
               "0) Exit this menu\n" +
               "1) Full customer report\n" +
               "2) Add user\n" +
               "3) Apply interest to savings accounts\n\n" +
               "Action: ";
    }
    
    @Override
    public void start() {
        // This is intentionally left blank per the assignment
        // The Bank class will handle admin operations through startAdmin()
    }
    
    @Override
    public String getReport() {
        return "Admin: " + getUserName();
    }
    
    public static void main(String[] args) {
        Admin admin = new Admin();
        if (admin.login()) {
            System.out.println("Admin login successful!");
            // The actual operations are handled by the Bank class
        } else {
            System.out.println("Admin login failed!");
        }
    }
}