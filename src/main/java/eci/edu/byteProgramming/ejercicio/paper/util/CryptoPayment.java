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