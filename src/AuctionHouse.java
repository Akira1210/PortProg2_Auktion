import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Klasse, die Auktionshäuser als Objekte definiert
 * Erstellung erfolgt ohne Argumente bzw. mit leerem Konstruktor
 */
public class AuctionHouse {
    private static List<Auction> auctions;

    public AuctionHouse() {
        auctions = new ArrayList<>();       //Liste aller Auktionen
    }

    /**
     * Erstellt Auction Objekte
     * @param product   Produkt für die Auktion
     * @param comm      Kommunikator für diese Auktion
     */
    public void createAuction(Products product, Communicator comm) {
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

    /**
     * Beendet angegebene Auktion
     * @param auction
     */
    public void endAuction(Auction auction) {
        auctions.remove(auction);
    }

    // GETTERS

    public static List<Auction> getAuctions() {
        return auctions;
    }
}