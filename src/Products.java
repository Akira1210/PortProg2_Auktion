// Product class
import java.util.Random;
public class Products {
    private double startingPrice;
    private double decrementPrice;
    private double minimalPrice;

    public Products(double startingPrice, double decrementPrice, double minimalPrice) {
        this.startingPrice = startingPrice;
        this.decrementPrice = decrementPrice;
        this.minimalPrice = minimalPrice;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public double getDecrementPrice() {
        return decrementPrice;
    }

    public double getMinimalPrice() {
        return minimalPrice;
    }
}


