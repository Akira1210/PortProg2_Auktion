import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;


public class Main {
    static int numAuctioneers;
    static int numBidders;
    static int numAuctions;
    static ArrayList<Integer> prodUsed= new ArrayList<>();
    static ArrayList<String> resultAuc = new ArrayList<>();

    /**
     * Behandelt falsche Eingaben, wenn Anzahl von Auktion, Auktionatoren und Bietern eingegeben werden
     * @param e Variable für hier behandelte Ausnahme
     */
    private static void HandleInputMismatchException(InputMismatchException e) {
        System.out.println(e + ": Eingabe muss eine Ganzzahl sein");
        System.exit(-1);
    }
/**
 * Behandelt negative Eingaben, wenn Anzahl von Auktion, Auktionatoren und Bietern eingegeben werden
 */
    private static void NumberFormatException() {
        System.out.println("Eingabe muss eine positive Ganzzahl sein");
        System.exit(-1);
    }

    /**
     * Liest Produktdaten aus 'Products.txt'
     * @throws FileNotFoundException wird ausgelöst, wenn 'Products.txt' nicht existiert
     * @throws NoSuchElementException wird ausgelöst, wenn  versucht wird eine nicht vorhandene Zeile in 'Products.txt' zu lesen. Behandlung schließt Einlesung von Produkten ab.
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
            System.out.println("Alle Produkte hinzugefügt");
            prodfile.close();
        }
    }

    /**
     * Startet Auktions-Simulation
     * Erfragt Eingaben zur Anzahl von Auktionen, Auktionatoren und Bieter vom Nutzer
     * @param args NotImplemented
     * @throws InputMismatchException wird ausgelöst, wenn Eingaben nicht ausschließlich positive Ganzahlen sind
     */
    public static void main(String[] args) throws InputMismatchException, NumberFormatException {
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
            if (numAuctions<1) {NumberFormatException();}
        } catch (InputMismatchException eAu) {
            HandleInputMismatchException(eAu);
    
        }

        System.out.println("Bitte geben Sie die Anzahl der Auktionatoren an:");
        try {
            numAuctioneers = input.nextInt();
            if (numAuctions<numAuctioneers) {
                String t="";
                String s="";
                if (numAuctions==1) {t = " Auktionator ";s=" wird ";}
                if (numAuctions>1) {t = " Auktionatoren ";s=" werden ";}
                System.out.println("(!Hinweis!) Es nehmen mehr Auktionatoren teil, als Auktionen durchgeführt werden. Es" + s + "nur "+numAuctions+ t + "eine Auktion durchführen.");
                numAuctioneers=numAuctions;
                if (numAuctioneers<1) {NumberFormatException();}
            }
        } catch (InputMismatchException eA) {
            HandleInputMismatchException(eA);
        }

        System.out.println("Bitte geben Sie die Anzahl der Bieter an:");
        try {
            numBidders = input.nextInt();
            if (numBidders<1) {NumberFormatException();}
        } catch (InputMismatchException eB) {
            HandleInputMismatchException(eB);
        }



        input.close();
        Create.CreateNum(1,numAuctioneers);
        Create.CreateNum(0, numBidders);
        

        BiddersToAuction();
        for (String res : resultAuc) {
            System.out.println(res);
        }


    }

    /**
     * Teilt zufällig Bieter auf Auktionen auf
     */
    private static void BiddersToAuction(){
        Random rand = new Random();
        for (int i=0; i< numBidders; i++) {
            int randRes= rand.nextInt(0,numAuctioneers);
            Create.ListBidders.get(i).setAttend(randRes);
            System.out.println("Bidder "+(i+1)+" nimmt an Auktion " + (Create.ListBidders.get(i).getAttend()+1)+" teil, hat besonderes Interesse an " + Create.ListBidders.get(i).getInterests()+" und hat ein Budget von "+ Create.ListBidders.get(i).getMoney()+" €.");

        }


    }
}
