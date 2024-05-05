import java.util.concurrent.TimeUnit;
public class Auction implements Runnable {
    private Products product;
    private double currentPrice;
    private boolean auctionEnded;
    private double winningBid;

    public Auction(Products product) {
        this.product = product;
        this.currentPrice = product.getStartingPrice();
        this.auctionEnded = false;
    }


    public synchronized void bid(double amount) {
        if (!auctionEnded && amount >= currentPrice) {
            System.out.println("Bidder " + Thread.currentThread().getName() + " placed a bid of " + amount + " euros on " + Products.getItemName(product));
            currentPrice = amount;
            winningBid = amount; // Set the winning bid to the new bid amount
            auctionEnded = true;
            notifyAll(); // Notify all waiting threads that the auction has ended
        }
    }


    @Override
    public void run() {
        try {
            // Display initial current price
            System.out.println("Current price for " + Products.getItemName(product) + ": " + currentPrice + " euros.");

            synchronized (this) {
                wait(5000); // Wait for 5 seconds before starting decrements
            }

            while (!auctionEnded && currentPrice > product.getMinimalPrice()) {
                currentPrice -= product.getDecrementPrice();
                System.out.println("Current price for " + Products.getItemName(product) + ": " + currentPrice + " euros.");
                synchronized (this) {
                    wait(5000); // Wait for 5 seconds before decrementing the price again
                }
            }

            if (currentPrice <= product.getMinimalPrice()) {
                auctionEnded = true;
                System.out.println("Auction ended. Minimal price reached for " + product + ": " + product.getMinimalPrice() + " euros.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isAuctionEnded() {
        return auctionEnded;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }
    public Products getProduct() {
        return product;
    }
    public double getWinningBid() {
        return winningBid;
    }

}