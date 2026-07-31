package com.mu.factory;

public class BkashPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {

        System.out.println("================================");
        System.out.println("Bkash Payment");
        System.out.println("Amount : " + amount);
        System.out.println("Payment Successful.");
        System.out.println("================================");

    }

}