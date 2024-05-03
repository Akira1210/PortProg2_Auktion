import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Bidders implements Runnable {
    private double budget;
    private boolean aggressiveBehavior;
    private Auction currentAuction;

    public Bidders(double budget, boolean aggressiveBehavior) {
        this.budget = budget;
        this.aggressiveBehavior = aggressiveBehavior;
    }


    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            Auction currentAuction = Main.getCurrentAuction();
            if (currentAuction == null || currentAuction.isAuctionEnded()) {
                break; // Exit the loop if the auction has ended or not started
            }

            double currentPrice = currentAuction.getCurrentPrice();
            double budget = getBudget();
            Products product = currentAuction.getProduct();

            // Check if the bidder has enough budget
            if (currentPrice <= budget) {
                // Use a synchronized block to ensure that only one thread can call the bid method at a time
                synchronized (currentAuction) {
                    if (!currentAuction.isAuctionEnded()) {
                        // Place a bid with the current price
                        currentAuction.bid(currentPrice);
                        System.out.println("Bidder " + this + " placed a bid of " + currentPrice + " euros on " + product);
                    }
                }
            } else {
                break; // Exit the loop if the bidder does not have enough budget
            }

            // Wait for a random time before placing the next bid
            try {
                Thread.sleep(random.nextInt(5000) + 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public double getBudget() {
        return budget;
    }
}