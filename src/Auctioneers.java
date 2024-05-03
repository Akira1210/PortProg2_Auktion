import java.util.ArrayList;
import java.util.List;

public class Auctioneers {
    private AuctionHouse auctionHouse;
    private List<Products> products;

    public Auctioneers(AuctionHouse auctionHouse) {
        this.auctionHouse = auctionHouse;
        this.products = new ArrayList<>();
    }

    public void registerProduct(Products product) {
        products.add(product);
    }

    public void startAuctions() {
        for (Products product : products) {
            auctionHouse.createAuction(product);
        }
    }
}

