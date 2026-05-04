import java.io.*;
package edu.brooklyn.project2;
import java.util.Scanner;
public class BankApp {
  public static void displayMenu(){
    System.out.println("1. Log into bank account");
    System.out.println("2. Create bank account");
    System.out.println("0. Exit");
    System.out.println("Enter option: ");
  }
 
  public static void main(String[] args) {
    Scanner scnr = new Scanner(System.in);
    File pfile = new File("accounts.txt");
    Scanner fileSc = new Scanner(pfile); 
    Bank bank = new Bank();
    readAcctFile(fileSc,bank);
    int option = -1;
    while(option != 0 ){
      displayMenu();
      option = scnr.nextInt();
      if (option == 1){
        login(scnr,bank);
      }
      else if (option == 2){
        
      }
      else if(option == 0){
        System.out.println("Exit");
        return;
      }
      else{
        System.out.println("Unknown option.");
    }
    }
  }
       
    public static void login(Scanner scnr,Bank bank){
        System.out.print("SSN: ");
        String ssn = scnr.next();
        System.out.println(ssn);
        System.out.print("Password: ");
        String password = PasswordUtil.getPasswordHash(PasswordUtils.readPassword(scnr));
        for(int i = 0; i < 3; i++){
            if(bank.compPwd(password,ssn)){
                break;
            }
            System.out.println("Login incorrect ");
            if(i>2){
              System.out.println("3 incorrect attempts.");
              return;

            }
            password = PasswordUtil.getPasswordHash(PasswordUtils.readPassword(scnr));
          }
          int op = -1;
          while(op != 0){
          accountMenu();
          option = scnr.nextInt();
          if(option == 1){
            bank.checkBalance(ssn);
          }
          else if (option == 2){
            bank.withdraw(ssn);
          }
          else if (option == 3){
          bank.deposit(ssn);
          }
          else if (option == 0){
            System.out.println("Exit");
            return;
          }
          else{
            System.out.println("Unknown option:");
          }
          

          
      }
    
  

  public static void createAc(Scanner scnr,Bank bank){
    System.out.print("SSN: ");
    String ssn;
    while(ssn.length() > 9 && ssn.length() < 9){
        ssn = scnr.next();
        if (!bank.checkSsn(ssn) && ssn.length() == 9){
            break;
        }

        //if else statement for length check and existing ssn check   
    } 
    System.out.println(ssn);
    System.out.print("Password: ");
    String pwd;
    while(pwd.length() < 1){
        pwd = PasswordUtil.getPasswordHash(PasswordUtils.readPassword(scnr));
     // Check for length
    }
    System.out.println();
    System.out.print("Balance: ");
    double balance;
    while(balance <= 0) {
        balance= scnr.nextDouble(); 
        // Check for negative or 0 balance

    }

    System.out.println(balance);
    CheckingAccount cA = new CheckingAccount(balance,ssn,pwd);
    bank.addAc(cA);
      }
      public static void accountMenu(){
      System.out.println("1. Check balance");
      System.out.println("2. Withdraw");
      System.out.println("0. Deposit");
      System.out.println("Exit");
      System.out.println("Enter option: ");
      }
      public static void readAcctFile(Scanner scnr,Bank bank){
      while(sncr.hasNextLine()){
        String s = scnr.nextLine();
        String[] tokens = s.split();
        String ssn = tokens[0];
        String pwd = tokens[1];
        double balance = Double.parseDouble(tokens[2]);
        bank.addAcc(new CheckingAccount(ssn, pwd, balance));
      }
      System.out.println("Accounts read in");

  }
    
    


