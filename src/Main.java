import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static int numAuctioneers;
    static int numBidders;
    static int numAuctions;

    
    public static void main(String[] args) throws InputMismatchException {
        Scanner input = new Scanner(System.in);
        
        System.out.println("Bitte geben Sie die Anzahl der Auktionatoren an:");
        try {numAuctioneers = input.nextInt();}
        catch (InputMismatchException eA) {
            System.out.println(eA+" :Input must be numeric");
        System.exit(-1);}
        
        System.out.println("Bitte geben Sie die Anzahl der Bieter an:");
        try {numBidders = input.nextInt(); }
        catch (InputMismatchException eB) {
            System.out.println(eB+" :Input must be numeric");
            System.exit(-1);}
        
        System.out.println("Bitte geben Sie die Anzahl der Auktionen an:");
        try {numAuctions = input.nextInt();}
        catch (InputMismatchException eAu) {
            System.out.println(eAu+" :Input must be numeric");
            System.exit(-1);
        }
        
        input.close();
        
        Create.CreateNum(0,numBidders);
        //Create.CreateNum(1,numAuctioneers);
        System.out.println(Create.ListBidders.get(0));

    }
}

class Auctioneers{
    private int patience;

    public Auctioneers(int patience){
        this.patience=patience;
    }
}

class Bidders{
    private int aggr;   //Agressivität, wahrscheinlichkeit zuzuschlagen, 0-100, 100 sehr agressiv, 0 desintresse
    private String interests;
    private double money;
    private String name;
    public Bidders(int aggr, String interests, Double money, String name) {
        this.aggr = aggr;
        this.interests=interests;
        this.money=money;
        this.name=name;
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
            " aggr='" + getAggr() + "'" +
            ", interests='" + getInterests() + "'" +
            ", money='" + getMoney() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
    


}

class RunAuction{
    private int numAc;
    public RunAuction(int num) {
        this.numAc = num;
    }

}

class Create{
    public static ArrayList<Bidders> ListBidders = new ArrayList<>();
    public static ArrayList<Auctioneers> ListAuctioneers = new ArrayList<>();

    public static void CreateNum(int type, int numA) {
        for (int i=0; i<numA;i++) {
            CreateRand(type, i);
        }
    }

    public static void CreateRand(int type, int thisnum) {
        Random rand = new Random();
        if (type==0) {
            Bidders t = new Bidders(rand.nextInt(0,101),"",rand.nextDouble(5,10000),"Bidder: "+(thisnum+1));
            ListBidders.add(t);

        }
        if(type==1) {


        }

    }
}
