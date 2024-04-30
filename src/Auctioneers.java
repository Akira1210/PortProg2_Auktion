import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import java.util.Random;

/**
 * Auktionator Klasse
 * Beinhaltet Zuteilung von Produkten zu Auktionatoren und die Logik für die
 * Auktion
 * Führt für jeden Auktionator einen eigenen Thread aus
 */
public class Auctioneers extends Thread {
    private int patience;
    private String name;
    private Products currentProd;
    public ArrayList<Thread> tAucList = new ArrayList<>();
    private boolean reportGenerated = false;

    /**
     * 
     * @param patience Gedult des Auktionators, um so kleiner desto schneller werden
     *                 Preissenkungen vorgenommen
     * @param name     Name des Auktionators. Besteht aus 'Auctioneer ' + Nummer,
     *                 als wievielter Auktionator Objekt erstellt wurde
     */
    public Auctioneers(int patience, String name) {
        this.patience = patience;
        this.name = name;
        Random rand = new Random();
        while (this.currentProd == null) {
            int rnum = rand.nextInt(0, Products.ProdList.size());
            if (!Products.ProdList.get(rnum).getItemUsed()) {
                this.currentProd = Products.ProdList.get(rnum);
                Products.ProdList.get(rnum).setItemUsed(true);

                Thread t = new Thread(this, name);
                tAucList.add(t);
                t.start();
            }

        }
    }

    @Override
    /**
     * Logik der Auktionatoren während der Auktion
     */
    public synchronized void run() {

    }

    /**
     * Noch nicht implementiert
     * Wenn mehr Auktionen geplant sind, als Auktionatoren zur Verfügung stellen,
     * erhält hier der Auktionator eines neues Produkt zum Verkauf
     */

    public void endReport() {
        reportGenerated = true;
        ArrayList<String> res = new ArrayList<>();
        res.add("------------------------------------------------\n");
        res.add("Alle Auktionen beendet. Es wurden " + Main.numAuctions + " durchgeführt.\n");
        res.add("------------------------------------------------\n");
        int i = 0;
        double u = 0.0;
        for (Products p : Products.ProdList) {
            if (p.getItemBought()) {
                i++;
                u = +p.getItemPrice();
            }
        }
        res.add("Es wurden " + i + " Produkte verkauft und einen Umsatz von " + u + " € erzielt.\n");
        res.add("Dabei wurde bei einer Provision von 10% für die Auktionatoren insgesamt ein Verdienst von " + (u * 0.1)
                + " € erzielt.");
        System.out.println(res.toString().replace(",", ""));   
    }

//GETTER AND SETTER
    public int getPatience() {
        return this.patience;
    }

    public void setPatience(int patience) {
        this.patience = patience;
    }

    public Products getCurrentProd() {
        return this.currentProd;
    }

    public void setCurrentProd(Products currentProd) {
        this.currentProd = currentProd;
    }

    public ArrayList<Thread> getTAucList() {
        return this.tAucList;
    }

    public void setTAucList(ArrayList<Thread> tAucList) {
        this.tAucList = tAucList;
    }

    public boolean isReportGenerated() {
        return this.reportGenerated;
    }

    public boolean getReportGenerated() {
        return this.reportGenerated;
    }

    public void setReportGenerated(boolean reportGenerated) {
        this.reportGenerated = reportGenerated;
    }
}
