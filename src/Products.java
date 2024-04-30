import java.util.ArrayList;

/** Klasse für Produkte
 * Enthält alle nötigen Parameter für Produkte, wie Name, Produkttyp, Preis, Preissenkungsschritte und Mindestpreis
 * Produkte werden extern mit dieser Klasse mittels Products.txt erstellt
 */
public class Products {

    private String ItemName;
    private String ItemType;
    private Double ItemPrice;
    private int ItemSteps;
    private Double ItemEndPrice;
    private boolean ItemBought;
    private boolean ItemBelowMin;
    public static ArrayList<Products> ProdList = new ArrayList<>();
/**
 * 
 * @param ItemName Name des Produkts
 * @param ItemType Art des Produkts, z.B. Sammlerstück, Auto, Geschichtsgegenstand, usw.
 * @param ItemPrice Preis des Produkts, wird während der Auktion bei Preissenkungen angepasst
 * @param ItemSteps Gibt die Preissenkungsschritte an
 * @param ItemEndPrice Gibt den Mindestpreis an
 */
    public Products(String ItemName, String ItemType, Double ItemPrice, int ItemSteps, Double ItemEndPrice) {
        this.ItemName = ItemName;
        this.ItemType = ItemType;
        this.ItemPrice = ItemPrice;
        this.ItemSteps = ItemSteps;
        this.ItemEndPrice = ItemEndPrice;
        this.ItemBelowMin = false;
        ProdList.add(this);
    }

    public String getItemName() {
        return this.ItemName;
    }

    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
    }

    public String getItemType() {
        return this.ItemType;
    }

    public void setItemType(String ItemType) {
        this.ItemType = ItemType;
    }

    public Double getItemPrice() {
        return this.ItemPrice;
    }

    public void setItemPrice(Double ItemPrice) {
        this.ItemPrice = ItemPrice;
    }

    public int getItemSteps() {
        return this.ItemSteps;
    }

    public void setItemSteps(int ItemSteps) {
        this.ItemSteps = ItemSteps;
    }

    public Double getItemEndPrice() {
        return this.ItemEndPrice;
    }

    public void setItemEndPrice(Double ItemEndPrice) {
        this.ItemEndPrice = ItemEndPrice;
    }

    public static int getProdListSize(){
        return ProdList.size();
    }
    public boolean getItemBought(){
        return this.ItemBought;
    }

    public void setItemBought(boolean ItemBought) {
        this.ItemBought = ItemBought;
    }

    public boolean getItemBelowMin() {
        return this.ItemBelowMin;
    }

    public void setItemBelowMin(boolean ItemBelowMin) {
        this.ItemBelowMin = ItemBelowMin;
    }

}

