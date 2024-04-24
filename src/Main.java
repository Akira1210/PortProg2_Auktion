import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * TODO: JavaDocs schreiben
 * TODO: Adressierung von laufenden Auktionen und deren Methoden fixen
 * TODO: evtl. nach abgeschlossenen Auktion, verbelibende Bieter auf noch laufende Auktionen verteilen
 * TODO: Verkauf des selben Produkts an mehrere Bieter fixen
 * TODO: Code-Cleanup und Auslagerung auf mehrere .java Files und Packages
 * TODO implement AuctionID !!!!!
 */
public class Main {
    static int numAuctioneers;
    static int numBidders;
    static int numAuctions;
    static ArrayList<Integer> prodUsed= new ArrayList<>();

    private static void HandleInputMismatchException(InputMismatchException e) {
        System.out.println(e + ": Eingabe muss eine Ganzzahl sein");
        System.exit(-1);
    }

    private static void setProd() throws FileNotFoundException, NoSuchElementException {
        File file = new File("src/Products.txt");
        Scanner prodfile = new Scanner(file);

        try {
            while (prodfile.hasNext()) {
                prodfile.nextLine();
                var name = prodfile.next(); // Datei besteht aus: Name des Produkts
                var type = prodfile.next(); // Produkttyp
                var price = prodfile.next();// Startpreis
                var step = prodfile.next(); // Preisschritte
                var end = prodfile.next();  // Mindestpreis
                                            // Produkte werden mit '-' voneinander getrennt
                Products t = new Products(name, type, Double.parseDouble(price), Integer.parseInt(step), Double.parseDouble(end));
                prodfile.nextLine();

            }
        } catch (NoSuchElementException e) {
            System.out.println("Alle Produkte hinzugefügt");
            prodfile.close();
        }
    }

    public static void main(String[] args) throws InputMismatchException {
        Create.setListInterests();
        try {
            setProd();
        } catch (FileNotFoundException e) {
            System.out.println(e + ": Products.txt nicht gefunden");
            System.exit(-1);
        }
        Scanner input = new Scanner(System.in);


        System.out.println("Bitte geben Sie die Anzahl der Auktionen an:");
        try {
            numAuctions = input.nextInt();
            if (numAuctions>Products.getProdListSize()) {
                System.out.println("Es stehen nicht genug Produkte zur Verfügung, um die angegebene Anzahl von Auktionen durchzuführen."+
                " Es werden nur " + Products.getProdListSize() + " Auktionen durchgeführt.");
                numAuctions = Products.getProdListSize();
            }
        } catch (InputMismatchException eAu) {
            HandleInputMismatchException(eAu);
    
        }

        System.out.println("Bitte geben Sie die Anzahl der Auktionatoren an:");
        try {
            numAuctioneers = input.nextInt();
        } catch (InputMismatchException eA) {
            HandleInputMismatchException(eA);
        }

        System.out.println("Bitte geben Sie die Anzahl der Bieter an:");
        try {
            numBidders = input.nextInt();
        } catch (InputMismatchException eB) {
            HandleInputMismatchException(eB);
        }




        input.close();
        



        Create.CreateNum(1,numAuctioneers);
        Create.CreateNum(0, numBidders);
        
        if (Products.ProdList.size()<numAuctioneers) {
            System.out.println("(!Hinweis!) Es stehen weniger Produkte als Auktionatoren zur Verfügung. Nicht jeder Auktionator wird etwas verkaufen können.");
        }
        BiddersToAuction();

         //Bieter werden Auktionen per Zufall zugeordnet !!!! WENN AUKTIONATOREN>PRODUKTE, DANN WIRD BIDDERSTOAUCTION NICHT AUSGEFÜHRT

    }
    private static void BiddersToAuction(){
        Random rand = new Random();
        for (int i=0; i< numBidders; i++) {
            int randRes= rand.nextInt(0,numAuctions);
            Create.ListBidders.get(i).setAttend(randRes);
            System.out.println(Create.ListBidders.get(i).getName()+" nimmt an Auktion " + (Create.ListBidders.get(i).getAttend()+1)+" teil.");

        }


    }
}
