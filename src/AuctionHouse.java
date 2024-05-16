import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuctionHouse {
    private static List<Auction> auctions;



    public AuctionHouse() {
        this.auctions = new ArrayList<>();
    }

    public synchronized void createAuction(Products product) {
        Auction auction = new Auction(product);
        auctions.add(auction);
        Main.setCurrentAuction(auction); // Set the current auction
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(auction);
        executor.shutdown();
    }

    public synchronized void endAuction(Auction auction) {
        auctions.remove(auction);
    }

    public synchronized static List<Auction> getAuctions() {
        return new ArrayList<>(auctions);
    }
}