import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Bidders implements Runnable {
    private double budget;
    private int aggressiveBehavior;
    //private Auction currentAuction;
    private String interest;
    private Communicator communicator;
    private int index;
    private static final double DEFAULT_CHANCE = 0.5;
    private static final double INTERESTED_CHANCE_INCREMENT = 0.2;
    private Set<Auction> registeredAuctions = new HashSet<>();

    public Bidders(double budget, int aggressiveBehavior, String interest) {
        this.budget = budget;
        this.aggressiveBehavior = aggressiveBehavior;
        this.interest=interest;
    }

    @Override
    public void run() {
        Random random = new Random();
        double budget = getBudget();
        //System.out.println("Hello"+Thread.currentThread().getName());
        while (true) {
            Auction currentAuction = Main.getAuctionForBidder(this.registeredAuctions);
            //System.out.println("Hello while"+Thread.currentThread().getName());
            if (currentAuction == null || !currentAuction.isRunning()) {
                //System.out.println("Hello break"+Thread.currentThread().getName());
                break;
            }
            double currentPrice = currentAuction.getCurrentPrice();

            // Check if the bidder is registered for the current auction
            //if (this.registeredAuctions.contains(currentAuction)) {
                //System.out.println("Hello register"+Thread.currentThread().getName());
                synchronized (currentAuction) {
                    if (!currentAuction.isAuctionEnded()) {
                        //System.out.println("Hello Auction active"+Thread.currentThread().getName());
                        int decision = 0;
                        decision += (this.budget / currentPrice)*10;
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
                        if (decision >= -1) {
                            System.out.println(decision);
                            currentAuction.bid(currentPrice);
                            //System.out.println("Bidder " + Thread.currentThread().getName() + " placed a bid of " + currentPrice + " euros on " + Products.getItemName(currentAuction.getProduct()));
                        }
                    }
               //}
            }

            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000,2000));
            } catch (InterruptedException e) {
                //System.out.println("Hello catch"+Thread.currentThread().getName());
                e.printStackTrace();
            }
        }
    }


    public void registerForAuction(Auction auction) {
        if (interestedInAuction(auction)) {
            this.registeredAuctions.add(auction);
            //System.out.println("Registered for " + Products.getItemName(auction.getProduct())+Thread.currentThread().getName());
        }
    }
    private boolean interestedInAuction(Auction auction) {
        double chance = DEFAULT_CHANCE;

        // Check if the auction's product type matches the bidder's interest
        if (auction.getProduct().getItemType().equals(interest)) {
            chance += INTERESTED_CHANCE_INCREMENT;
        }

        // Generate a random number between 0 and 1
        double random = Math.random();

        // Check if the random number is less than the calculated chance
        return random < chance;
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

    public Set<Auction> getRegisteredAuctions(){
        return this.registeredAuctions;
    }

}