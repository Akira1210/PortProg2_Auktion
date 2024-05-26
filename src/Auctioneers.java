import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  Klasse, die Auktionatoren als Objekte definiert
 *  Enthält Produktliste, Kommunikator und Auktionshaus
 */

public class Auctioneers {
    private AuctionHouse auctionHouse;
    private List<Products> products;
    private Communicator comm;

    /**
     * 
     * @param auctionHouse Auktionshaus
     */
    public Auctioneers(AuctionHouse auctionHouse) {
        this.auctionHouse = auctionHouse;
        this.products = new ArrayList<>();
        this.comm = new Communicator();
        this.comm.registerAuctioneer(this);
    }

    /**
     * Produkt wird Produktliste hinzugefügt
     * @param product
     */
    public void registerProduct(Products product) {
        this.products.add(product);
    }

    /**
     * Produkte für Auktionen registrieren
     * Startet Auktionen
     */
    public void startAuctions() {
        ExecutorService executor = Executors.newFixedThreadPool(products.size());
        for (Products product : this.products) {

            executor.submit(() -> auctionHouse.createAuction(product,this.comm));
        }
        executor.shutdown();
    }
}


