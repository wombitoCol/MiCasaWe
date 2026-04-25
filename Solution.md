## Solucion problema 1
# Promps
--------------------------------------------
📝 Problema #1: El Videoclub de Don
Duración: Máximo 15 minutos
Don Mario acaba de abrir un videoclub moderno donde los clientes pueden alquilar peliculas fisicas o digitales. El problema es que su sistema anterior era un caos: todos los precios se calculaban igual sin importar el tipo de pelicula o membresia del cliente, y no habia forma de saber que peliculas estaban disponibles en tiempo real.

Tu Mision
Ayuda a Don Mario creando un sistema de alquiler que permita:

Registrar peliculas (fisicas o digitales) con su disponibilidad.
Que el cliente elija X peliculas para alquilar.
Calcular el precio total segun su tipo de membresia:
Basica: precio normal.
Premium: 20% de descuento.
Mostrar al finalizar un recibo con las peliculas, precio por unidad y total.
Peliculas Disponibles
[Fisica] Interestellar - $8.000 - Disponible
[Fisica] El Padrino - $7.000 - No disponible
[Digital] Inception - $5.000 - Disponible
[Digital] Matrix - $6.000 - Disponible
Caso de Ejemplo
Membresia del cliente: Premium
Seleccione peliculas (numeros separados por coma): 1,3

--- RECIBO DE ALQUILER ---
Cliente: Premium
Peliculas:
 - Interestellar (Fisica) - $8.000
 - Inception (Digital) - $5.000
Subtotal: $13.000
Descuento (20%): $2.600
Total a pagar: $10.400
¡Disfrute su pelicula!
Objetivos del Ejercicio
Identificar cual o cuales patrones de diseno utilizar.
Explicar que principios de SOLID se aplican.
Aplicar polimorfismo y encapsulamiento.
Colocar evidencia de la ejecucion del ejercicio (ejecucion por consola; no es necesario hacer front).

ayudame a tener el codigo de este problema y por favor que se cumpla todo de una forma sencilla nada compleja pero cumpliendo los requerimientos.
------------------------------------
no no es una estructura apache maven java
------------------------------------
no deepseek no es necesario que crees todas esas clases ni mucho menos un main porque la estructura maven ya me da una clase Aplication.java que es el @SpringBootApplication
------------------------------------
Codigo
package com.videoclub.model;

public class Pelicula {
    private String titulo;
    private String tipo; // "Fisica" o "Digital"
    private double precio;
    private boolean disponible;
    
    public Pelicula(String titulo, String tipo, double precio, boolean disponible) {
        this.titulo = titulo;
        this.tipo = tipo;
        this.precio = precio;
        this.disponible = disponible;
    }
    
    // Getters
    public String getTitulo() { return titulo; }
    public String getTipo() { return tipo; }
    public double getPrecio() { return precio; }
    public boolean isDisponible() { return disponible; }
}

package com.videoclub.model;

public enum TipoMembresia {
    BASICA {
        @Override
        public double aplicarDescuento(double subtotal) {
            return subtotal;
        }
    },
    PREMIUM {
        @Override
        public double aplicarDescuento(double subtotal) {
            return subtotal * 0.8; // 20% descuento
        }
    };
    
    public abstract double aplicarDescuento(double subtotal);
}

package com.videoclub.model;

public class Membresia {
    private TipoMembresia tipo;
    
    public Membresia(TipoMembresia tipo) {
        this.tipo = tipo;
    }
    
    public double calcularTotal(double subtotal) {
        return tipo.aplicarDescuento(subtotal);
    }
    
    public String getNombre() {
        return tipo.name();
    }
}

package com.videoclub.service;

import com.videoclub.model.Pelicula;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AlquilerService {
    
    // Catálogo de películas (simulando BD)
    private final List<Pelicula> catalogo = Arrays.asList(
        new Pelicula("Interestellar", "Fisica", 8000, true),
        new Pelicula("El Padrino", "Fisica", 7000, false),
        new Pelicula("Inception", "Digital", 5000, true),
        new Pelicula("Matrix", "Digital", 6000, true)
    );
    
    public List<Pelicula> getCatalogo() {
        return catalogo;
    }
    
    public List<Pelicula> seleccionarPeliculas(List<Integer> indices) {
        List<Pelicula> seleccionadas = new ArrayList<>();
        for (int indice : indices) {
            if (indice >= 1 && indice <= catalogo.size()) {
                Pelicula p = catalogo.get(indice - 1);
                if (p.isDisponible()) {
                    seleccionadas.add(p);
                }
            }
        }
        return seleccionadas;
    }
    
    public double calcularSubtotal(List<Pelicula> peliculas) {
        return peliculas.stream().mapToDouble(Pelicula::getPrecio).sum();
    }
}

package com.videoclub.service;

