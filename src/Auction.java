import java.util.ArrayList;

class Auction implements Runnable {
    private Products currentProd;
    private Communicator communicator;
    private int patience;
    private ArrayList<Bidders> bidders;

    public Auction(Products currentProd, Communicator communicator) {
        this.currentProd = currentProd;
        this.communicator = communicator;
        this.patience = 5;
        this.bidders = new ArrayList<>();
    }

    @Override
    public void run() {
        // Notify bidders about the auction
        Message message = new Message("Auction started", currentProd);
        communicator.sendMessage(message);

        // Wait for bidders to join
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Start the auction
        while (currentProd.getItemPrice() > currentProd.getItemEndPrice()) {
            // Lower the price
            currentProd.setItemPrice(currentProd.getItemPrice() - 10);

            // Notify bidders about the price update
            message = new Message("Price updated", currentProd);
            communicator.sendMessage(message);

            // Wait for a short period
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Notify bidders about the auction end
        message = new Message("Auction ended", currentProd);
        communicator.sendMessage(message);
    }

    public void addBidder(Bidders bidder) {
        this.bidders.add(bidder);
    }

    public Products getCurrentProd() {
        return currentProd;
    }

    public void setCurrentProd(Products currentProd) {
        this.currentProd = currentProd;
    }

    public ArrayList<Bidders> getBidders() {
        return bidders;
    }

    public void setBidders(ArrayList<Bidders> bidders) {
        this.bidders = bidders;
    }

    public int getPatience() {
        return patience;
    }

    public void setPatience(int patience) {
        this.patience = patience;
    }

    public Communicator getCommunicator() {
        return communicator;
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }
}