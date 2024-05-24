import java.util.ArrayList;
import java.util.Random;
/**
 * Klasse, die dynamisch Bieter nach vorherigen User-Input von außen aufgerufen erzeugt und ihnen Interessen für die Auktionen zuteilt
 */
public class Create {
        public static ArrayList<String> ListInterests = new ArrayList<>();

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
            Random rand = new Random();  // budget, aggressiveBehavior, interest
            Bidders t = new Bidders(rand.nextInt(50, 30000), rand.nextInt(0,100), ListInterests.get(rand.nextInt(0,ListInterests.size()-1)));
                return t;
        }
    }
    
