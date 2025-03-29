all: Bank.class

Bank.class: Bank.java Admin.class CustomerList.class
	javac -g Bank.java

Admin.class: Admin.java User.class
	javac -g Admin.java

CustomerList.class: CustomerList.java Customer.class
	javac -g CustomerList.java

Customer.class: Customer.java User.class CheckingAccount.class SavingsAccount.class
	javac -g Customer.java

User.class: User.java HasMenu.class
	javac -g User.java

CheckingAccount.class: CheckingAccount.java HasMenu.class
	javac -g CheckingAccount.java

SavingsAccount.class: SavingsAccount.java CheckingAccount.class
	javac -g SavingsAccount.java

HasMenu.class: HasMenu.java
	javac -g HasMenu.java

testBank: Bank.class
	java Bank

testAdmin: Admin.class
	java Admin

testCustomer: Customer.class
	java Customer

testChecking: CheckingAccount.class
	java CheckingAccount

testSavings: SavingsAccount.class
	java SavingsAccount

clean:
	rm -f *.class *.ser