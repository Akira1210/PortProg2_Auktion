public class Main {
    private static Auction currentAuction;

    public static Auction getCurrentAuction() {
        return currentAuction;
    }

    public static void setCurrentAuction(Auction auction) {
        currentAuction = auction;
    }

    public static void main(String[] args) {
        // Create a communicator
        Communicator communicator = new Communicator();

        // Create an auction house
        AuctionHouse auctionHouse = new AuctionHouse();

        // Create auctioneers
        Auctioneers auctioneer1 = new Auctioneers(auctionHouse);
        Auctioneers auctioneer2 = new Auctioneers(auctionHouse);

        // Register auctioneers
        communicator.registerAuctioneer(auctioneer1);
        communicator.registerAuctioneer(auctioneer2);

        // Create products
        Products product1 = new Products(125, 25, 25);
        Products product2 = new Products(250, 10, 180);

        // Register products
        auctioneer1.registerProduct(product1);
        auctioneer2.registerProduct(product2);

        // Create threads for starting auctions
        Thread auctionThread1 = new Thread(() -> auctioneer1.startAuctions());
        Thread auctionThread2 = new Thread(() -> auctioneer2.startAuctions());

        // Start auction threads
        auctionThread1.start();
        auctionThread2.start();

        try {
            auctionThread1.join();
            auctionThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Create and start bidders
        Bidders bidder1 = new Bidders(500, true); // Bidder with 500 euros budget and aggressive behavior
        Bidders bidder2 = new Bidders(400, false); // Bidder with 1000 euros budget and non-aggressive behavior

        Thread bidderThread1 = new Thread(bidder1);
        Thread bidderThread2 = new Thread(bidder2);

        // Start bidder threads
        bidderThread1.start();
        bidderThread2.start();
    }}



