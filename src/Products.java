import java.util.ArrayList;

/**
 * Klasse, die Produkte als Objekte definiert. Enthält Produktname, Startpreis, Preinssenkungsschritte, Preisminimum, Produkttyp, eine
 * Boolean die angibt, ob das Produkt sich in einer Auktion befindet und eine Liste aller Produkte
 */
public class Products {
    private String itemname;
    private double startingPrice;
    private double decrementPrice;
    private double minimalPrice;
    private String itemType;
    private boolean inAuction;
    private static ArrayList<Products> allItems = new ArrayList<>();

    /**
     * Erstellt Produkt Objekt
     * @param itemname          Name des Produkts
     * @param itemType          Art des Produkts
     * @param startingPrice     Startpreis
     * @param decrementPrice    Preissenkungsschritte
     * @param minimalPrice      Preisminimum
     */
    public Products(String itemname, String itemType, double startingPrice, double decrementPrice, double minimalPrice) {
        this.itemname = itemname;
        this.itemType = itemType;
        this.startingPrice = startingPrice;
        this.decrementPrice = decrementPrice;
        this.minimalPrice = minimalPrice;
        setInAuction(false);            //Produkt anfangs in keiner Auktion
        allItems.add(this);                       //Produkt wird Liste hinzugefügt
    }

    // GETTER / SETTERS
    public static String getItemName(Products product) {
        return product.itemname;
    }

    public String getItemType() {
        return itemType;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public double getDecrementPrice() {
        return decrementPrice;
    }

    public double getMinimalPrice() {
        return minimalPrice;
    }

    public static Products getItem(int i){
        return allItems.get(i);
    }

    public boolean getInAuction(){
        return inAuction;
    }

    public void setInAuction(boolean inAuction){
        this.inAuction=inAuction;
    }

    public static int getItemAmount(){
        return allItems.size();
    }
}


