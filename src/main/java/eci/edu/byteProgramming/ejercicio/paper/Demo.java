package eci.edu.byteProgramming.ejercicio.paper;

import eci.edu.byteProgramming.ejercicio.paper.util.*;

public class Demo {
    public static void main(String[] args) {
        System.out.println("🎬 INICIANDO SISTEMA DE PAGOS ECI\n");
        
        // Inicializar módulos
        Inventory inventory = new Inventory();
        Facturation facturation = new Facturation();
        Notification notification = new Notification();
        
        // Crear observer que reacciona a eventos
        PaymentEventObserver paymentObserver = new PaymentEventObserver(inventory, facturation, notification);
        
        // Servicio de compras
        ShoppingService shoppingService = new ShoppingService();
        shoppingService.registerObserver(paymentObserver);
        
        // === CASO 1: Pago con Tarjeta de Crédito ===
        System.out.println("═══════════════════════════════════════");
        System.out.println("📦 COMPRA #1: Laptop Gamer");
        System.out.println("═══════════════════════════════════════");
        
        PaymentFactory creditCardFactory = new CreditCardPaymentFactory(
            "4532015112830366", "Juan Perez", "12/25", "123", "Calle 123"
        );
        
        boolean success1 = shoppingService.purchaseProduct(
            creditCardFactory, 1200.00, "CUST001", "Juan Perez", 
            "juan@email.com", "LAPTOP001", "Gaming Laptop"
        );
        
        System.out.println("Resultado: " + (success1 ? "✅ ÉXITO" : "❌ FALLIDO") + "\n");
        
        // === CASO 2: Pago con PayPal ===
        System.out.println("═══════════════════════════════════════");
        System.out.println("📦 COMPRA #2: Smartphone");
        System.out.println("═══════════════════════════════════════");
        
        PaymentFactory paypalFactory = new PayPalPaymentFactory(
            "maria@email.com", "auth_token_12345678901"
        );
        
        boolean success2 = shoppingService.purchaseProduct(
            paypalFactory, 800.00, "CUST002", "Maria Gomez", 
            "maria@email.com", "PHONE001", "Smartphone"
        );
        
        System.out.println("Resultado: " + (success2 ? "✅ ÉXITO" : "❌ FALLIDO") + "\n");
        
        // === CASO 3: Pago con Criptomonedas (saldo insuficiente - debería fallar) ===
        System.out.println("═══════════════════════════════════════");
        System.out.println("📦 COMPRA #3: Libro de Java (FALLARÁ - saldo insuficiente)");
        System.out.println("═══════════════════════════════════════");
        
        PaymentFactory cryptoFactory = new CryptoPaymentFactory(
            "0x742d35Cc6634C0532925a3b844Bc9e7595f0b0f1", 
            "ETH", 40.00  // Saldo insuficiente para pagar $45.99
        );
        
        boolean success3 = shoppingService.purchaseProduct(
            cryptoFactory, 45.99, "CUST003", "Carlos Ruiz", 
            "carlos@email.com", "BOOK001", "Java Programming Book"
        );
        
        System.out.println("Resultado: " + (success3 ? "✅ ÉXITO" : "❌ FALLIDO"));
    }
}
