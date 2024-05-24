import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class Tests {
    private Products product;
    private Auction auction;
    private AuctionHouse auctionHouse;
    private Bidders bidder;
    private Communicator comm;

    @BeforeEach
    public void setUp() {
        auctionHouse = new AuctionHouse();
        comm = new Communicator();
        product = new Products("Test Produkt", "Test Typ", 1000.0, 100.0, 500.0);
        auction = new Auction(product, comm);
    }

    @Test
    public void testPriceDecrement() throws InterruptedException {
        Thread auctionThread = new Thread(auction);
        auctionThread.start();
        Thread.sleep(100); // Warten, um Preisabnahme zu ermöglichen
        assertTrue(auction.getCurrentPrice() < 1000.0);
        auctionThread.interrupt(); // Stoppen des Threads nach dem Test
    }

    @Test
    public void testAuctionEndsAtMinimalPrice() throws InterruptedException {
        Thread auctionThread = new Thread(auction);
        auctionThread.start();

        // Warten bis die Auktion beendet ist
        while (auctionThread.isAlive()) {
            Thread.sleep(100); // Kurze Wartezeiten, um CPU-Zyklen zu sparen
        }

        // Prüfungen durchführen nachdem der Thread sicher beendet ist
        assertEquals(product.getMinimalPrice(), auction.getCurrentPrice());
        assertTrue(auction.isAuctionEnded());
    }
    @Test
    public void testCreateAuction() {
        // Erstelle eine Auktion mit dem gegebenen Produkt
        auctionHouse.createAuction(product, comm);

        // Überprüfe, ob die Auktion zur Liste hinzugefügt wurde
        List<Auction> auctions = AuctionHouse.getAuctions();
        assertFalse(auctions.isEmpty(), "Die Auktionsliste sollte nicht leer sein, nachdem eine Auktion hinzugefügt wurde");
        Auction createdAuction = auctions.get(auctions.size() - 1);
        assertEquals(product, createdAuction.getProduct(), "Das Produkt in der erstellten Auktion sollte mit dem bereitgestellten übereinstimmen");
        assertNotNull(createdAuction, "Die erstellte Auktion sollte nicht null sein");
    }
}
