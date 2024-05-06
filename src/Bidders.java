import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Bidders implements Runnable {
    private double budget;
    private int aggressiveBehavior;
    private Auction currentAuction;
    private String interest;

    public Bidders(double budget, int aggressiveBehavior, String interest) {
        this.budget = budget;
        this.aggressiveBehavior = aggressiveBehavior;
        this.interest=interest;
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
            Random rand = new Random();

            // Check if the bidder has enough budget
            if (currentPrice <= budget) {
                // Use a synchronized block to ensure that only one thread can call the bid method at a time
                synchronized (currentAuction) {
                    if (!currentAuction.isAuctionEnded()&rand.nextInt(0,100)>75) {
                        int decision = 0;
                        decision+=(this.budget/currentPrice)*100;
                        if (this.interest.equals(currentAuction.getProduct().getItemType())) {decision+=200;}
                        decision+=rand.nextInt(0,200);
                        if (decision>400) {currentAuction.bid(currentPrice);}

                        //currentAuction.bid(currentPrice);
                        System.out.println("Bidder " + Thread.currentThread().getName() + " placed a bid of " + currentPrice + " euros on " + Products.getItemName(product));
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