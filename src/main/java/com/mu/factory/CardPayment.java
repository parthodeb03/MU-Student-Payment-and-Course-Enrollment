package com.mu.factory;

public class CardPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("Card Payment");
        System.out.println("Amount : " + amount);
        System.out.println("Payment Successful.");

    }

}