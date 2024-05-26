import java.util.ArrayList;
import java.util.List;
/**
 * Klasse für die Kommunikation zwischen Bietern und Auktionatoren. Bieter bekommen nur Information von dem Auktionator, für dessen Auktion sie auch registriert sind. Bieter können auch nur diesem Auktionator Informationen senden.
 * Enthält Liste der Bieter, Variable des Auktionators und den aktuellen Preis
 * Jede Auktion erhält einen festen Kommunikator
 */

public class Communicator {
    private List<Bidders> bidders;
    private Auctioneers auctioneers;
    private double currentPrice;

    public Communicator() {
        this.bidders = new ArrayList<>();
    }

    // Registriert Bieter für diese spezielle Communicator-Instanz
    public void registerBidder(Bidders bidder) {
        bidders.add(bidder);
    }

    // Registriert Auktionator für diesen speziell Communicator-Instanz, nur ein Auktionator ist an einem Communicator beteiligt
    public  void registerAuctioneer(Auctioneers auctioneer) {
        this.auctioneers=auctioneer;
    }
    

    //Gebot vom Bieter, ruft die bid Methode in der Auction Klasse auf
    public void toAucc(double bid, Auction auction) {
        auction.bid(bid);
    }

    //Neuer Preis vom Auktionator
    public void toBidd(double newPrice) {
        this.currentPrice=newPrice;
    }

    //Wird vom Bieter aufgerufen, gibt neuen bzw. aktuellen Preis zurück
    public double getCurrentPrice(){
        return currentPrice;
    }
}
