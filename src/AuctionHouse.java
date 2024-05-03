import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AuctionHouse {
    private List<Auction> auctions;

    public AuctionHouse() {
        this.auctions = new ArrayList<>();
    }

    public synchronized void createAuction(Products product) {
        Auction auction = new Auction(product);
        auctions.add(auction);
        Main.setCurrentAuction(auction); // Set the current auction
        Thread thread = new Thread(auction);
        thread.start(); // Start the auction in a new thread
    }

    public synchronized void endAuction(Auction auction) {
        auctions.remove(auction);
    }

    public List<Auction> getAuctions() {
        return auctions;
    }
}