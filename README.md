Overview

This week we are finishing our banking app.  By now, you should have a number of classes finished:

    HasMenu interface
    CheckingAccount
    SavingsAccount
    User
    Customer

You really have to build two new classes to put the whole thing together.  The Admin class is quite easy, as it extends User and has limited new capabilities.  The Bank class is the more interesting class, as it puts everything together and manages file access.
Full UML

Here's the full UML for the entire system:

BankB.svg

## Banking App - UML Diagram

### HasMenu (interface)
+ menu(): String
+ start(): void

### CheckingAccount
- balance: double
+ CheckingAccount(balance)
+ main(): void 
+ menu(): String
+ start(): void
+ getBalance(): double
+ getBalanceString(): String
+ setBalance(balance): void
+ checkBalance(): void
+ getDouble(): double
+ makeDeposit(): void
+ makeWithdraw(): void

### SavingsAccount
- interestRate: double
+ main()
+ calcInterest(): void
+ setInterestRate(): void
+ getInterestRate(): void

### User (abstract)
- username: String
- PIN: String
+ login(): Boolean
+ login(username, PIN): boolean
+ setUserName(userName): void
+ getUserName(): String
+ setPIN(PIN): void
+ getPIN(): String
+ getReport(): String (abstract)

### Admin
+ Admin()
+ menu(): String
+ getReport(): String

### Customer
+ Customer()
+ menu(): String
+ getReport(): String
+ start(): void

### Bank
- admin: Admin
- customers: ArrayList
+ Bank()
+ main(): void
+ loadSampleCustomers(): void
+ loadCustomers(): void
+ saveCustomers(): void
+ fullCustomerReport(): void
+ addUser(): void
+ applyInterest(): void
+ loginAsCustomer(): void
+ menu(): String
+ start(): void
+ startAdmin(): void

### Relationships
+ CheckingAccount → HasMenu (implements)
+ User → HasMenu (implements)
+ SavingsAccount → CheckingAccount (extends)
+ Admin → User (extends)
+ Customer → User (extends)
+ Bank → Admin (composition, 1)
+ Bank → Customer (composition, n)


Details:





Admittedly, there's a lot going on here, but it's actually not too different from what you've already got.  The main focus here is integration. That is, how the Bank class holds the whole thing together.  The other main idea is persistence.  We'll use object serialization to save and load customer data, so we'll need to make a few changes to make that happen.
Integration

The Bank class is the actual application.  It has a single Admin instance, and an ArrayList of customers.  It will have its own menu system, that allows you to log in as an admin or a customer.  That menu should look like this:

Bank Menu

0) Exit system
1) Login as admin
2) Login as customer

Action:

If the user chooses to log in as the admin, we'll invoke the Admin's login function (inherited from User).  If the login is successful, we'll show the admin menu:

Admin login
User name: admin
PIN: 0000

Admin Menu

0) Exit this menu
1) Full customer report
2) Add user
3) Apply interest to savings accounts

Action:

As you would expect, the admin menu is defined in the admin class.  However, there is a bit of complexity here.  The Admin is a User, but most of the admin's actions are really about the bank, specifically about the customer list. The admin class doesn't have access to the customer list as we're currently set up, so there are two options:

    Modify Admin so it accepts the customer list as a parameter
    Perform all the admin functions in the Bank class (that seems more natural to me.)

So the Amin class will still have a menu() method containing the Admin menu options.  It will also have a start() method, but we'll keep that empty.  Instead, we'll add a startAdmin() method to Bank, so bank will manage admin duties.  

Here's a sample run of the admin menu:

Admin Menu

0) Exit this menu
1) Full customer report
2) Add user
3) Apply interest to savings accounts

Action: 1
Full customer report
User: Alice, Checking: $1000.00, Savings: $1000.00
User: Bob, Checking: $0.00, Savings: $0.00
User: Cindy, Checking: $0.00, Savings: $0.00

Admin Menu

0) Exit this menu
1) Full customer report
2) Add user
3) Apply interest to savings accounts

Action: 2
Add User
Name: Dave
PIN: 4444

Admin Menu

0) Exit this menu
1) Full customer report
2) Add user
3) Apply interest to savings accounts

Action: 3
Apply interest
New balance: $1050.00
New balance: $0.00
New balance: $0.00
New balance: $0.00

Admin Menu

0) Exit this menu
1) Full customer report
2) Add user
3) Apply interest to savings accounts

Action: 1
Full customer report
User: Alice, Checking: $1000.00, Savings: $1050.00
User: Bob, Checking: $0.00, Savings: $0.00
User: Cindy, Checking: $0.00, Savings: $0.00
User: Dave, Checking: $0.00, Savings: $0.00

Admin Menu

0) Exit this menu
1) Full customer report
2) Add user
3) Apply interest to savings accounts

Action:

Note that Alice starts with $1000 in each account

We'll create a new customer called Dave, and we'll apply interest.  After that, a customer report shows that Dave has been created and Alice has earned the appropriate amount of interest in her savings account.
Sample Data

