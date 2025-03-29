import java.io.Serializable;
import java.util.Scanner;

public abstract class User implements HasMenu, Serializable {
    protected String userName;
    protected String PIN;

    // Login method that prompts for username and PIN
    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String userName = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String PIN = scanner.nextLine();
        return login(userName, PIN);
    }

    // Login method that checks the provided username and PIN against the stored values
    public boolean login(String userName, String PIN) {
        return this.userName.equals(userName) && this.PIN.equals(PIN);
    }

    // Setters and getters for userName and PIN
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getPIN() {
        return PIN;
    }

    // Abstract method for generating a report (to be implemented by subclasses)
    public abstract String getReport();

    // Menu method, can be customized by subclasses
    @Override
    public String menu() {
        return "0) Quit\n1) Login\n2) View report";
    }

    // Start method, calls menu and handles user interactions
    @Override
    public void start() {
        int choice = -1;
        Scanner scanner = new Scanner(System.in);
        while (choice != 0) {
            System.out.println(menu());
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1) {
                    if (login()) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Invalid username or PIN.");
                    }
                } else if (choice == 2) {
                    System.out.println(getReport());
                } else if (choice == 0) {
                    System.out.println("Exiting...");
                } else {
                    System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
            }
        }
    }
}