package eci.edu.byteProgramming.ejercicio.paper.util;

public class PayPalPayment extends PaymentMethod {
    private String email;
    private String authToken;
    
    public PayPalPayment(double amount, String customerId, String description,
                         String email, String authToken) {
        super(amount, customerId, description);
        this.email = email;
        this.authToken = authToken;
    }
    
    @Override
    public boolean validatePaymentMethod() {
        return email != null && email.contains("@") && 
               authToken != null && authToken.length() > 10;
    }
    
    @Override
    public boolean processPayment() {
        System.out.println("💰 Processing PayPal payment...");
        
        if (!validatePaymentMethod()) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
        
        setStatus(PaymentStatus.PROCESSING);
        
        try {
            Thread.sleep(1000);
            System.out.println("✅ PayPal payment authorized for: " + email);
            setStatus(PaymentStatus.COMPLETED);
            return true;
        } catch (Exception e) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
    }
    
    @Override
    public String getPaymentMethod() {
        return "PAYPAL";
    }
}