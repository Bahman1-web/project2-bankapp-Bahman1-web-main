package edu.brooklyn.project2;
import java.io.*;
import java.util.*;
 public class Bank {
    private ArrayList<CheckingAccount> accounts;

    public Bank(){
        this.accounts = new ArrayList<>();
    }
    public void addAcc(CheckingAccount acct) {
        accounts.add(acct);
        writeAccts();
        return;
    }

    public boolean checkSsn(String ssn){
        for(int i = 0; i < accounts.size();i++){
            CheckingAccount acct = accounts.get(i);
            if (ssn.equals(acct.getSsn())){
                return true;
            }
        }
        return false;
    }
    public boolean comparePwd(String pwd, String ssn){
        for(int i = 0; i < accounts.size(); i++){
            CheckingAccount acct = accounts.get(i);
            if (ssn.equals(acct.getSsn()) && pwd.equals(acct.getPwd())) {
                return true;    
            }

        }
        return false;
    }
    public void checkBalance(String ssn){
            double balance = 0;
            for(int i = 0; i < accounts.size();i++){
                CheckingAccount acct = accounts.get(i);
                if (ssn.equals(acct.getSsn())){
                    balance = acct.getBalance();
                }        
            }
            System.out.printf("$%,10.2f", balance);
    }

    public void withDraw(String ssn, Scanner scnr){
        System.out.print("Enter amount to withdraw: ");
        double amount = scnr.nextInt();
        System.out.printf("%,10.2f", amount);
        CheckingAccount acct;
        for(int i= 0; i < accounts.size();i++){
            acct = accounts.get(i);
            if (acct.getSsn().equals(ssn)){
                if(amount > acct.getBalance()){
                    System.out.println("Insufficient funds. ");
                }
                else if (amount <= 0) {
                    System.out.println("Amount must be positive. ");
                }
                else if (amount < 0) { // make it so it won't contain fractinal cents
                    System.out.println("Amount must not contain fractional cents. ");
                }
                acct.setBalance(acct.getBalance() - amount);
                writeAccts();
            }
        }





    }

    public void deposit(String ssn, Scanner scnr){ // Make a change to count fractional numbers
        System.out.print("Enter dollar amount to deposit: ");
        double amount = scnr.nextInt();
        System.out.printf("%,10.2f", amount);//use BigDecimal to check for less than cents
        if (amount<=0){
            System.out.println("Amount must be positive ");
        }
        else {
             for(int i = 0; i < accounts.size();i++){
                CheckingAccount acct = accounts.get(i);
                if (ssn.equals(acct.getSsn())){
                    acct.setBalance(amount+acct.getBalance());
                    writeAccts();

            
        }
    }
}
    }
    public void writeAccts(){
        try{
            PrintWriter writer = new PrintWriter("accounts1.txt");
            for (int i =0; i< accounts.size(); i++) {
                CheckingAccount account = accounts.get(i);
                writer.println(account.toString());
            }
            writer.flush();
            writer.close();
        }
        catch(IOException e) {
            System.out.println("No such file found.");
            
        }
        } 
        
    }


