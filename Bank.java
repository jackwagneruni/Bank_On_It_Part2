import java.io.*;
import java.util.Scanner;

public class Bank implements HasMenu {
    private Admin admin;
    private CustomerList customers;
    
    public Bank() {
        admin = new Admin();
        customers = new CustomerList();
        
        // Uncomment the next two lines to refresh data
        //this.loadSampleCustomers();
        //this.saveCustomers();
        
        this.loadCustomers();
        this.start();
        this.saveCustomers();
    }
    
    public void loadSampleCustomers() {
        System.out.println("Loading sample customers...");
        customers.add(new Customer("Alice", "1111", 1000.0, 1000.0));
        customers.add(new Customer("Bob", "2222", 0.0, 0.0));
        customers.add(new Customer("Cindy", "3333", 0.0, 0.0));
        System.out.println("Sample customers loaded.");
    }
    
    public void saveCustomers() {
        try {
            FileOutputStream fileOut = new FileOutputStream("customers.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(customers);
            out.close();
            fileOut.close();
            System.out.println("Customers saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }
    
    public void loadCustomers() {
        try {
            FileInputStream fileIn = new FileInputStream("customers.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            customers = (CustomerList) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Customers loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved customers found. Loading sample customers...");
            loadSampleCustomers();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading customers: " + e.getMessage());
            System.out.println("Loading sample customers instead...");
            loadSampleCustomers();
        }
    }
    
    public void fullCustomerReport() {
        System.out.println("Full customer report");
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer.getReport());
            }
        }
    }
    
    public void addUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add User");
        
        System.out.print("Name: ");
        String userName = scanner.nextLine();
        
        System.out.print("PIN: ");
        String pin = scanner.nextLine();
        
        customers.add(new Customer(userName, pin));
        System.out.println("User added successfully.");
    }
    
    public void applyInterest() {
        System.out.println("Apply interest");
        for (Customer customer : customers) {
            SavingsAccount savings = customer.getSavings();
            savings.calcInterest();
            System.out.println("New balance: " + savings.getBalanceString());
        }
    }
    
    public void loginAsCustomer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Customer login");
        
        System.out.print("User name: ");
        String userName = scanner.nextLine();
        
        System.out.print("PIN: ");
        String pin = scanner.nextLine();
        
        Customer currentCustomer = null;
        
        for (Customer customer : customers) {
            if (customer.login(userName, pin)) {
                currentCustomer = customer;
                break;
            }
        }
        
        if (currentCustomer != null) {
            System.out.println("Login successful.");
            currentCustomer.start();
        } else {
            System.out.println("Invalid username or PIN.");
        }
    }
    
    public void startAdmin() {
        if (admin.login()) {
            System.out.println("Admin login successful.");
            boolean running = true;
            
            while (running) {
                System.out.print(admin.menu());
                Scanner scanner = new Scanner(System.in);
                int choice;
                
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }
                
                if (choice == 0) {
                    running = false;
                } else if (choice == 1) {
                    fullCustomerReport();
                } else if (choice == 2) {
                    addUser();
                } else if (choice == 3) {
                    applyInterest();
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            System.out.println("Admin login failed.");
        }
    }
    
    @Override
    public String menu() {
        return "Bank Menu\n\n" +
               "0) Exit system\n" +
               "1) Login as admin\n" +
               "2) Login as customer\n\n" +
               "Action: ";
    }
    
    @Override
    public void start() {
        boolean running = true;
        
        while (running) {
            System.out.print(menu());
            Scanner scanner = new Scanner(System.in);
            int choice;
            
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            
            if (choice == 0) {
                running = false;
                System.out.println("Exiting system. Goodbye!");
            } else if (choice == 1) {
                startAdmin();
            } else if (choice == 2) {
                loginAsCustomer();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    public static void main(String[] args) {
        new Bank();
    }
}