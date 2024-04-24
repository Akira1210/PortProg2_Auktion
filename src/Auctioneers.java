import java.util.ArrayList;
import java.util.Random;

public class Auctioneers extends Thread {
        private int patience;
        private String name;
        public Products currentProd;
        private int currentProdId;
        public ArrayList<Thread> tAucList = new ArrayList<>();
        public static boolean[] bought = new boolean[Main.numAuctions];
    
    
        public Auctioneers(int patience, String name) {
            this.patience = patience;
            this.name = name;
            Random rand = new Random();
            while (this.currentProd == null) {
            int rnum = rand.nextInt(0,Products.ProdList.size());
            if (!Main.prodUsed.contains(rnum)) {
            this.currentProd = Products.ProdList.get(rnum);
            currentProdId=Main.prodUsed.size();
            Main.prodUsed.add(rnum);
    
            Thread t = new Thread(this, name);
            tAucList.add(t);
            t.start();
            }
    
            }
        }
        @Override
        public synchronized void run() {
            System.out.println("Von "+ Thread.currentThread().getName() + " versteigert wird: "+ this.currentProd.getItemName() + ". Der Startpreis beträgt: " + this.currentProd.getItemPrice());
            while (!Auctioneers.bought[this.currentProdId]) {
                try {
                    sleep(this.patience);
                    if (Auctioneers.bought[this.currentProdId]) {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (this.currentProd.getItemPrice().compareTo(this.currentProd.getItemEndPrice()) >0){
                this.currentProd.setItemPrice(this.currentProd.getItemPrice()-this.currentProd.getItemSteps());
            System.out.println("Der Preis für "+this.currentProd.getItemName()+" liegt nun bei "+this.currentProd.getItemPrice());}
                else {System.out.println("Mindestpreis unterschritten. Produkt "+this.currentProd.getItemName()+" nicht verkauft.");
                Auctioneers.bought[this.currentProdId] = true;
                nextAuction(this);
                return;
            }
    
            }
            System.out.println("Produkt verkauft.");
            //this.interrupt();
            nextAuction(this);
            return;
            
        }
    
        private void nextAuction(Auctioneers a) {
            this.interrupt();
            for (int i=0; i<Main.numAuctions;i++) {
                if (bought[i] == false) {
    
                    // this.currentProd =;
                    // a.run();
                }
            }
        }
    
    }