import com.videoclub.model.Pelicula;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReciboService {
    
    public void generarRecibo(String tipoMembresia, List<Pelicula> peliculas, 
                              double subtotal, double total) {
        double descuento = subtotal - total;
        
        System.out.println("\n--- RECIBO DE ALQUILER ---");
        System.out.println("Cliente: " + tipoMembresia);
        System.out.println("Peliculas:");
        peliculas.forEach(p -> 
            System.out.printf(" - %s (%s) - $%.0f%n", p.getTitulo(), p.getTipo(), p.getPrecio())
        );
        System.out.printf("Subtotal: $%.0f%n", subtotal);
        if (descuento > 0) {
            System.out.printf("Descuento (20%%): $%.0f%n", descuento);
        }
        System.out.printf("Total a pagar: $%.0f%n", total);
        System.out.println("--------------------------");
        System.out.println("¡Disfrute su pelicula!");
    }
}

package com.videoclub.controller;

import com.videoclub.model.*;
import com.videoclub.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import java.util.*;

@Controller
public class AlquilerController implements CommandLineRunner {
    
    @Autowired
    private AlquilerService alquilerService;
    
    @Autowired
    private ReciboService reciboService;
    
    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        
        // Mostrar catálogo
        System.out.println("\n--- CATÁLOGO DE PELÍCULAS ---");
        List<Pelicula> catalogo = alquilerService.getCatalogo();
        for (int i = 0; i < catalogo.size(); i++) {
            Pelicula p = catalogo.get(i);
            String estado = p.isDisponible() ? "Disponible" : "No disponible";
            System.out.printf("%d. %s (%s) - $%.0f - %s%n", 
                i+1, p.getTitulo(), p.getTipo(), p.getPrecio(), estado);
        }
        
        // Seleccionar membresía
        System.out.println("\nSeleccione su tipo de membresía:");
        System.out.println("1. Básica");
        System.out.println("2. Premium");
        System.out.print("Opción: ");
        int opcion = scanner.nextInt();
        
        TipoMembresia tipo = (opcion == 2) ? TipoMembresia.PREMIUM : TipoMembresia.BASICA;
        Membresia membresia = new Membresia(tipo);
        
        // Seleccionar películas
        System.out.print("\nSeleccione películas (números separados por coma): ");
        scanner.nextLine();
        String[] indicesStr = scanner.nextLine().split(",");
        List<Integer> indices = Arrays.stream(indicesStr)
            .map(s -> Integer.parseInt(s.trim()))
            .toList();
        
        // Procesar alquiler
        List<Pelicula> seleccionadas = alquilerService.seleccionarPeliculas(indices);
        
        if (seleccionadas.isEmpty()) {
            System.out.println("No se seleccionó ninguna película válida.");
            return;
        }
        
        double subtotal = alquilerService.calcularSubtotal(seleccionadas);
        double total = membresia.calcularTotal(subtotal);
        
        // Generar recibo
        reciboService.generarRecibo(membresia.getNombre(), seleccionadas, subtotal, total);
        
        scanner.close();
    }
}

Para hacer este codigo de maven del primer punto me demore unos 8 minutos. 
Muchas gracias a Ignacio por no aclarar que no necesariamente tenia que ser en maven :v me hizo perder el tiempo mlp.
## Ejercicio dos 
# Promp
Duración: Máximo 25 minutos
Descripción del Problema
Una tienda virtual necesita implementar un sistema de pagos que soporte múltiples métodos de pago:

Tarjeta de crédito
PayPal
Criptomonedas
Cada método tiene su propio proceso de validación y ejecución. El sistema debe:

Crear objetos de pago y sus validadores correspondientes
No exponer los detalles internos a la lógica principal de compras
Notificar automáticamente a otros componentes cuando se procesa un pago exitoso:
📦 Módulo de inventario: descontar del stock
📄 Módulo de facturación: generar factura
📧 Módulo de notificaciones: enviar correo al cliente
Requisitos Técnicos
La solución debe ser flexible para:

✅ Soportar nuevos métodos de pago sin modificar la lógica existente
✅ Permitir que nuevos módulos reaccionen a eventos de pago sin cambiar el core
Pistas de patrones:

Se requiere un mecanismo para crear familias de objetos relacionados (pago + validador)
Se requiere un mecanismo para notificar automáticamente a múltiples observadores de eventos
ayudame a crear lo que falta aqui para que pueda funcionar con los requeriminetos
Veo que el código existente tiene varios problemas y no cumple completamente con los requisitos. Te voy a mostrar lo que falta y lo que está mal, y te daré la solución completa.

Problemas identificados
PaymentMethod tiene errores: El constructor no usa bien los parámetros

Falta PaymentFactory: Esencial para crear familias de objetos de pago

Falta integración: No hay un flujo de compra completo

PaymentEventObserver tiene error: Importa javax.management.Notification incorrecto

No hay demo funcional: Falta una clase principal que ejecute todo

Solución completa (lo que falta)
1. Corregir PaymentMethod.java
java
package eci.edu.byteProgramming.ejercicio.paper.util;

import java.util.Date;
import java.util.UUID;

public abstract class PaymentMethod implements ValidatePayment {
    protected double amount;
    protected String transactionID;
    protected String customerID;
    protected String currency;
    protected Date timestamp;
    protected PaymentStatus status;
    protected String description;

