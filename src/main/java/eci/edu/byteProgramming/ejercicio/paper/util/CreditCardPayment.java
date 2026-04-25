package eci.edu.byteProgramming.ejercicio.paper.util;

public class CreditCardPayment extends PaymentMethod {
    private String cardNumber;
    private String cardHolderName;
    private String expirationDate;
    private String cvv;
    private String cardType;
    
    public CreditCardPayment(double amount, String customerId, String description,
                             String cardNumber, String cardHolderName, 
                             String expirationDate, String cvv, String address) {
        super(amount, customerId, description);
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.cardType = determineCardType(cardNumber);
    }
    
    @Override
    public boolean validatePaymentMethod() {
        return cardNumber != null && cardNumber.length() >= 13 &&
               cvv != null && cvv.length() >= 3 &&
               expirationDate != null && expirationDate.matches("\\d{2}/\\d{2}");
    }
    
    @Override
    public boolean processPayment() {
        System.out.println("💳 Processing Credit Card payment...");
        
        if (!validatePaymentMethod()) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
        
        setStatus(PaymentStatus.PROCESSING);
        
        try {
            Thread.sleep(1500);
            System.out.println("✅ Credit card authorized: " + maskCardNumber());
            setStatus(PaymentStatus.COMPLETED);
            return true;
        } catch (Exception e) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
    }
    
    @Override
    public String getPaymentMethod() {
        return "CREDIT_CARD";
    }
    
    private String determineCardType(String cardNumber) {
        if (cardNumber.startsWith("4")) return "VISA";
        if (cardNumber.startsWith("5")) return "MASTERCARD";
        return "UNKNOWN";
    }
    
    private String maskCardNumber() {
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}
