package edu.brooklyn.project2;
public class CheckingAccount {
    private double balance;
    private String ssn;
    private String pwd;

    public CheckingAccount(double balance, String ssn, String pwd){
        this.balance = balance;
        this.ssn = ssn;
        this.pwd = pwd;
    }
    public double getBalance(){
        return balance;
    }
    public String getSsn(){
        return ssn;
    }
    public String getPwd(){
        return pwd;
    }
    public void setBalance(double balance){
        this.balance = balance;
    }
    public void setSsn(String ssn){
        this.ssn = ssn;
    }
    public void setPwd(String pwd){
        this.pwd = pwd;
    }
    public String toString(){
        return String.format("%s %s %10.2f", ssn, pwd, balance);
    }
}

