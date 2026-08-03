package com.mu.factory;

public class PaymentFactory {

    private PaymentFactory() {
    }

    public static PaymentStrategy createPayment(String paymentMethod) {

        if (paymentMethod == null) {
            return null;
        }

        switch (paymentMethod.trim().toLowerCase()) {

            case "cash":
                return new CashPayment();

            case "bkash":
                return new BkashPayment();

            case "card":
                return new CardPayment();

            default:
                return null;
        }
    }
}