    // Constructor corregido
    public PaymentMethod(double amount, String customerID, String description) {
        this.amount = amount;
        this.customerID = customerID;
        this.description = description;
        this.currency = "USD";
        this.status = PaymentStatus.PENDING;
        this.timestamp = new Date();
        this.transactionID = generateTransactionId();
    }

    public abstract boolean processPayment();
    public abstract String getPaymentMethod();

    protected String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4);
    }

    // Getters
    public double getAmount() { return amount; }
    public String getTransactionId() { return transactionID; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public String getCustomerId() { return customerID; }
    public String getDescription() { return description; }
    public Date getTimestamp() { return timestamp; }
}
2. Crear PaymentFactory.java (Factory Pattern)
java
package eci.edu.byteProgramming.ejercicio.paper.util;

public interface PaymentFactory {
    PaymentMethod createPaymentMethod(double amount, String customerId, String description);
}
3. Crear fábricas concretas
CreditCardPaymentFactory.java

java
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
PayPalPaymentFactory.java

java
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
CryptoPaymentFactory.java

java
package eci.edu.byteProgramming.ejercicio.paper.util;

public class CryptoPaymentFactory implements PaymentFactory {
    private String walletAddress;
    private String cryptoType;
    private double walletBalance;
    
    public CryptoPaymentFactory(String walletAddress, String cryptoType, double walletBalance) {
        this.walletAddress = walletAddress;
        this.cryptoType = cryptoType;
        this.walletBalance = walletBalance;
    }
    
    @Override
    public PaymentMethod createPaymentMethod(double amount, String customerId, String description) {
        return new CryptoPayment(amount, customerId, description, walletAddress, cryptoType, walletBalance);
    }
}
4. Renombrar y corregir las clases de pago
CreditCardPayment.java (antes CreditCardFactory)

java
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
PayPalPayment.java (antes PaypalFactory)

java
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
CryptoPayment.java (antes CryptoFactory)

java
package eci.edu.byteProgramming.ejercicio.paper.util;

public class CryptoPayment extends PaymentMethod {
    private String walletAddress;
    private String cryptoType;
    private double walletBalance;
    private String blockchainHash;
    
    public CryptoPayment(double amount, String customerId, String description,
                         String walletAddress, String cryptoType, double walletBalance) {
        super(amount, customerId, description);
        this.walletAddress = walletAddress;
        this.cryptoType = cryptoType;
        this.walletBalance = walletBalance;
    }
    
    @Override
    public boolean validatePaymentMethod() {
        return walletAddress != null && walletAddress.length() >= 26 &&
               walletBalance >= getAmount();
    }
    
    @Override
    public boolean processPayment() {
        System.out.println("₿ Processing Cryptocurrency payment...");
        
        if (!validatePaymentMethod()) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
        
        setStatus(PaymentStatus.PROCESSING);
        
        try {
            Thread.sleep(2000);
            this.blockchainHash = "0x" + Integer.toHexString((int)(Math.random() * Integer.MAX_VALUE));
            System.out.println("✅ Crypto transaction confirmed: " + blockchainHash);
            setStatus(PaymentStatus.COMPLETED);
            return true;
        } catch (Exception e) {
            setStatus(PaymentStatus.FAILED);
            return false;
        }
    }
    
    @Override
    public String getPaymentMethod() {
        return "CRYPTOCURRENCY";
    }
}
5. Corregir PaymentEventObserver.java
java
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
6. Crear clase ShoppingService.java (lógica principal)
java
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
7. Crear Demo.java (clase principal para probar)
java
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
Patrones de diseño implementados
Patrón	Dónde se aplica
Abstract Factory	PaymentFactory y sus implementaciones concretas
Observer	PaymentObserver, PaymentEventObserver
Strategy	Diferentes métodos de pago (comportamiento intercambiable)
Template Method	PaymentMethod define el esqueleto
Principios SOLID aplicados
✅ Open/Closed: Nuevos métodos de pago solo requieren nueva clase, sin modificar ECIPayment
✅ Liskov: Todos los PaymentMethod pueden reemplazar a la clase base
✅ Dependency Inversion: ShoppingService depende de abstracciones (PaymentFactory, PaymentObserver)
✅ Single Responsibility: Cada módulo tiene una responsabilidad clara

Cómo ejecutar
bash
# Compilar
javac eci/edu/byteProgramming/ejercicio/paper/util/*.java
javac eci/edu/byteProgramming/ejercicio/paper/Demo.java

# Ejecutar
java eci.edu.byteProgramming.ejercicio.paper.Demo
El sistema ahora es flexible, extensible y cumple con todos los requisitos.

This response is AI-generated, for reference only.
-------------
No ejecuto Ignacio me presiono y no pude hacerlo bien califico el servicio con dos de dies 
PS C:\Users\julio.mayorquin\Documents\MiCasaWe> javac eci/edu/byteProgramming/ejercicio/paper/util/*.java
error: Invalid filename: eci/edu/byteProgramming/ejercicio/paper/util/*.java
Usage: javac <options> <source files>
use --help for a list of possible options
PS C:\Users\julio.mayorquin\Documents\MiCasaWe> 