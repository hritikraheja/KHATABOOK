package com.example.khatabook.modalClasses;

import java.io.Serializable;
import java.util.Objects;

public class User implements Comparable<User>, Serializable {
    private String name;
    private String accountNumber;
    private String email;
    private String phoneNumber;
    private double accountBalance;
    private String password;

    public User(String name, String accountNumber, String email, String phoneNumber, double accountBalance, String password) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.accountBalance = accountBalance;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", accountBalance=" + accountBalance +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public int compareTo(User o) {
        return this.getAccountNumber().compareTo(o.getAccountNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Double.compare(user.accountBalance, accountBalance) == 0 &&
                Objects.equals(name, user.name) &&
                Objects.equals(accountNumber, user.accountNumber) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, accountNumber, email, phoneNumber, accountBalance, password);
    }
}