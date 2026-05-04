import java.util.Scanner;
public class PrintWriter {
     public static void main(String[] args) throws java.io.IOException {
        java.io.File file = new java.io.File("accounts.txt");
        Scanner scnr = new Scanner(file);
         while (scnr.hasNext()) {
            String ssn = scnr.next();
            String pwd = scnr.next();
            Double balance = scnr.nextDouble();
16       
17       


    
}