While you are testing the application, it's a great idea to have some sample data that you can work with.  The sample data should be representative of the data you will be working with, but you should never test on actual customer data until you are absolutely certain the system works as advertised. 

I recommend adding a loadSampleCustomers() method to your Bank class so whenever it starts up, it begins with some sample data.  
Persistance

Of course, this project is really a database, and one of the key elements of a database is persistence across runs of the program.  There are a number of ways to achieve this. We will use Object Serialization, because it's very powerful and pretty easy to implement.  If we organize our code well, we will only need to load and store one object, which will be the ArrayList of Customers.  This is not a difficult process, but it requires some preparation.

    The ArrayList must be serializable (It will be, if it contains only serializable objects)
    It's better to store the ArrayList in a simple container class to prevent a serialization warning
    Since the ArrayList is a list of Customers, the Customer class must be serializable
    Since the Customer class is based on User, the User class must be serializable
    Since the Customer class contains CheckingAccount and SavingsAccount classes, they must be serializable

Any class can be serializable if you adhere to these steps:

    import java.io.*
    implement serializable
    ensure all ancestor classes are serializable (if we wish to store something defined in those classes)
    ensure all data members are serializable
    this means you can't have a Scanner as an instance variable.  Just create scanners in method calls when you need them.

You might need to modify some of your classes to make this work.  By my calculations, the following classes should be serializable in this system:

    Customer
    User (contains userName and PIN, which need to be saved)
    CheckingAccount
    SavingsAccount (will probably already be serializable because it inherits from CheckingAccount) 

Serialization tips

    If you are running into serialization errors, try to ascertain that all the appropriate classes are serializable
    As we described in class, you can serialize an ArrayList of serializable objects, but you will get a warning when you try to de-serialize.
    The easiest way to handle this is to create a 'dummy class' containing only the ArrayList

    class CustomerList extends ArrayList<Customer>{}

    If you change one of the classes, you will run into versioning errors.  
    The best way to handle this is to refresh your data once in a while.
    My bank constructor looks like this:

      public Bank(){
        // uncomment the next two lines to refresh data

        //this.loadSampleCustomers();
        //this.saveCustomers();
        this.loadCustomers();
        this.start();
        this.saveCustomers();
      } // end constructor

    If the class definitions have changed, just uncomment the first two lines for one run, and you'll reset the data to the current format. 
    You may have to do this every time you modify one of the serializable classes.

Milestones

There is a lot to manage here, so you'll want to think about the most sensible plan of attack. I would break down the tasks in this way:

Task One: Admin

    build the Admin class
    It is arguably one of the easiest classes in the system
    It will extend User, so the login functionality is already written
    The constructor should set default username and pin values
    For ease of testing, please use "admin" as the userName and "0000" as the PIN
    Create the admin menu, which should have options to exit, make a customer report, add a user, and apply interest rates
    You need to have a start() method, but leave it blank (!!!) we'll actually do all the admin actions in the bank class.
    You need a getReport() method, but we don't really use it. Maybe have it report the admin name and PIN.

Task Two: Bank Essentials

    get the basic version of the bank up and running
    It should implement HasMenu, so it will need start() and menu() methods
    The bank has two instance variables
        a single instance of Admin
        an ArrayList of Customers (better, a class that extends an ArrayList of Customers)
    Create a menu() method, which is the main menu of the Bank
    Create a start() method, which handles the Bank main menu inputs
    For now, print outputs indicating you got to each of the appropriate places

Task Three: implement admin menus and customer login

    Once that is tested, start implementing details
    The admin menu is most easily handled in the Bank class
    Add an adminStart() method to the Bank class
        It will call the admin menu and handle the resulting tasks
        Since all admin tasks are actually bank tasks, it's easier to manage them in the bank.
        At first, just use print statements to confirm user's intentions
        When you are ready to test, have the bank main menu try to login the admin, and if the login is successful, run the adminStart() method
    Add the ability to login as a customer
        This should be a method in the Bank class
        Ask for a userName and PIN
        Set an instance of Customer (currentCustomer, perhaps) to null
        Go through every customer in the customerList
            if you can log in to that customer
                set currentCustomer to that customer
                activate the start() method of that customer
        If you get through the entire list without a successful login, inform the user
    Add admin methods to Bank class
        The admin menu will need a few methods. None of them are difficult, but you'll need them:
        fullCustomerReport()
            step through each customer in the list
            print the getReport() value from that customer
        addUser()
            Ask user for a userName and a PIN
            Create customer with that information
            add that customer to the end of the customer list
        applyInterest()
            Go through each customer in the list
            apply the calcInterest() method of the savings account for that customer

Task four: implement serialization

    Create a saveCustomers() method in Bank
    Create a loadCustomers() method in Bank
    Modify the Bank constructor to save and load the data
        On your first pass, create and save the default data for testing purposes
        After initial save is tested, comment out these lines, but you may want to keep them around for retesting
    Test everything to make sure it's all working

