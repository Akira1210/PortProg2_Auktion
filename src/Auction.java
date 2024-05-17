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
        //synchronized (lock) {
            if (!auctionEnded && amount >= currentPrice) {
                System.out.println("Bieter" + Thread.currentThread().getName() + " hat ein Gebot von " + amount + " Euro für " + Products.getItemName(product) + " abgegeben.");
                currentPrice = amount;
                winningBid = amount;
                auctionEnded = true;
                lock.notifyAll();
            }
        //}
    }

    @Override
    public void run() {
        synchronized (lock) {
            while (!auctionEnded && currentPrice > product.getMinimalPrice()) {
                currentPrice -= product.getDecrementPrice();
                System.out.println("Der aktuelle Preis für " + Products.getItemName(product) + " liegt bei: " + currentPrice + " Euro.");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if (currentPrice <= product.getMinimalPrice()) {
                auctionEnded = true;
                System.out.println("Auktion beendet. Der Mindestpreis für " + Products.getItemName(product) + " von: " + product.getMinimalPrice() + " Euro wurde erreicht.");
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

    public boolean isRunning() {
        return !auctionEnded;
    }
}