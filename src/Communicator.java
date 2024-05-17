import java.util.ArrayList;
import java.util.List;

public class Communicator {
    private List<Bidders> bidders;
    private List<Auctioneers> auctioneers;

    public Communicator() {
        this.bidders = new ArrayList<>();
        this.auctioneers = new ArrayList<>();
    }

    public void registerBidder(Bidders bidder) {
        bidders.add(bidder);
    }

    public void registerAuctioneer(Auctioneers auctioneer) {
        auctioneers.add(auctioneer);
    }

    public void notifyAuctioneer(int numBidders, String bidderName, double bidAmount) {
        for (Auctioneers auctioneer : auctioneers) {

            System.out.println("Benachrichtigung für Auktionator: " + auctioneer);
            System.out.println("Anzahl der registrierten Bieter: " + numBidders);
            System.out.println("Bieter: " + bidderName + " hat ein Gebot von " + bidAmount + " Euro abgegeben.");
        }
    }

    public void notifyBidder(Products product, double currentPrice, double budget) {
        for (Bidders bidder : bidders) {
            System.out.println("Benachrichtigung für Bieter: " + bidder);
            System.out.println("Auktion Details: Produkt - " + product + ", Aktueller Preis - " + currentPrice + " Euro.");
        }
    }
}
