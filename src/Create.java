import java.util.ArrayList;
import java.util.Random;
/**
 * Klasse, die dynamisch Objekte nach vorherigen User-Input von außen aufgerufen erzeugt
 */
public class Create {
        //public static ArrayList<Bidders> ListBidders = new ArrayList<>();
        //public static ArrayList<Auctioneers> ListAuctioneers = new ArrayList<>();
        public static ArrayList<String> ListInterests = new ArrayList<>();
    
        /**
         * Löst die Erstellung der Objekt aus, bekommt die Anzahl der gewünschten Objekte und lässt die Erstellung-Methode entsprechent häufig aus
         * @param type 0 = erstellt Bieter Objekt, 1 = erstellt Auktionator Objekt
         * @param numA Anzahl der zu erstellenten Objekte
         */
        public static void CreateNum(int type, int numA) {
            for (int i = 0; i < numA; i++) {
                //CreateRand(type, i);
            }
        }

        /**
         * Fügt Interessen eine Liste hinzu
         * Wird bei Bieter-Erstellung zufällig jedem Bieter zugeteilt
         * 
         */
        public static void setListInterests(){
            ListInterests.add("CollectorsItem");
            ListInterests.add("SportsItem");
            ListInterests.add("VintageItem");
            ListInterests.add("HistoryItem");
            ListInterests.add("Electronics");
            ListInterests.add("Fashion");
            ListInterests.add("Art");
            ListInterests.add("Book");
            ListInterests.add("Jewelry");
            ListInterests.add("Furniture");
            ListInterests.add("None");
        }

    


        public static Bidders createBidder(){
            Random rand = new Random();
            Bidders t = new Bidders(rand.nextInt(50, 30000), rand.nextInt(0,100), ListInterests.get(rand.nextInt(0,ListInterests.size()-1)));
                //ListBidders.add(t);
                return t;
        }

        public static Auctioneers createAuctioneer(AuctionHouse auctionHouse){
            Auctioneers t = new Auctioneers(auctionHouse);
            //ListAuctioneers.add(t);
            return t;
        }
    }
    
