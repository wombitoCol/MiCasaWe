package eci.edu.byteProgramming.ejercicio.paper.util;

public class PaymentEventObserver implements PaymentObserver {
    private Inventory inventory;
    private Facturation facturation;
    private Notification notification;
    
    public PaymentEventObserver(Inventory inventory, Facturation facturation, Notification notification) {
        this.inventory = inventory;
        this.facturation = facturation;
        this.notification = notification;
    }
    
    @Override
    public void onPaymentSuccess(PaymentMethod payment, String customerName, 
                                 String customerEmail, String productId) {
        System.out.println("\n📢 Payment Observer: Processing successful payment...");
        
        // Descontar inventario
        Product product = inventory.getProduct(productId);
        if (product != null) {
            inventory.discountProduct(productId, 1);
        }
        
        // Generar factura
        String productDetails = product != null ? product.getName() : "Producto genérico";
        facturation.generateInvoice(payment, customerName, productDetails);
        
        // Enviar notificación
        notification.sendConfirmationEmail(customerEmail, customerName, payment);
        
        System.out.println("✅ All post-payment processes completed!\n");
    }
    
    @Override
    public void onPaymentFailed(PaymentMethod payment, String customerEmail) {
        System.out.println("\n❌ Payment Observer: Payment failed");
        notification.sendFailureNotification(payment, customerEmail);
    }
}