import java.util.Random;
/**
 * Bieter Klasse
 * Enthält die Entscheidungslogik zum Kauf für die Auktion
 * Für für jeden Bieter einen eigenen Thread aus
 */
public class Bidders implements Runnable {
    private int aggr;
    private Communicator communicator;
    private String interests;
    private double money;
    private String name;
    private int attend;
    private Products currentProd; //Zuorndnung, an welcher Auktion der Bieter teilnimmt

    /**
     *
     * @param aggr 'Aggresivität', stellt wahrscheinlichkeit bereit, ob Bieter zuschlägt. 0-100, 100 äußerst Wahrsheinlich, 0 nicht wahrscheinlich
     * @param interests Gibt Interessen der Bieter an. Wenn Interessen mit Produkttyp übereinstimmen ist die Wahrscheinlichkeit zuzuschlagen höher
     * @param money Gibt Budget der Bieter wider. Wenn Money<Produktpreis, dann enthält sich der Bieter. Mit höherem Budget ist die Wahrscheinlichkeit zuzuschlagen höher
     * @param name Name des Bieters. Besteht aus 'Bidder ' + Nummer, als wievielter Bieter Objekt erstellt wurde
     */
    public Bidders(int aggr,Communicator communicator, String interests, Double money, String name) {
        this.aggr = aggr;
        this.communicator=communicator;
        this.interests = interests;
        this.money = (Math.round(money*100)/100d);
        this.name = name;
        Thread t = new Thread(this, name);
        t.start();
    }

    @Override
    public void run() {
        while (true) {
            Message message = communicator.receiveMessage();
            if (message!= null) {
                if (message.getContent().equals("Auction started")) {
                    currentProd = message.getProduct();
                    System.out.println(name + " joined the auction for " + currentProd.getItemName());
                } else if (message.getContent().equals("Price updated")) {
                    currentProd = message.getProduct();
                    System.out.println(name + " received price update: " + currentProd.getItemPrice());
                    if (currentProd.getItemPrice() <= money) {
                        // Bidder wants to buy the product
                        System.out.println(name + " wants to buy " + currentProd.getItemName());
                    }
                } else if (message.getContent().equals("Auction ended")) {
                    currentProd = message.getProduct();
                    System.out.println(name + " received auction end notification for " + currentProd.getItemName());
                }
            }
        }
    }

    /**
     * Methode die regelmäßig überprüft, ob der Bieter sich für den Kauf entscheidet
     * @param b dieser Bieter
     * @param p Produkt, für welches sich der Bieter interessiert
     * @return Entscheidung des Bieters, true wenn Angebot akzeptiert
     */
    public boolean BidderDecision(Bidders b, Products p) {
        Random rand = new Random();
        double Decision=0;
        int takesAction = 0;
        takesAction =+ rand.nextInt(0,100);

        if (takesAction>50) {
            if(b.getInterests().equals(p.getItemType())) {Decision+=100;}
            if (b.getMoney()/p.getItemPrice() >= 2) {Decision+=100;}
            Decision += rand.nextInt(0, 100);
            if (b.getMoney()<p.getItemPrice()) {Decision=0;}
            if (Decision>=210) {return true; }
        }
        return false;

    }

    public int getAttend(){
        return this.attend;
    }

    public void setAttend(int attend) {
        this.attend = attend;
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

}