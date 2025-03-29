import java.io.Serializable;
import java.util.Scanner;

public class CheckingAccount implements HasMenu, Serializable {
    private double balance;

    public CheckingAccount() {
        this.balance = 0.0;
    }

    public CheckingAccount(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public String getBalanceString() {
        return "$" + String.format("%.2f", balance);
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    public void checkBalance() {
        System.out.println("Current Balance: " + getBalanceString());
    }

    protected double getDouble() {
        Scanner scanner = new Scanner(System.in);
        double amount = 0.0;
        amount = Double.parseDouble(scanner.nextLine());
        return amount;
    }

    public void makeDeposit() {
        System.out.print("How much to deposit? ");
        double amount = getDouble();
        
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + String.format("%.2f", amount));
            System.out.println("New Balance: " + getBalanceString());
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void makeWithdraw() {
        System.out.print("How much to withdraw? ");
        double amount = getDouble();
        
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: $" + String.format("%.2f", amount));
            System.out.println("New Balance: " + getBalanceString());
        } else {
            System.out.println("Invalid withdrawal amount.");
        }
    }

    public String menu() {
        return "Checking Account Menu\n" +
               "0) Exit\n" +
               "1) Check Balance\n" +
               "2) Make a Deposit\n" +
               "3) Make a Withdrawal\n" +
               "Action: ";
    }

    public void start() {
        boolean exit = false;
        while (!exit) {
            System.out.print(menu());
            Scanner scanner = new Scanner(System.in);
            int choice;
            
            choice = scanner.nextInt();

            if (choice == 0) {
                exit = true;
            } else if (choice == 1) {
                checkBalance();
            } else if (choice == 2) {
                makeDeposit();
            } else if (choice == 3) {
                makeWithdraw();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        CheckingAccount checkingAccount = new CheckingAccount(100.00);
        checkingAccount.start();
    }
}