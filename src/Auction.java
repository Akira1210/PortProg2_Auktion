import java.util.concurrent.TimeUnit;
public class Auction implements Runnable {
    private Products product;
    private double currentPrice;
    private boolean auctionEnded;
    private double winningBid;
    private final Object lock = new Object();




    public Auction(Products product) {
        this.product = product;
        this.currentPrice = product.getStartingPrice();
        this.auctionEnded = false;

    }

    public void bid(double amount) {
        synchronized (lock) {
            if (!auctionEnded && amount >= currentPrice) {
                System.out.println("Bidder " + Thread.currentThread().getName() + " placed a bid of " + amount + " euros on " + Products.getItemName(product));
                currentPrice = amount;
                winningBid = amount;
                auctionEnded = true;
                lock.notifyAll();
            }
        }
    }

    @Override
    public void run() {
        synchronized (lock) {
            while (!auctionEnded && currentPrice > product.getMinimalPrice()) {
                currentPrice -= product.getDecrementPrice();
                System.out.println("Current price for " + Products.getItemName(product) + ": " + currentPrice + " euros.");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if (currentPrice <= product.getMinimalPrice()) {
                auctionEnded = true;
                System.out.println("Auction ended. Minimal price reached for " + Products.getItemName(product) + ": " + product.getMinimalPrice() + " euros.");
            }
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