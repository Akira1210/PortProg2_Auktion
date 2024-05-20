import java.util.ArrayList;
import java.util.List;

public class Communicator {
    private List<Bidders> bidders;
    private List<Auctioneers> auctioneers;
    private double currentPrice;

    public Communicator() {
        this.bidders = new ArrayList<>();
        this.auctioneers = new ArrayList<>();
    }

    public void registerBidder(Bidders bidder) {
        bidders.add(bidder);
    }

    public  void registerAuctioneer(Auctioneers auctioneer) {
        auctioneers.add(auctioneer);
    }
    

    public void toAucc(double bid, Auction auction) {
        auction.bid(bid);
    }

    public void toBidd(double newPrice) {
        this.currentPrice=newPrice;
    }

    public double getCurrentPrice(){
        return currentPrice;
    }
}
