import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
/**
 * Klasse, die Bieter als Objekte definiert
 * Enthält unteranderem Budget, Aggressivität, Interesse, Kommunikator und registrierte Auktionen
 */
public class Bidders implements Runnable {
    private double budget;
    private int aggressiveBehavior;
    private String interest;
    private Communicator comm;
    private static final double DEFAULT_CHANCE = 0.5;
    private static final double INTERESTED_CHANCE_INCREMENT = 0.2;
    private Auction registeredAuction;
    private Set<Auction> AuctionsBacklog = new HashSet<>();

    /**
     * Konstruktor für Bieter
     * @param budget                Budget für Bieter
     * @param aggressiveBehavior    Aggressivität des Bieters zwischen 0 un 100
     * @param interest              Interesse des Bieters an eine bestimmte Art von Artikeln
     */
    public Bidders(double budget, int aggressiveBehavior, String interest) {
        this.budget = budget;
        this.aggressiveBehavior = aggressiveBehavior;
        this.interest=interest;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            Auction currentAuction = Main.getAuctionForBidder(this.registeredAuction); //Versucht Auktion aus registeredAuction von den aktiven Auktionen zu bekommen
            if (currentAuction == null || !currentAuction.isRunning()) {               //Falls Auktion aus registeredAuction nicht gefunden werden kann oder inaktiv ist,
                BackLogRegister();                                                     //wird hier weitere Auktion aus Backlog geladen
                currentAuction = Main.getAuctionForBidder(this.registeredAuction);                                                    
                if (registeredAuction==null)                                           //Falls weiterhin keine passende Auktion gefunden werden kann, abbrechen
                break;
            }
            double currentPrice = comm.getCurrentPrice();                              //setzt aktueller Preis

                synchronized (currentAuction) { //synchroner Zugriff, damit z.B. doppel Gebote vermieden werden
                    
                    if (!currentAuction.isAuctionEnded()) {
                        int decision = 0;

                        decision += (this.budget / currentPrice)*10; //Umso mehr Geld der Bieter besitzt, umso wahrscheinlich ist ein Gebot, abhängig von Produktspreis

                        if (this.interest.equals(currentAuction.getProduct().getItemType())) { //Wenn Bieter an Art dieses Produkts allgemein interessiert ist, ist ein Gebot wahrscheinlicher
                            decision += 200;
                        }
                        decision += random.nextInt(0, 200);  //Zufallfaktor 1, da Entscheidungen häufig auch impulsiv gefällt werden

                        // Agressivität der Bieter
                        if (aggressiveBehavior > 50) {
                            decision += 100;
                        }

                        //Zufallfaktor 2, da Entscheidungen häufig auch impulsiv gefällt werden
                        if (random.nextBoolean()) {
                            decision += 50;
                        }

                        // Falls Desicion >=800 und das Budget ausreichend ist, gibt der Bieter ein Gebot ab
                        if (decision >= 800  & this.budget>=currentPrice) {
                            Reporter.addBoughtItems(currentAuction);        //Reporter wird gemeldet, das ein Produkt gekauft wurde
                            comm.toAucc(currentPrice, currentAuction);      //Allen anderen wird gemeldet, dass ein Gebot abgegeben wurde 
                            this.budget-=currentPrice;                      //Budget wird anhand des ausgebenen Geldes reduziert
                            registeredAuction=null;                         //registeredAuction wird null gesetzt und
                            BackLogRegister();                              //nächste Auktion wird aus dem Backlog geladen
                        }
                    }
                }

            //Pause bevor Bieter sich erneut Überlegt, ein Gebot abzugeben
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000,2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Eine Warteliste für Auktionen, an denen der Bieter interessiert ist
     * Auktionen werden in Backlog gespeichert, wenn der Bieter bereits an einer Auktion aktiv teilnimmt
     * Hier wird die nächste aktive Auktion aus dem Backlog geladen, wenn die Auktion an dem der Bieter aktiv teilnahm beendet wurde
     */
    private void BackLogRegister() {
        for (Auction auction : AuctionsBacklog) {       //Nächste Auktion wird aus dem Backlog geladen
            if (!auction.isAuctionEnded()) {
                registerForAuction(auction);
            }
            if (registeredAuction==null) {
            }
        }
    }

    /**
     * Registriert Bieter für Auktion
     * Interesse wird zuvor überprüft
     * Auktionen werden in AuctionsBacklog gespeichert, wenn der Bieter bereits an einer Auktion aktiv teilnimmt
     * @param auction
     */
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
     
    /**
     * Überprüft das Interesse des Bieters
     * Zufallsfaktor spielt ebenfalls eine Rolle
     * @param auction
     * @return Boolean, wenn true nimmt Bieter an Auktion teil
     */
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

    // GETTERS

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