/**
 * Klasse, die Auktion als Objekte definiert
 * Auktions Objekte enthalten Produkte, Kommunikator, der aktuelle Preis und eine Boolean Variable, 
 * welche Angibt, ob die Auktion noch läuft oder bereits beendet wurde
 */

public class Auction implements Runnable {
    private Products product;
    private double currentPrice;
    private boolean auctionEnded;
    private Communicator comm;



    /**
     * Konstruktor für Auktionen
     * @param product   Produkt Objekt
     * @param comm      Kommunikator
     */
    public Auction(Products product, Communicator comm) {
        this.product = product;
        this.currentPrice = product.getStartingPrice();
        this.auctionEnded = false;
        this.comm = comm;

    }

    /**
     * Wird von Kommunikator aufgerufen, wenn Bieter ein Gebot abgit
     * @param amount    Gebotspreis
     */
    public void bid(double amount) {
            if (!auctionEnded && amount >= currentPrice) {         
                                                                    //BieterNr: rechnet sich aus AuktionatorenThreads + Nr. von diesem 
                                                                    //BieterThread, deshalb (Nr. von diesem BieterThread - Anzahl Auktionatoren) = Tatsächliche Bieter Nr.  
                System.out.println("Bieter " + (Integer.parseInt(Thread.currentThread().getName().replaceAll("pool-1-thread-", ""))-Main.getNumAuctioneers()) 
                + " hat ein Gebot von " + amount + " Euro für " + Products.getItemName(product) + " abgegeben.");
                currentPrice = amount;
                auctionEnded = true;
            }
    }

    @Override
    public void run() {
            while (!auctionEnded && currentPrice > product.getMinimalPrice()) {
                currentPrice -= product.getDecrementPrice();
                comm.toBidd(currentPrice); //new
                System.out.println("Der aktuelle Preis für " + Products.getItemName(product) + " liegt bei: " + currentPrice + " Euro.");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if (currentPrice <= product.getMinimalPrice()) {
                auctionEnded = true;
                System.out.println("Auktion beendet. Der Mindestpreis für " + Products.getItemName(product) + " von: " + product.getMinimalPrice() + " Euro wurde erreicht.");
        }
    }

    // GETTERS

    public boolean isAuctionEnded() {
        return auctionEnded;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }
    public Products getProduct() {
        return product;
    }

    public boolean isRunning() {
        return !auctionEnded;
    }

    public Communicator getComm() {
        return this.comm;
    }
}