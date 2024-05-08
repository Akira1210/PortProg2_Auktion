import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Bidders implements Runnable {
    private double budget;
    private int aggressiveBehavior;
    private Auction currentAuction;
    private String interest;
    private Communicator communicator;
    private int index;

    public Bidders(double budget, int aggressiveBehavior, String interest) {
        this.budget = budget;
        this.aggressiveBehavior = aggressiveBehavior;
        this.interest=interest;
    }



    @Override
    public void run() {
        Random random = new Random();
        double budget = getBudget();
        while (true) {
            Auction currentAuction = Main.getCurrentAuction();
            if (currentAuction == null || currentAuction.isAuctionEnded()) {
                break;
            }

            double currentPrice = currentAuction.getCurrentPrice();
            if (currentPrice <= budget) {
                synchronized (currentAuction) {
                    if (!currentAuction.isAuctionEnded()) {
                        int decision = 0;
                        decision += (this.budget / currentPrice) * 100;
                        if (this.interest.equals(currentAuction.getProduct().getItemType())) {
                            decision += 200;
                        }
                        decision += random.nextInt(0, 200);

                        // Advanced bidding logic: aggressive behavior
                        if (aggressiveBehavior > 50) {
                            decision += 100;
                        }

                        // Advanced bidding logic: randomness factor
                        if (random.nextBoolean()) {
                            decision += 50;
                        }

                        if (decision > 400) {
                            currentAuction.bid(currentPrice);
                            System.out.println("Bidder " + Thread.currentThread().getName() + " placed a bid of " + currentPrice + " euros on " + Products.getItemName(currentAuction.getProduct()));
                        }
                    }
                }
            } else {
                break;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(5000) + 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public double getBudget() {
        return budget;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }
}