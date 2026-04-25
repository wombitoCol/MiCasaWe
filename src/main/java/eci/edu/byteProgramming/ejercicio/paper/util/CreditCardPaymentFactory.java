package eci.edu.byteProgramming.ejercicio.paper.util;

public class CreditCardPaymentFactory implements PaymentFactory {
    private String cardNumber;
    private String cardHolderName;
    private String expirationDate;
    private String cvv;
    private String address;
    
    public CreditCardPaymentFactory(String cardNumber, String cardHolderName, 
                                    String expirationDate, String cvv, String address) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.address = address;
    }
    
    @Override
    public PaymentMethod createPaymentMethod(double amount, String customerId, String description) {
        return new CreditCardPayment(amount, customerId, description, 
                                     cardNumber, cardHolderName, expirationDate, cvv, address);
    }
}
