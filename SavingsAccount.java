import java.util.Scanner;
import java.io.Serializable;

public class SavingsAccount extends CheckingAccount implements Serializable {
    private double interestRate;

    public SavingsAccount(double balance, double interestRate) {
        super(balance);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        if (interestRate >= 0) {
            this.interestRate = interestRate;
        } else {
            System.out.println("Interest rate cannot be negative.");
        }
    }

    public void calcInterest() {
        double interest = getBalance() * interestRate;
        setBalance(getBalance() + interest);
    }

    @Override
    public String menu() {
        return "Savings Account Menu\n" +
               "0) Exit\n" +
               "1) Check Balance\n" +
               "2) Make a Deposit\n" +
               "3) Make a Withdrawal\n" +
               "4) Calculate Interest\n" +
               "5) Set Interest Rate\n" +
               "Action: ";
    }

    @Override
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
            } else if (choice == 4) {
                calcInterest();
                checkBalance();
            } else if (choice == 5) {
                System.out.print("Enter new interest rate (as decimal, e.g., 0.05 for 5%): ");
                double rate = getDouble();
                setInterestRate(rate);
                System.out.println("Interest rate set to: " + (rate * 100) + "%");
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        SavingsAccount savingsAccount = new SavingsAccount(1000.00, 0.05);
        savingsAccount.start();
    }
}