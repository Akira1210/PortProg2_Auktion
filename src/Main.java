import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Entry Point Klasse, die alle nötigen Objekte initialisiert und Grundbausteine für die Anwendung bereitstellt
 */
public class Main {
    private static int numAuctioneers;                  //Anzahl der Auktionatoren
    private static int numBidders;                      //Anzahl der Bieter
    private static boolean allAuctionsAdded=false;      //Boolean, ob alle Auktionen erstellt wurde und die Anwendung fortgesetzt werden kann

    public static void main(String[] args) throws FileNotFoundException, NoSuchElementException, NumberFormatException {

        setProd();  // Produkte werden eingelesen

        Scanner input = new Scanner(System.in);     //Scanner Objekt, um Nutzereingaben zu ermöglichen

        //Eingabe der Auktionatoren-Anzahl
        System.out.println("\nBitte geben Sie die Anzahl der Auktionatoren an:");
        try {
            numAuctioneers = input.nextInt();   //Überprüft, ob Eingabe eine Zahl ist, sonst HandleInputMismatchException unten
            if (numAuctioneers < 1) {           //Überprüft, ob die Eingabe größer Eins ist
                NumberFormatException();
            }
        } catch (InputMismatchException eA) {
            HandleInputMismatchException(eA);   //Wird aufgerufen, wenn Eingabe keine Zahl war
        }
        
        //Sicherstellellen, dass genug Produkte für die Auktionatoren zur Verfügung stehen
        if (numAuctioneers>Products.getItemAmount()) {
            System.out.println("Es stehen nicht genug Produkte für die Auktionatoren zur Verfügung");
            numAuctioneers=Products.getItemAmount();    //Falls nicht, wird die Anzahl der Auktionatoren auf die Anzahl der Produkte reduziert
            System.out.println("Die Anzahl der Auktionaoren wurde auf die Anzahl der Verfügbaren Produkte beschränkt ("+numAuctioneers+" Auktionatoren).");
        }

        //Eingabe der Bieter-Anzahl
        System.out.println("\nBitte geben Sie die Anzahl der Bieter an:");
        try {
            numBidders = input.nextInt();       //Überprüft, ob Eingabe eine Zahl ist, sonst HandleInputMismatchException unten
            if (numBidders < 1) {               //Überprüft, ob die Eingabe größer Eins ist     
                NumberFormatException();        
            }
        } catch (InputMismatchException eB) {
            HandleInputMismatchException(eB);   //Wird aufgerufen, wenn Eingabe keine Zahl war
        }

        input.close();  //Schließt Scanner Objekt, da keine Nutzereingaben mehr erforderlich sind

        System.out.println("\n");


        // Erstellung des Auktionshauses
        AuctionHouse auctionHouse = new AuctionHouse();

        // Interessen für Bieter werden in eine Liste geladen
        Create.setListInterests();

        // Auktionatoren erstellen
        List<Auctioneers> auctioneers = new ArrayList<>();
        for (int i = 0; i < numAuctioneers; i++) {
            Auctioneers t = new Auctioneers(auctionHouse);
            t.registerProduct(prodToAuctioneer()); // Produkte registrieren
            auctioneers.add(t);
        }

        // Bieter erstellen
        List<Bidders> bidders = new ArrayList<>();
        for (int i = 0; i < numBidders; i++) {
            Bidders t = Create.createBidder();
            bidders.add(t);
        }

        //Berechnung der Anzahl der nötigen Threads
        int numThreads = calculateNumThreads();

        // Erstellung des ThreadPoolExecutor
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                numThreads,                     // corePoolSize
                numThreads,                     // maximumPoolSize
                100L,             // keepAliveTime
                TimeUnit.MILLISECONDS,          // unit
                new LinkedBlockingQueue<>()     // workQueue
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

            for (Auction auction : AuctionHouse.getAuctions()) {
                bidders.get(i).registerForAuction(auction);
            }
            executorService.submit(() -> bidders.get(index).run()); //Startet Bieter-Threads
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
     * @throws FileNotFoundException    Wird geworfen, wenn Products.txt nicht gefunden wird
     * @throws NoSuchElementException   Wird geworfen, wenn alle Produkte eingelsen wurden (Nächste Zeile in txt existiert nicht). Programm wird fortgesetzt
     */
    private static void setProd() throws FileNotFoundException, NoSuchElementException {
        File file = new File("src/Products.txt");
        Scanner prodfile = null;
        try {
        prodfile = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            FileNotFoundException(e);
        }

        try {
            while (prodfile.hasNext()) {
                prodfile.nextLine();
                String name = prodfile.next();  // Datei besteht aus: Name des Produkts
                String type = prodfile.next();  // Produkttyp
                String price = prodfile.next(); // Startpreis
                String step = prodfile.next();  // Preisschritte
                String end = prodfile.next();   // Mindestpreis
                                                // Produkte werden mit '-' voneinander getrennt
                try {Products t = new Products(name, type, Double.parseDouble(price), Integer.parseInt(step),
                        Double.parseDouble(end));   //Erstellt Produkt Objekt
                }
                catch (Exception e){
                    ProductIllegalArgument(e);  //Wird geworfen, wenn Produkt Konstruktor ungültiges Argument erhält, weist auf eine falsche Formattierung der txt hin
                }

                prodfile.nextLine();

            }
        } 
        catch (NoSuchElementException e) {
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
            num = i; //Falls Zufallsprodukt bereits in einer Auktion ist, wird Produktliste von vorne bis hinten nach freien Produkten linear durchsucht
            if (num == Products.getItemAmount()) //Falls alle Produkte in einer Auktion bereits zu finden sind (Eigentlich Unreachable Code, da nach Eingabe der Anzahl der Auktionaotren bereits sichergestellt wird, dass genug Produkte verfügbar sind)
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

    public static Auction getAuctionForBidder(Auction regauction) {
            for (Auction plannedAuction : AuctionHouse.getAuctions()) {
                if (plannedAuction.equals(regauction)&plannedAuction.isRunning()) {
                    return plannedAuction;
                }
            }
        return null;
    }


    //EXCEPTION HANDELING
    private static void HandleInputMismatchException(InputMismatchException e) {
        System.out.println(e + ": Die Eingabe muss eine Ganzzahl sein. Programm wird beendet");
        System.exit(-1);
    }

    private static void NumberFormatException() {
        System.out.println("Die Eingabe muss eine positive Ganzzahl größer 0 sein. Programm wird beendet");
        System.exit(-1);
    }

    private static void FileNotFoundException(FileNotFoundException e) {
        System.out.println(e+": Die Datei 'Products.txt' konnte nicht gefunden werden. Es können keine Produkte geladen werden. Programm wird beendet.");
        System.exit(-1);
    }

    private static void ProductIllegalArgument(Exception e){
        System.out.println(e+" Der Produkterstellung wurde ein ungültiges Argument übergeben. Eventuell ist die 'Products.txt' beschädigt. Programm wird beendet.");
        System.exit(-1);
    }

}
