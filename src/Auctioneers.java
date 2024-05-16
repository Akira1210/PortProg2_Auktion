import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Auctioneers {
    private AuctionHouse auctionHouse;
    private List<Products> products;

    public Auctioneers(AuctionHouse auctionHouse) {
        this.auctionHouse = auctionHouse;
        this.products = new ArrayList<>();
    }

    public void registerProduct(Products product) {
        this.products.add(product);
    }
    public void startAuctions() {
        ExecutorService executor = Executors.newFixedThreadPool(products.size());
        for (Products product : this.products) {

            executor.submit(() -> auctionHouse.createAuction(product));
        }
        executor.shutdown();
    }
}


