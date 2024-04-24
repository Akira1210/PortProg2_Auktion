import java.util.Random;

public class Bidders extends Thread {
        private int aggr; // Agressivität, wahrscheinlichkeit zuzuschlagen, 0-100, 100 sehr agressiv, 0
                          // desintresse
        private String interests;
        private double money;
        private String name;
        public int attend; //Zuorndnung, an welcher Auktion der Bieter teilnimmt 
    
        public Bidders(int aggr, String interests, Double money, String name) {
            this.aggr = aggr;
            this.interests = interests;
            this.money = (Math.round(money*100)/100d);
            this.name = name;
            Thread t = new Thread(this, name);
            t.start();
        }
    
        @Override
        public void run() {
            while(!Auctioneers.bought[this.getAttend()]) {
                try {
                    Thread.sleep(2000);
                    if (Auctioneers.bought[this.getAttend()]) {
                        break;}
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (BidderDecision(this, Products.ProdList.get(Main.prodUsed.get(this.getAttend())))) {
                    Auctioneers.bought[this.getAttend()] = true;
                    System.out.println(this.getName()+ " hat " + Products.ProdList.get(Main.prodUsed.get(this.getAttend())).getItemName() + " für " + Products.ProdList.get(Main.prodUsed.get(this.getAttend())).getItemPrice()+ " gekauft.");
                    Thread.interrupted();
                    return;
                }
                Thread.interrupted();
                    return;
    
    
            }
        }
    
        public boolean BidderDecision(Bidders b, Products p) {
            Random rand = new Random();
            double Decision=0;
            int takesAction = 0;
            takesAction = rand.nextInt(0,100);
            if (takesAction>90) {
            if(b.getInterests().equals(p.getItemType())) {Decision=+100;}
            Decision=+ b.getMoney()*(5/Decision)-p.getItemPrice();}
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
