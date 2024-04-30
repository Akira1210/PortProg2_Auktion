import java.util.ArrayList;
import java.util.Random;
/**
 * Klasse, die dynamisch Objekte nach vorherigen User-Input von außen aufgerufen erzeugt
 */
public class Create {
        public static ArrayList<Bidders> ListBidders = new ArrayList<>();
        public static ArrayList<Auctioneers> ListAuctioneers = new ArrayList<>();
        public static ArrayList<String> ListInterests = new ArrayList<>();
    
        /**
         * Löst die Erstellung der Objekt aus, bekommt die Anzahl der gewünschten Objekte und lässt die Erstellung-Methode entsprechent häufig aus
         * @param type 0 = erstellt Bieter Objekt, 1 = erstellt Auktionator Objekt
         * @param numA Anzahl der zu erstellenten Objekte
         */
        public static void CreateNum(int type, int numA) {
            for (int i = 0; i < numA; i++) {
                CreateRand(type, i);
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
    /**
     * Erstellt Bieter und Auktionatoren 
     * @param type 0 = erstellt Bieter Objekt, 1 = erstellt Auktionator Objekt
     * @param thisnum gibt zur Benennung jedem Objekt eine Nummer
     */
        private static void CreateRand(int type, int thisnum) {
            Random rand = new Random();
            if (type == 0) {
                Bidders t = new Bidders(rand.nextInt(0, 101), ListInterests.get(rand.nextInt(0,ListInterests.size())), rand.nextDouble(500, 1000000), "Bidder: " + (thisnum + 1));
                ListBidders.add(t);
    
            }
            if (type == 1) {
                Auctioneers t = new Auctioneers(rand.nextInt(1000, 7000), "Auctioneer: " + (thisnum + 1));
                ListAuctioneers.add(t);
    
            }
    
        }
    }
    
