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