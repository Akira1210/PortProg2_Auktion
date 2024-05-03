import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuctionHouse {
    private ArrayList<Thread> tAucList;
    private Communicator communicator;
    private boolean reportGenerated;

    public AuctionHouse() {
        this.tAucList = new ArrayList<>();
        this.communicator = new Communicator();
        this.reportGenerated = false;
    }

    public void startAuction(Products currentProd) {
        Auction auc = new Auction(currentProd, communicator);
        Thread tAuc = new Thread(auc);
        tAuc.start();
        tAucList.add(tAuc);
    }

    public void endAuction(int prodIndex) {
        Products p = Products.ProdList.get(prodIndex);
        p.setItemBought(true);
        p.setItemUsed(false);
        p.setItemPrice(p.getItemEndPrice());
    }

    public void endReport() {
        reportGenerated = true;
        ArrayList<String> res = new ArrayList<>();
        int i = 0;
        double u = 0.0;
        for (Products p : Products.ProdList) {
            if (p.getItemBought()) {
                i++;
                u = u + p.getItemPrice();
            }
        }
        System.out.println(res.toString().replace(",", ""));
    }

    public ArrayList<Thread> gettAucList() {
        return tAucList;
    }

    public void settAucList(ArrayList<Thread> tAucList) {
        this.tAucList = tAucList;
    }

    public Communicator getCommunicator() {
        return communicator;
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    public boolean isReportGenerated() {
        return reportGenerated;
    }

    public void setReportGenerated(boolean reportGenerated) {
        this.reportGenerated = reportGenerated;
    }
}