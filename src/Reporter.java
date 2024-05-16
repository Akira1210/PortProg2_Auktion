import java.util.ArrayList;

public class Reporter {
    private static ArrayList<String> ItemsOnSale = new ArrayList<>();          //Auflistung der Waren, die zur Auktion stehen
    private ArrayList<String> BoughtItems = new ArrayList<>();          //Auflistung der Waren, die verkauft wurden
    private ArrayList<String> AuctionInfo = new ArrayList<>();          //Auktionsinfos, wie Anzahl der Auktionen, Auktionatoren und Bietern
    private ArrayList<String> AuctionStatistics = new ArrayList<>();    //Endergebnis hinsichtlich Umsatzes, Provision und Anazhl verkaufter Produkte

    public static void addProduct(Products product) {
        ItemsOnSale.add(Products.getItemName(product));
    }

    public void addBoughtItems(Products product) {

    }

    public void writeAuctionInfo() {

    }

    public void calcAuctionInfo(){

    }

}
