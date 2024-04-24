import java.util.ArrayList;
import java.util.Random;

public class Create {
        public static ArrayList<Bidders> ListBidders = new ArrayList<>();
        public static ArrayList<Auctioneers> ListAuctioneers = new ArrayList<>();
        public static ArrayList<String> ListInterests = new ArrayList<>();
    
        public static void CreateNum(int type, int numA) {
            for (int i = 0; i < numA; i++) {
                CreateRand(type, i);
            }
        }
        public static void setListInterests(){
            ListInterests.add("CollectorsItem");
            ListInterests.add("Car");
        }
    
        public static void CreateRand(int type, int thisnum) {
            Random rand = new Random();
            if (type == 0) {
                Bidders t = new Bidders(rand.nextInt(0, 101), ListInterests.get(rand.nextInt(0,ListInterests.size())), rand.nextDouble(5, 10000), "Bidder: " + (thisnum + 1));
                ListBidders.add(t);
    
            }
            if (type == 1) {
                Auctioneers t = new Auctioneers(rand.nextInt(1000, 10000), "Auctioneer: " + (thisnum + 1));
                ListAuctioneers.add(t);
    
            }
    
        }
    }
    
