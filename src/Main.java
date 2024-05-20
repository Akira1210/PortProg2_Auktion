import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.*;

public class Main {
    private static int numAuctioneers;
    private static int numBidders;
    private static boolean allAuctionsAdded=false;

    public static void main(String[] args) throws FileNotFoundException, NoSuchElementException, NumberFormatException {

        // Produkte werden eingelesen
        setProd();

        Scanner input = new Scanner(System.in);
        System.out.println("\nBitte geben Sie die Anzahl der Auktionatoren an:");
        try {
            numAuctioneers = input.nextInt();
            if (numAuctioneers < 1) {
                NumberFormatException();
            }
        } catch (InputMismatchException eA) {
            HandleInputMismatchException(eA);
        }
        //Sicherstellellen, dass genug Produkte für die Auktionatoren zur Verfügung stehen
        if (numAuctioneers>Products.getItemAmount()) {
            System.out.println("Es stehen nicht genug Produkte für die Auktionatoren zur Verfügung");
            numAuctioneers=Products.getItemAmount();    //Falls nicht, wird die Anzahl der Auktionatoren auf die Anzahl der Produkte reduziert
            System.out.println("Die Anzahl der Auktionaoren wurde auf die Anzahl der Verfügbaren Produkte beschränkt ("+numAuctioneers+" Auktionatoren).");
        }

        System.out.println("\nBitte geben Sie die Anzahl der Bieter an:");
        try {
            numBidders = input.nextInt();
            if (numBidders < 1) {
                NumberFormatException();
            }
        } catch (InputMismatchException eB) {
            HandleInputMismatchException(eB);
        }
        input.close();
        System.out.println("\n");

        // Erstellung des Auktionshauses
        AuctionHouse auctionHouse = new AuctionHouse();

        // Interessen für Bieter werden in eine Liste geladen
        Create.setListInterests();

        // Auktionatoren erstellen
        List<Auctioneers> auctioneers = new ArrayList<>();
        for (int i = 0; i < numAuctioneers; i++) {
            Auctioneers t = new Auctioneers(auctionHouse);
            t.registerProduct(prodToAuctioneer()); // Register products
            auctioneers.add(t);
        }

        // Bieter erstellen
        List<Bidders> bidders = new ArrayList<>();
        for (int i = 0; i < numBidders; i++) {
            Bidders t = Create.createBidder();
            bidders.add(t);
        }

        //Berechnung der Anazhl der nötigen Threads
        int numThreads = calculateNumThreads();

        // Erstellung des ThreadPoolExecutor
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                numThreads, // corePoolSize
                numThreads, // maximumPoolSize
                100L, // keepAliveTime
                TimeUnit.MILLISECONDS, // unit
                new LinkedBlockingQueue<>() // workQueue
        );

        // Start Auktionatoren Threads
        for (Auctioneers auctioneer : auctioneers) {
            executorService.submit(() -> auctioneer.startAuctions());
            
        }

        // Warten, dass alle Auktionen in eine für die Bieter einsehbare Auktionsliste hinzugefügt wurden
        while (allAuctionsAdded==false) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Bieter werden für bestimmte Auktionen registriert
        for (int i = 0; i < numBidders; i++) {
           final int index = i;
           synchronized (AuctionHouse.getAuctions()) {}

            for (Auction auction : AuctionHouse.getAuctions()) {
                bidders.get(i).registerForAuction(auction);
            }
            bidders.get(i).setIndex(i);
            executorService.submit(() -> bidders.get(index).run());
        }

        // Warten auf die Beendigung aller Threads
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Wenn alle Auktionen beendet, wird EndReport ausgegeben
        while (Thread.activeCount()!=1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Reporter.writeAuctionInfo();
        Reporter.printEndReport();
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
                String name = prodfile.next();  // Datei besteht aus: Name des Produkts
                String type = prodfile.next();  // Produkttyp
                String price = prodfile.next(); // Startpreis
                String step = prodfile.next();  // Preisschritte
                String end = prodfile.next();   // Mindestpreis
                                                // Produkte werden mit '-' voneinander getrennt
                Products t = new Products(name, type, Double.parseDouble(price), Integer.parseInt(step),
                        Double.parseDouble(end));

                prodfile.nextLine();

            }
        } catch (NoSuchElementException e) {
            prodfile.close(); //Alle Produkte wurden eingelesen
        }
    }

    // Auktionatoren wählen zufällig Produkte zum Verkaufen
    private static Products prodToAuctioneer() {
        Random rand = new Random();
        Products prod = null;
        int i = -1;
        int num =rand.nextInt(0, Products.getItemAmount() - 1); //Erstmal Zufallszahl, um zufälliges Produkte aus Liste zu erhalten

        while (true) {
            i++;
            prod = Products.getItem(num);
            if (!prod.getInAuction()) { //Überprüfen, dass Produkt nicht bereits in einer Auktion ist
                Products.getItem(num).setInAuction(true);
                return prod;
            }
            num = i; //Falls Zufallsprodukt bereits in einer Auktion ist, wird Produktliste von vrone bis hinten nach freien Produkten linear durchsucht
            if (num == Products.getItemAmount()) //Falls alle Produkte in einer Auktion bereits zu finden sind (Eigentlich Unreachable Code, da nach Eingabe der Anzahl der AUktionaotren bereits sichergestellt wird, dass genug Produkte verfügbar sind)
            {
                System.out.println("Auktionator konnte kein freies Produkt finden...");
                break;
            }
            
        }
        return null;

    }

    public static int calculateNumThreads() {
        // Anzahl Auktionatoren Threads
        int numAuctioneerThreads = Math.max(numAuctioneers, 1);

        // Anzahl Bieter Threads
        int numBidderThreads = Math.max(numBidders,1);

        // Insgesamte Anzahl der nötigen Threads
        return numAuctioneerThreads + numBidderThreads;
    }

    //GETTERS / SETTERS
    public static int getNumAuctioneers(){
        return numAuctioneers;
    }

    public static int getNumBidders(){
        return numBidders;
    }

    public static void setAllAuctionsAdded(boolean allauctionsadded) {
        allAuctionsAdded = allauctionsadded;
    }

    public static Auction getAuctionForBidder(Set<Auction> registeredAuctions) {
        for (Auction regauction : registeredAuctions) {
            for (Auction plannedAuction : AuctionHouse.getAuctions()) {
                if (plannedAuction.equals(regauction)) {
                    return plannedAuction;
                }
            }
        }
        return null;
    }


    //EXCEPTION HANDELING
    private static void HandleInputMismatchException(InputMismatchException e) {
        System.out.println(e + ": Eingabe muss eine Ganzzahl sein");
        System.exit(-1);
    }

    private static void NumberFormatException() {
        System.out.println("Eingabe muss eine positive Ganzzahl sein");
        System.exit(-1);
    }

}
