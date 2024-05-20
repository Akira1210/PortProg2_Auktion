import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Bidders implements Runnable {
    private double budget;
    private int aggressiveBehavior;
    private String interest;
    private Communicator comm;
    private int index;
    private static final double DEFAULT_CHANCE = 0.5;
    private static final double INTERESTED_CHANCE_INCREMENT = 0.2;
    //private Set<Auction> registeredAuctions = new HashSet<>();
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
                        if (decision >= 1  & this.budget>=currentPrice) {
                            Reporter.addBoughtItems(currentAuction);
                            comm.toAucc(currentPrice, currentAuction);
                            this.budget-=currentPrice;                      //Budget wird anhand des ausgebenen Geldes reduziert
                            //System.out.println(currentPrice+ " ausgebenen, noch übrig: " + this.getBudget());
                            //registeredAuctions.clear();                     //registeredAuctions leeren
                            registeredAuction=null;

                            for (Auction auction : AuctionsBacklog) {       //Nächste Auktion wird aus dem Backlog geladen
                                if (!auction.isAuctionEnded()) {
                                    registerForAuction(auction);
                                }
                            }

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
        return budget;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Auction getRegisteredAuction(){
        return this.registeredAuction;
    }

}