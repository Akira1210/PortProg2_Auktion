import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Bidders implements Runnable {
    private double budget;
    private int aggressiveBehavior;
    private String interest;
    private Communicator comm;
    private static final double DEFAULT_CHANCE = 0.5;
    private static final double INTERESTED_CHANCE_INCREMENT = 0.2;
    private Auction registeredAuction;
    private Set<Auction> AuctionsBacklog = new HashSet<>();

    public Bidders(double budget, int aggressiveBehavior, String interest) {
        this.budget = budget;
        this.aggressiveBehavior = aggressiveBehavior;
        this.interest=interest;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            Auction currentAuction = Main.getAuctionForBidder(this.registeredAuction);
            if (currentAuction == null || !currentAuction.isRunning()) {
                BackLogRegister();
                if (registeredAuction==null)
                break;
            }
            double currentPrice = comm.getCurrentPrice();

                synchronized (currentAuction) {
                    if (!currentAuction.isAuctionEnded()) {
                        int decision = 0;
                        decision += (this.budget / currentPrice)*10;
                        if (this.interest.equals(currentAuction.getProduct().getItemType())) {
                            decision += 200;
                        }
                        decision += random.nextInt(0, 200);

                        // Agressivität der Bieter
                        if (aggressiveBehavior > 50) {
                            decision += 100;
                        }

                        // Zufallsfaktor für Bieterentscheidung
                        if (random.nextBoolean()) {
                            decision += 50;
                        }
                        if (decision >= 800  & this.budget>=currentPrice) {
                            Reporter.addBoughtItems(currentAuction);
                            comm.toAucc(currentPrice, currentAuction);
                            this.budget-=currentPrice;                      //Budget wird anhand des ausgebenen Geldes reduziert
                            registeredAuction=null;
                            BackLogRegister();
                        }
                    }
                }

            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000,2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void BackLogRegister() {
        for (Auction auction : AuctionsBacklog) {       //Nächste Auktion wird aus dem Backlog geladen
            if (!auction.isAuctionEnded()) {
                registerForAuction(auction);
            }
            if (registeredAuction==null) {
            }
        }
    }

    public void registerForAuction(Auction auction) {
        if (interestedInAuction(auction)) {
            if (this.registeredAuction==null) {
            this.registeredAuction =auction;
            comm=auction.getComm();
            comm.registerBidder(this);
            }
            else {AuctionsBacklog.add(auction);}
        }
    }
    private boolean interestedInAuction(Auction auction) {
        double chance = DEFAULT_CHANCE;

        if (auction.getProduct().getItemType().equals(interest)) {
            chance += INTERESTED_CHANCE_INCREMENT;
        }

        // Zufallszahl zwischen 0 und 1
        double random = Math.random();

        // Überprüfen ob die generierte Zufallszahl kleiner ist als die berechnete Chance
        return random < chance;
    }


    public double getBudget() {
        return this.budget;
    }

    public Auction getRegisteredAuction(){
        return this.registeredAuction;
    }

    public Communicator getComm(){
        return this.comm;
    }

}