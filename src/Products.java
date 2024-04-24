import java.util.ArrayList;

public class Products {

    private String ItemName;
    private String ItemType;
    private Double ItemPrice;
    private int ItemSteps;
    private Double ItemEndPrice;
    public static ArrayList<Products> ProdList = new ArrayList<>();

    public Products(String ItemName, String ItemType, Double ItemPrice, int ItemSteps, Double ItemEndPrice) {
        this.ItemName = ItemName;
        this.ItemType = ItemType;
        this.ItemPrice = ItemPrice;
        this.ItemSteps = ItemSteps;
        this.ItemEndPrice = ItemEndPrice;
        ProdList.add(this);}

    @Override
    public String toString() {
        return "{" +
            " ItemName='" + getItemName() + "'" +
            ", ItemType='" + getItemType() + "'" +
            ", ItemPrice='" + getItemPrice() + "'" +
            ", ItemSteps='" + getItemSteps() + "'" +
            ", ItemEndPrice='" + getItemEndPrice() + "'" +
            "}";
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



}
