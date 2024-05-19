import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuctionHouse {
    private static List<Auction> auctions;

    public AuctionHouse() {
        auctions = new ArrayList<>();       //Liste aller Auktionen
    }

    public synchronized void createAuction(Products product, Communicator comm) {
        Auction auction = new Auction(product, comm);
        Reporter.addProduct(product);
        auctions.add(auction);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(auction);
        executor.shutdown();
        if (auctions.size() == Main.getNumAuctioneers()) {
            Main.setAllAuctionsAdded(true);
        }
    }

    public synchronized void endAuction(Auction auction) {
        auctions.remove(auction);
    }

    public static synchronized List<Auction> getAuctions() {
        return auctions;
    }
}