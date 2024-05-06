import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static Auction currentAuction;

    public static Auction getCurrentAuction() {
        return currentAuction;
    }

    public static void setCurrentAuction(Auction auction) {
        currentAuction = auction;
    }

    public static void main(String[] args) throws FileNotFoundException, NoSuchElementException {
        // Create a communicator
        Communicator communicator = new Communicator();

        // Create an auction house
        AuctionHouse auctionHouse = new AuctionHouse();

        // Add Interests for Bidders to List
        Create.setListInterests();

        // Create auctioneers
        Auctioneers auctioneer1 = new Auctioneers(auctionHouse);
        Auctioneers auctioneer2 = new Auctioneers(auctionHouse);

        // Register auctioneers
        communicator.registerAuctioneer(auctioneer1);
        communicator.registerAuctioneer(auctioneer2);

        // Create products
        setProd();

        // Register products
        auctioneer1.registerProduct(prodToAuctioneer());
        auctioneer2.registerProduct(prodToAuctioneer());

        // Create threads for starting auctions
        Thread auctionThread1 = new Thread(() -> auctioneer1.startAuctions());
        Thread auctionThread2 = new Thread(() -> auctioneer2.startAuctions());

        // Start auction threads
        auctionThread1.setName("A1");
        auctionThread1.start();
        auctionThread2.setName("A2");
        auctionThread2.start();

        try {
            auctionThread1.join();
            auctionThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Create and start bidders
        Bidders bidder1 = Create.createBidder(); // 
        Bidders bidder2 = Create.createBidder(); // Bidder with 1000 euros budget and non-aggressive behavior

        Thread bidderThread1 = new Thread(bidder1);
        Thread bidderThread2 = new Thread(bidder2);
        bidderThread1.setName("B1");
        bidderThread2.setName("B2");

        // Start bidder threads
        bidderThread1.start();
        bidderThread2.start();
    }

    /**
     * Liest Produkte aus Products.txt aus
     * @throws FileNotFoundException
     * @throws NoSuchElementException
     */
     private static void setProd() throws FileNotFoundException, NoSuchElementException {
        File file = new File("src/Products.txt");
        Scanner prodfile = new Scanner(file);

        try {
            while (prodfile.hasNext()) {
                prodfile.nextLine();
                String name = prodfile.next(); // Datei besteht aus: Name des Produkts
                String type = prodfile.next(); // Produkttyp
                String price = prodfile.next();// Startpreis
                String step = prodfile.next(); // Preisschritte
                String end = prodfile.next();  // Mindestpreis
                                            // Produkte werden mit '-' voneinander getrennt
                Products t = new Products(name, type, Double.parseDouble(price), Integer.parseInt(step), Double.parseDouble(end));

                prodfile.nextLine();

            }
        } catch (NoSuchElementException e) {
            System.out.println("All Products added");
            prodfile.close();
        }
    }

    //Auctioneers randomly pick a product to sell
    private static Products prodToAuctioneer(){
        Random rand = new Random();
        boolean alreadyInAuction = false;
        Products t=null;
        int i=0;

        while(!alreadyInAuction) {
            i++;
            t = Products.getItem(rand.nextInt(0,Products.getItemAmount()-1));
            alreadyInAuction=t.getInAuction();
            if (i==Products.getItemAmount());{
                break;
            }
    }
    return t;

    }

}


    



