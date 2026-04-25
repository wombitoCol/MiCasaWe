package eci.edu.byteProgramming.ejercicio.paper.util;

public class ShoppingService {
    private ECIPayment eciPayment;
    
    public ShoppingService() {
        this.eciPayment = new ECIPayment();
    }
    
    public void registerObserver(PaymentObserver observer) {
        eciPayment.addObserver(observer);
    }
    
    public boolean purchaseProduct(PaymentFactory factory, double amount, 
                                   String customerId, String customerName, 
                                   String customerEmail, String productId, 
                                   String productDescription) {
        return eciPayment.processPayment(factory, amount, customerId, 
                                         productDescription, customerName, 
                                         customerEmail, productId);
    }
}