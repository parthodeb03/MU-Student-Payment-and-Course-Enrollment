package com.mu.factory;

public class CashPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {

        System.out.println("================================");
        System.out.println("Cash Payment");
        System.out.println("Amount : " + amount);
        System.out.println("Payment Successful.");
        System.out.println("================================");

    }

}