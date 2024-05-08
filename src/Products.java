// Product class

import java.util.ArrayList;

public class Products {
    private String itemname;
    private double startingPrice;
    private double decrementPrice;
    private double minimalPrice;
    private String itemType;
    private boolean inAuction;
    private static ArrayList<Products> allItems = new ArrayList<>();

    public Products(String itemname, String itemType, double startingPrice, double decrementPrice, double minimalPrice) {
        this.itemname = itemname;
        this.itemType = itemType;
        this.startingPrice = startingPrice;
        this.decrementPrice = decrementPrice;
        this.minimalPrice = minimalPrice;
        setInAuction(false);
        allItems.add(this);
    }

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


