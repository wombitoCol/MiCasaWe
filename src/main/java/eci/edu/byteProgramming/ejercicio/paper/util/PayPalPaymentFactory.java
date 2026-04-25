package eci.edu.byteProgramming.ejercicio.paper.util;

public class PayPalPaymentFactory implements PaymentFactory {
    private String email;
    private String authToken;
    
    public PayPalPaymentFactory(String email, String authToken) {
        this.email = email;
        this.authToken = authToken;
    }
    
    @Override
    public PaymentMethod createPaymentMethod(double amount, String customerId, String description) {
        return new PayPalPayment(amount, customerId, description, email, authToken);
    }
}