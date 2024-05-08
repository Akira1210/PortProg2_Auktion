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

            System.out.println("Notification for Auctioneer: " + auctioneer);
            System.out.println("Number of bidders registered: " + numBidders);
            System.out.println("Bidder: " + bidderName + " placed a bid of " + bidAmount + " euros.");
        }
    }

    public void notifyBidder(Products product, double currentPrice, double budget) {
        for (Bidders bidder : bidders) {
            System.out.println("Notification for Bidder: " + bidder);
            System.out.println("Auction details: Product - " + product + ", Current price - " + currentPrice + " euros.");
        }
    }
}
