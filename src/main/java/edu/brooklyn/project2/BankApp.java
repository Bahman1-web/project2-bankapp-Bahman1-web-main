package edu.brooklyn.project2;
import java.io.*;
import java.util.Scanner;
public class BankApp {
  public static void displayMenu() {
    System.out.println("1. Log into bank account");
    System.out.println("2. Create bank account");
    System.out.println("0. Exit");
    System.out.println("Enter option: ");
  }

  public static void main(String[] args) throws IOException {
    Scanner scnr = new Scanner(System.in);
    File pfile = new File("accounts.txt");
    Scanner fileSc = new Scanner(pfile);
    Bank bank = new Bank();
    readAcctFile(fileSc, bank);
    int option = -1;
    while (option != 0) {
      displayMenu();
      option = scnr.nextInt();
      if (option == 1) {
        login(scnr, bank);
      } else if (option == 2) {
        createAc(scnr,bank);
      } else if (option == 0) {
        System.out.println("Exit");
        return;
      } else {
        System.out.println("Unknown option.");
      }
    }
  }

  public static void login(Scanner scnr, Bank bank) {
    System.out.print("SSN: ");
    String ssn = scnr.next();
    System.out.println(ssn);
    System.out.print("Password: ");
    String password = PasswordUtils.getPasswordHash(PasswordUtils.readPassword(scnr));
    for (int i = 0; i < 3; i++) {
      if (bank.comparePwd(password, ssn)) {
        break;
      }
      System.out.println("Login incorrect ");
      if (i > 2) {
        System.out.println("3 incorrect attempts.");
        return;

      }
      password = PasswordUtils.getPasswordHash(PasswordUtils.readPassword(scnr));
    }
    int op = -1;
    while (op != 0) {
      accountMenu();
      int option = scnr.nextInt();
      if (option == 1) {
        bank.checkBalance(ssn);
      } else if (option == 2) {
        bank.withDraw(ssn,scnr);
      } else if (option == 3) {
        bank.deposit(ssn,scnr);
      } else if (option == 0) {
        System.out.println("Exit");
        return;
      } else {
        System.out.println("Unknown option:");
      }


    }


  }

  public static void createAc(Scanner scnr, Bank bank) {
    System.out.print("SSN: ");
    String ssn = scnr.next();
    while (ssn.length() > 9 && ssn.length() < 9) {
      if (!bank.checkSsn(ssn) && ssn.length() == 9) {
        break;
      }
      ssn = scnr.next();
      //if else statement for length check and existing ssn check
    }
    System.out.println(ssn);
    System.out.print("Password: ");
    String pwd = PasswordUtils.getPasswordHash(PasswordUtils.readPassword(new Scanner(System.in)));
    System.out.println(pwd);
    System.out.println();
    System.out.print("Balance: ");
    double  balance = scnr.nextDouble();
    while (balance <= 0) {
      balance = scnr.nextDouble();
      // Check for negative or 0 balance

    }
    System.out.println(balance);
    CheckingAccount cA = new CheckingAccount(balance, ssn, pwd);
    bank.addAcc(cA);
  }

  public static void accountMenu() {
    System.out.println("1. Check balance");
    System.out.println("2. Withdraw");
    System.out.println("0. Deposit");
    System.out.println("Exit");
    System.out.println("Enter option: ");
  }


  public static void readAcctFile(Scanner scnr, Bank bank) {
    while (scnr.hasNextLine()) {
      String s = scnr.nextLine();
      String[] tokens = s.split(" ");
      String ssn = tokens[0];
      String pwd = tokens[1];
      double balance = Double.parseDouble(tokens[2]);
      bank.addAcc(new CheckingAccount(balance, ssn, pwd));
    }
    System.out.println("Accounts read in");

  }
}
    
    


