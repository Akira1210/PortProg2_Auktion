import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList; import java.util.InputMismatchException;
import java.util.List; import java.util.NoSuchElementException;
import java.util.Random; import java.util.Scanner;
import java.util.concurrent.*;


public class Main {
    private static Auction currentAuction;
    private static int numAuctioneers;
    private static int numAuctions;
    private static int numBidders;

    public static Auction getCurrentAuction() {
        return currentAuction;
    }

    public static void setCurrentAuction(Auction auction) {
        currentAuction = auction;
    }

    public static int calculateNumThreads() {
        // Number of auctioneer threads
        int numAuctioneerThreads = Math.max(numAuctioneers, 1);

        // Maximum number of concurrent bids per bidder
        int maxBidsPerBidder = 1; // Each bidder can bid in every auction

        // Total number of bidder threads
        int numBidderThreads = Math.max(numBidders * maxBidsPerBidder, 1);

        // Total number of threads
        return numAuctioneerThreads + numBidderThreads;
    }

    public static void main(String[] args) throws FileNotFoundException, NoSuchElementException, NumberFormatException {

        // Create products
        setProd();

        //TODO Sysin for Number of Bidders and Auctions
        Scanner input = new Scanner(System.in);
        int numAuctions = 0;
        int numAuctioneers = 0;
        int numBidders = 0;

        System.out.println("Bitte geben Sie die Anzahl der Auktionatoren an:");
        try {
            numAuctioneers = input.nextInt();
            numAuctions = numAuctioneers;
            if (numAuctioneers < 1) {
                NumberFormatException();
            }
        } catch (InputMismatchException eA) {
            HandleInputMismatchException(eA);
        }

        System.out.println("Bitte geben Sie die Anzahl der Bieter an:");
        try {
            numBidders = input.nextInt();
            if (numBidders < 1) {
                NumberFormatException();
            }
        } catch (InputMismatchException eB) {
            HandleInputMismatchException(eB);
        }
        input.close();

        //End Sysin

        // Create a communicator
        Communicator communicator = new Communicator();

        // Create an auction house
        AuctionHouse auctionHouse = new AuctionHouse();

        // Add Interests for Bidders to List
        Create.setListInterests();

        // Create auctioneers
        List<Auctioneers> auctioneers = new ArrayList<>();
        for (int i = 0; i < numAuctioneers; i++) {
            Auctioneers t = new Auctioneers(auctionHouse);
            communicator.registerAuctioneer(t);                 //Register auctioneers
            t.registerProduct(prodToAuctioneer());              //Register products
            auctioneers.add(t);
        }

        // Create and start bidders
        List<Bidders> bidders = new ArrayList<>();
        for (int i = 0; i < numBidders; i++) {
            Bidders t = Create.createBidder();
            bidders.add(t);
        }

        int numThreads = calculateNumThreads();

        // Create ThreadPoolExecutor with custom settings
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                numThreads, // corePoolSize
                numThreads, // maximumPoolSize
                0L, // keepAliveTime
                TimeUnit.MILLISECONDS, // unit
                new LinkedBlockingQueue<>() // workQueue
        );

        for (int i = 0; i < numBidders; i++) {
            final int index = i;
            executorService.submit(() -> {
                Bidders bidder = bidders.get(index);
                bidder.setCommunicator(communicator);
                bidder.setIndex(index);
                bidder.run();
            });
        }

        // Start auction threads
        for (Auctioneers auctioneer : auctioneers) {
            executorService.submit(() -> auctioneer.startAuctions());
        }

        // Wait for all threads to finish
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void HandleInputMismatchException(InputMismatchException e) {
        System.out.println(e + ": Eingabe muss eine Ganzzahl sein");
        System.exit(-1);
    }

    private static void NumberFormatException() {
        System.out.println("Eingabe muss eine positive Ganzzahl sein");
        System.exit(-1);
    }

    /**
     * Liest Produkte ausProducts.txt aus* @throws FileNotFoundException
     *
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
    private static Products prodToAuctioneer() {
        Random rand = new Random();
        boolean alreadyInAuction = false;
        Products t = null;
        int i = 0;

        while (!alreadyInAuction) {
            i++;
            t = Products.getItem(rand.nextInt(0, Products.getItemAmount() - 1));
            alreadyInAuction = t.getInAuction();
            if (i == Products.getItemAmount()) ;
            {
                break;
            }
        }
        return t;

    }

}

