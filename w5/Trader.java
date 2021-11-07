import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class can be used to simulate a trading strategy based
 * on technical indicators.
 *
 * @author Fernando Otero
 * @version 1.0
 */
public class Trader
{
    /**
     * Holds the data.
     * 
     * 0: closing price
     * 1: 15 days Moving Average
     * 2: 50 days Moving Average
     * 3: 10 days Momentum
     * 4: MA Action
     * 5: MOM Action
     */
    private double[][] data = new double[0][0];
    
    /**
     * Initial budget.
     */
    private double budget = 3000;
    
    /**
     * The (empty) initial portfolio.
     */
    private double portfolio = 0;

    /**
     * Loads a csv file in memory, skipping the first line (column names).
     * 
     * @ param filename the file to load.
     */
    public void load(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        ArrayList<double[]> lines = new ArrayList<>();
        String line = null;
        // skip the first line (column names)
        reader.readLine();
        
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(",");
            
            double[] values = new double[split.length];
            
            for (int i = 0; i < values.length; i++) {
                values[i] = "N/A".equals(split[i])
                                ? Double.NaN
                                : Double.valueOf(split[i]);
            }

            lines.add(values);
        }

        data = lines.toArray(data);
    }

    private void action(double currentPrice, double actionValue) {

        System.out.println("---------------------------------");
        System.out.println("Current stock price: " + currentPrice);
        System.out.println("Moving average action: " + actionValue);
        System.out.println("Budget: " + budget);
        
        if (actionValue == 1.0) {
            double toBuyAmount = budget / currentPrice;
            budget -= (toBuyAmount * currentPrice);
            portfolio += toBuyAmount;
        }

        if (actionValue == 2.0) {
            double toSellAmount = budget / currentPrice;
            double canSellAmount = portfolio - toSellAmount;

            budget += (canSellAmount * currentPrice);

            portfolio -= canSellAmount;
        }
    }

    /**
     * Simulates a trade.
     * 
     * @return the total amount (cash) after the trading session.
     */
    public double trade() {
        // go through each line of our data
        for (int i = 0; i < data.length; i++) {
            // should look at the "Action" column to decide what to do,
            // ignoring cases where there is no action (Double.NaN)
            //System.out.println(data[i][4]);

            // MA Action
            action(data[i][0], data[i][4]);

            // MOM Action
            // action(data[i][0], data[i][5]);
        }
        
        // return the total amount (cash) after the trading session,
        // assuming that any stock is sold at the last known closing price
        return budget + (portfolio * data[data.length - 1][0]);
    }
}