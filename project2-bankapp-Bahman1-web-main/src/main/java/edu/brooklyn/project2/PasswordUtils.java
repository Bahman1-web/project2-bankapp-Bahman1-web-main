package edu.brooklyn.project2;

import java.util.HexFormat;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
    public static String getPasswordHash(String pw) {
        MessageDigest msgDigest;
        try {
            msgDigest = MessageDigest.getInstance("SHA-256");
            msgDigest.update(pw.getBytes());
            String pwHash = HexFormat.of().formatHex(msgDigest.digest());
            return pwHash;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String readPassword(Scanner input) {
        java.io.Console console = System.console();
        if (console == null) {
            return input.nextLine();
        }
        return new String(System.console().readPassword());
    }
}
