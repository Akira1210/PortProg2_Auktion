import java.util.ArrayList;

public class Reporter {
    private static ArrayList<String> ItemsOnSale = new ArrayList<>();          //Auflistung der Waren, die zur Auktion stehen
    private static ArrayList<Auction> BoughtItems = new ArrayList<>();         //Auflistung der Waren, die verkauft wurden
    private static String AuctionInfo = "";                                    //Auktionsinfos, wie Anzahl der Auktionen, Auktionatoren und Bietern
    private static String AuctionStatistics = "";                              //Endergebnis hinsichtlich Umsatzes, Provision und Anzahl verkaufter Produkte

    public static void addProduct(Products product) {
        ItemsOnSale.add(Products.getItemName(product));
    }

    public static void addBoughtItems(Auction product) {
        BoughtItems.add(product);
    }

    public static void writeAuctionInfo(String info) {
        AuctionInfo+=info;
    }

    public static void calcAuctionInfo(){
        double profit=0;
        for (Auction item: BoughtItems) {
            profit+=item.getCurrentPrice();
        }
        AuctionStatistics="Es wurden " + BoughtItems.size() + " Produkte verkauft und ein Umsatz von " + profit + " Euro erzielt." +
        "\nDie Provision für das Auktionshaus liegt bei 7,5%, wodurch für die Auktionatoren eine Provision von insgesamt " +
        Math.round(profit*0.075) + " Euro erzielt wurde.";
    }

    public static void printEndReport(){
        calcAuctionInfo();
        System.out.println("\nAlle Auktionen wurden beendet.");
        System.out.println("\nFolgende Produkte standen zum Verkauf:");
        for (String item: ItemsOnSale) {
            System.out.println(" -"+item);
        }
        System.out.println("\nFolgende Produkte wurden verkauft:");
        for (Auction item:BoughtItems) {
            System.out.println(" -" + Products.getItemName(item.getProduct())+" wurde für "+item.getCurrentPrice()+" Euro verkauft.");
        }
        System.out.println("\nEinige Informationen zur Auktion:");
        System.out.println(AuctionInfo);

        //ystem.out.println("\nZuletzt noch einige finanzielle Informationen:");
        System.out.println(AuctionStatistics);
    }

}
