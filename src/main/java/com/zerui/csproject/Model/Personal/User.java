package com.zerui.csproject.Model.Personal;
import com.zerui.csproject.Utils.Firebase;

public class User {
    private static String password, email;
    private static Account account;
    public static int login(String email, String password) {
        String id = Firebase.login(email, password);
        if (id!=null) {
            if (Firebase.isVerified(email)) {
                User.password = password;
                User.email = email;
                account = Firebase.getAccount(id);
                return 2;
            } else return 1;
        }
        else return 0;
    }
    public static boolean changePassword(String oldPassword, String newPassword) {
        return false;
    }
}
