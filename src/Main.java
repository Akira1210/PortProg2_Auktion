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

    private static void HandleInputMismatchException(InputMismatchException e) {
        System.out.println(e + ": Eingabe muss eine Ganzzahl sein");
        System.exit(-1);
    }

    private static void setProd() throws FileNotFoundException, NoSuchElementException {
        File file = new File("/Users/jonat/source/Java/PortProg2_Auktion/src/Products.txt");
        Scanner prodfile = new Scanner(file);
        int switchOption = 0;

        try {
            while (prodfile.hasNext()) {
                prodfile.nextLine();
                var name = prodfile.next(); // Datei besteht aus: Name des Produkts
                var type = prodfile.next(); // Produkttyp
                var price = prodfile.next(); // Startpreis
                var step = prodfile.next(); // Preisschritte
                var end = prodfile.next(); // Mindestpreis
                System.out.println(name); // Produkte werden mit '-' voneinander getrennt
                System.out.println(type);
                System.out.println(price);
                System.out.println(step);
                System.out.println(end);
                Products t = new Products(name, type, Double.parseDouble(price), Integer.parseInt(step), Double.parseDouble(end));
                prodfile.nextLine();
            }
        } catch (NoSuchElementException e) {
            System.out.println("Alle Produkte hinzugefügt");
        }
    }

    public static void main(String[] args) throws InputMismatchException {
        Create.setListInterests();
        Scanner input = new Scanner(System.in);

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

        System.out.println("Bitte geben Sie die Anzahl der Auktionen an:");
        try {
            numAuctions = input.nextInt();
        } catch (InputMismatchException eAu) {
            HandleInputMismatchException(eAu);
        }



        input.close();
        try {
            setProd();
        } catch (FileNotFoundException e) {
            System.out.println(e + ": Products.txt nicht gefunden");
            System.exit(-1);
        }

        Create.CreateNum(0, numBidders);
        Create.CreateNum(1,numAuctioneers);
        if (Products.ProdList.size()<numAuctioneers) {
            System.out.println("(!Hinweis!) Es stehen weniger Produkte als Auktionatoren zur Verfügung. Nicht jeder Auktionator wird etwas verkaufen können.");
        }



        //TESTS
        System.out.println(Create.ListBidders.get(0));
        System.out.println(Products.ProdList.get(0));
        System.out.println(Products.ProdList.get(1));

    }
}

class Auctioneers {
    private int patience;
    private String name;

    public Auctioneers(int patience, String name) {
        this.patience = patience;
        this.name = name;
    }
}

class Bidders {
    private int aggr; // Agressivität, wahrscheinlichkeit zuzuschlagen, 0-100, 100 sehr agressiv, 0
                      // desintresse
    private String interests;
    private double money;
    private String name;

    public Bidders(int aggr, String interests, Double money, String name) {
        this.aggr = aggr;
        this.interests = interests;
        this.money = (Math.round(money*100)/100d);
        this.name = name;
    }

    public int getAggr() {
        return this.aggr;
    }

    public void setAggr(int aggr) {
        this.aggr = aggr;
    }

    public String getInterests() {
        return this.interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public double getMoney() {
        return this.money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                " name='" + getName() + "'" +
                ", aggr='" + getAggr() + "'" +
                ", interests='" + getInterests() + "'" +
                ", money='" + getMoney() + "'" +
                "}";
    }

}

class RunAuction {
    private int numAc;

    public RunAuction(int num) {
        this.numAc = num;
    }

}

class Create {
    public static ArrayList<Bidders> ListBidders = new ArrayList<>();
    public static ArrayList<Auctioneers> ListAuctioneers = new ArrayList<>();
    public static ArrayList<String> ListIntersts = new ArrayList<>();

    public static void CreateNum(int type, int numA) {
        for (int i = 0; i < numA; i++) {
            CreateRand(type, i);
        }
    }
    public static void setListInterests(){
        ListIntersts.add("CollectorsItem");
        ListIntersts.add("Car");
    }

    public static void CreateRand(int type, int thisnum) {
        Random rand = new Random();
        if (type == 0) {
            Bidders t = new Bidders(rand.nextInt(0, 101), ListIntersts.get(rand.nextInt(0,ListIntersts.size())), rand.nextDouble(5, 10000), "Bidder: " + (thisnum + 1));
            ListBidders.add(t);

        }
        if (type == 1) {
            Auctioneers t = new Auctioneers(rand.nextInt(0, 10), "Auctioneer: " + (thisnum + 1));
            ListAuctioneers.add(t);

        }

    }
}

class Products {
    private String ItemName;
    private String ItemType;
    private Double ItemPrice;
    private int ItemSteps;
    private Double ItemEndPrice;
    public static ArrayList<Products> ProdList = new ArrayList();

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

}
