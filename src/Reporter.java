import java.util.ArrayList;

/**
 * Klasse, die den Schlussbericht erstellt. Reporter schreibt während der Auktion mit
 * Enthält Liste der Produkte die zu verkauf stehen, Liste der Produkte die verkauft wurden und Informationen zu den AUktionen
 */
public class Reporter {
    private static ArrayList<String> ItemsOnSale = new ArrayList<>();          //Auflistung der Waren, die zur Auktion stehen
    private static ArrayList<Auction> BoughtItems = new ArrayList<>();         //Auflistung der Waren, die verkauft wurden
    private static String AuctionInfo = "";                                    //Auktionsinfos, wie Anzahl der Auktionen, Auktionatoren und Bietern
    private static String AuctionStatistics = "";                              //Endergebnis hinsichtlich Umsatzes, Provision und Anzahl verkaufter Produkte

    /**
     * Produkt wird der Liste aller Produkte hinzugefügt
     * @param product
     */
    public static void addProduct(Products product) {
        ItemsOnSale.add(Products.getItemName(product));
    }

    /**
     * Produkt wird der Liste der verkauften Produkte hinzugefügt
     * @param product
     */
    public static void addBoughtItems(Auction product) {
        BoughtItems.add(product);
    }

    /**
     * Auktionsinfo werden zu einem leserlichem String zusammengefasst
     */
    public static void writeAuctionInfo() {
        if (Main.getNumAuctioneers()<2) {
            AuctionInfo="Es wurde eine Auktion durchgeführt. Insgesamt ";
        }
        if (Main.getNumAuctioneers()>1) {
            AuctionInfo="Es wurden "+Main.getNumAuctioneers()+" Auktionen durchgeführt. Insgesamt ";
        }
        if (Main.getNumBidders()<2) {
            AuctionInfo+="hat ein";
        }
        if (Main.getNumBidders()>1) {
            AuctionInfo+="haben "+Main.getNumBidders();
        }
        AuctionInfo+=" Bieter an diesem Auktiontag teilgenommen.";
    }

    /**
     * Berechnungen für verschiedene für die Auktionen relevanter Zahlen
     * Zusammenfassung in einem leserlichem String
     */
    public static void calcAuctionInfo(){
        double profit=0;
        String auc ="";
        for (Auction item: BoughtItems) {
            profit+=item.getCurrentPrice();
        }
        if (BoughtItems.size()<1) {
            AuctionStatistics="Es wurden keine Produkte verkauft ";
        }
        if (BoughtItems.size()==1) {
            AuctionStatistics="Es wurde ein Produkt verkauft "; 

        }
        if (BoughtItems.size()>1) {
            AuctionStatistics="Es wurden " + BoughtItems.size() + " Produkte verkauft ";
        }
        AuctionStatistics+="und ein Umsatz von " + profit + " Euro erzielt." +
        "\nDie Provision für das Auktionshaus liegt bei 1%, wodurch für ";
        if (Main.getNumAuctioneers()<2) {
            auc="den Auktionator ";
        }
        if (Main.getNumAuctioneers()>1) {
            auc="die Auktionatoren ";
        }
        AuctionStatistics+=auc+"eine Provision von insgesamt " + (Math.round(profit*1)/100d) + " Euro erzielt wurde.";

        if (BoughtItems.size()<1) {
            AuctionStatistics="Es wurden keine Produkte verkauft, weshalb kein Umsatz und keine Provision für "+auc+"erzielt wurde.";
        }
    }

    /**
     * Wird als letztes von der ANwendung aufgerufen
     * Gibt alle in dieser Klasse zusammengesammelte Informationen aus
     */
    public static void printEndReport(){
        calcAuctionInfo();
        System.out.println("\nAlle Auktionen wurden beendet.");
        System.out.println("\nFolgende Produkte standen zum Verkauf:");
        for (String item: ItemsOnSale) {
            System.out.println(" -"+item);
        }
        if (BoughtItems.size()>0) {
        System.out.println("\nFolgende Produkte wurden verkauft:");
        for (Auction item:BoughtItems) {
            System.out.println(" -" + Products.getItemName(item.getProduct())+" wurde für "+item.getCurrentPrice()+" Euro verkauft.");}
        }
        System.out.println("\nEinige Informationen zur Auktion:");
        System.out.println(AuctionInfo);
        System.out.println(AuctionStatistics);
    }

}
