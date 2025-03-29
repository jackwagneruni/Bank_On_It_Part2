import java.util.Scanner;
import java.io.Serializable;
import java.text.NumberFormat;

public class Customer extends User implements Serializable {
    private CheckingAccount checking;
    private SavingsAccount savings;
    private transient NumberFormat currencyFormatter;

    public Customer() {
        checking = new CheckingAccount();
        savings = new SavingsAccount(0, 0.05);
        currencyFormatter = NumberFormat.getCurrencyInstance();
    }

    public Customer(String userName, String PIN) {
        this();
        setUserName(userName);
        setPIN(PIN);
    }

    public Customer(String userName, String PIN, double checkingBalance, double savingsBalance) {
        setUserName(userName);
        setPIN(PIN);
        checking = new CheckingAccount(checkingBalance);
        savings = new SavingsAccount(savingsBalance, 0.05);
        currencyFormatter = NumberFormat.getCurrencyInstance();
    }

    @Override
    public String menu() {
        return "Customer Menu\n" +
               "0) Exit\n" +
               "1) Manage Checking Account\n" +
               "2) Manage Savings Account\n" +
               "3) Change PIN\n" +
               "Action: ";
    }

    @Override
    public void start() {
        boolean running = true;
        while (running) {
            System.out.print(menu());
            int choice = getChoice();
    
            if (choice == 0) {
                running = false;
            } else if (choice == 1) {
                checking.start();
            } else if (choice == 2) {
                savings.start();
            } else if (choice == 3) {
                changePin();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private int getChoice() {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        choice = Integer.parseInt(scanner.nextLine());
        return choice;
    }

    private void changePin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter current PIN: ");
        String currentPin = scanner.nextLine();
        
        if (currentPin.equals(getPIN())) {
            System.out.print("Enter new PIN: ");
            String newPin = scanner.nextLine();
            System.out.print("Confirm new PIN: ");
            String confirmPin = scanner.nextLine();
            
            if (newPin.equals(confirmPin)) {
                setPIN(newPin);
                System.out.println("PIN changed successfully.");
            } else {
                System.out.println("PINs do not match. PIN not changed.");
            }
        } else {
            System.out.println("Incorrect current PIN. PIN not changed.");
        }
    }

    public CheckingAccount getChecking() {
        return checking;
    }

    public SavingsAccount getSavings() {
        return savings;
    }
    

    private NumberFormat getCurrencyFormatter() {
        if (currencyFormatter == null) {
            currencyFormatter = NumberFormat.getCurrencyInstance();
        }
        return currencyFormatter;
    }

    @Override
    public String getReport() {
        return "User: " + getUserName() + 
               ", Checking: " + checking.getBalanceString() + 
               ", Savings: " + savings.getBalanceString();
    }
    
    public static void main(String[] args) {
        Customer customer = new Customer("Alice", "1111");
        if (customer.login()) {
            System.out.println("Login Successful");
            customer.start();
        } else {
            System.out.println("Login Failed");
        }
    }
}