import java.io.*;
import java.util.Scanner;

public class Bank implements HasMenu {
    private Admin admin;
    private CustomerList customers;
    
    public Bank() throws IOException, ClassNotFoundException {
        admin = new Admin();
        customers = new CustomerList();
        
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
    
    public void saveCustomers() throws IOException {
        File file = new File("customers.ser");
        System.out.println("Saving customers to: " + file.getAbsolutePath());
        
        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(customers);
        out.close();
        fileOut.close();
        System.out.println("Customers saved successfully.");
        
        if(file.exists() && !file.isDirectory()) { 
            System.out.println("Verified: File exists at " + file.getAbsolutePath() + 
                              " with size " + file.length() + " bytes");
        }
    }
    
    public void loadCustomers() throws IOException, ClassNotFoundException {
        File file = new File("customers.ser");
        System.out.println("Attempting to load customers from: " + file.getAbsolutePath());
        
        if(!file.exists()) {
            System.out.println("File does not exist. Loading sample customers...");
            loadSampleCustomers();
            return;
        }
        
        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        customers = (CustomerList) in.readObject();
        in.close();
        fileIn.close();
        System.out.println("Customers loaded successfully.");
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
    
    public void addUser() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add User");
        
        System.out.print("Name: ");
        String userName = scanner.nextLine();
        
        System.out.print("PIN: ");
        String pin = scanner.nextLine();
        
        customers.add(new Customer(userName, pin));
        System.out.println("User added successfully.");
        
        saveCustomers();
    }
    
    public void applyInterest() throws IOException {
        System.out.println("Apply interest");
        for (Customer customer : customers) {
            SavingsAccount savings = customer.getSavings();
            savings.calcInterest();
            System.out.println("New balance: " + savings.getBalanceString());
        }
        
        saveCustomers();
    }
    
    public void loginAsCustomer() throws IOException {
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
            saveCustomers();
        } else {
            System.out.println("Invalid username or PIN.");
        }
    }
    
    public void startAdmin() throws IOException {
        if (admin.login()) {
            System.out.println("Admin login successful.");
            boolean running = true;
            
            while (running) {
                System.out.print(admin.menu());
                Scanner scanner = new Scanner(System.in);
                int choice;
                
                choice = Integer.parseInt(scanner.nextLine());
                
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
            
            choice = Integer.parseInt(scanner.nextLine());
            
            if (choice == 0) {
                running = false;
                System.out.println("Exiting system. Goodbye!");
            } else if (choice == 1) {
                try {
                    startAdmin();
                } catch (IOException e) {
                    System.exit(1);
                }
            } else if (choice == 2) {
                try {
                    loginAsCustomer();
                } catch (IOException e) {
                    System.exit(1);
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    public String formatCurrency(double amount) {
        return "$" + String.format("%.2f", amount);
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        new Bank();
    }
}