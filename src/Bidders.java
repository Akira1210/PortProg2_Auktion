import java.util.Random;
/**
 * Bieter Klasse
 * Enthält die Entscheidungslogik zum Kauf für die Auktion
 * Für für jeden Bieter einen eigenen Thread aus
 */
public class Bidders extends Thread {
        private int aggr;
        private String interests;
        private double money;
        private String name;
        public int attend; //Zuorndnung, an welcher Auktion der Bieter teilnimmt 
    
        /**
         * 
         * @param aggr 'Aggresivität', stellt wahrscheinlichkeit bereit, ob Bieter zuschlägt. 0-100, 100 äußerst Wahrsheinlich, 0 nicht wahrscheinlich
         * @param interests Gibt Interessen der Bieter an. Wenn Interessen mit Produkttyp übereinstimmen ist die Wahrscheinlichkeit zuzuschlagen höher
         * @param money Gibt Budget der Bieter wider. Wenn Money<Produktpreis, dann enthält sich der Bieter. Mit höherem Budget ist die Wahrscheinlichkeit zuzuschlagen höher
         * @param name Name des Bieters. Besteht aus 'Bidder ' + Nummer, als wievielter Bieter Objekt erstellt wurde
         */
        public Bidders(int aggr, String interests, Double money, String name) {
            this.aggr = aggr;
            this.interests = interests;
            this.money = (Math.round(money*100)/100d);
            this.name = name;
            Thread t = new Thread(this, name);
            t.start();
        }

        @Override
        /**
         * Ruft in regelmäßigen Abständen Logik der Bieter während der Auktion auf
         */
        public synchronized void run() {
            while(!Products.ProdList.get(this.getAttend()).getItemBought()) {
                try {
                    Thread.sleep(200);
                    if (Products.ProdList.get(this.getAttend()).getItemBought()) {
                        break;}
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (BidderDecision(this, Products.ProdList.get(Main.prodUsed.get(this.getAttend())))) {
                    Products.ProdList.get(Main.prodUsed.get(this.getAttend())).setItemBought(true);
                    String s = Thread.currentThread().getName()+ " hat " + Products.ProdList.get(Main.prodUsed.get(this.getAttend())).getItemName() +" bei Auktion " + (this.getAttend()+1) + " für " + Products.ProdList.get(Main.prodUsed.get(this.getAttend())).getItemPrice()+ " € gekauft.";
                    System.out.println(s);
                    Main.resultAuc.add(s);
                    Thread.interrupted();
                    return;
                }
                Thread.interrupted();
                    return;
    
    
            }
        }
    /**
     * Methode die regelmäßig überprüft, ob der Bieter sich für den Kauf entscheidet
     * @param b dieser Bieter
     * @param p Produkt, für welches sich der Bieter interessiert
     * @return Entscheidung des Bieters, true wenn Angebot akzeptiert
     */
        public synchronized boolean BidderDecision(Bidders b, Products p) {
            Random rand = new Random();
            double Decision=0;
            int takesAction = 0;
            takesAction =+ rand.nextInt(0,100);

            if (takesAction>90) {
            if(b.getInterests().equals(p.getItemType())) {Decision=100;}
            Decision=+ b.getMoney()*(5/Decision)-p.getItemPrice();}
            if (b.getMoney()<p.getItemPrice()) {Decision=0;}
            if (Decision>200) {return true;}
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
