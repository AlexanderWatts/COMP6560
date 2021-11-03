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
    private int portfolio = 0;

    public double simpleMovingAverage(int startingPeriod, int endingPeriod) {
        int periodLength = endingPeriod - startingPeriod;

        double sum = 0;

        for (int i = startingPeriod; i < endingPeriod; i++) {
            sum += data[i][0];
        }

        return sum / periodLength;
    }
    
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
        }
        
        // return the total amount (cash) after the trading session,
        // assuming that any stock is sold at the last known closing price
        return budget + (portfolio * data[data.length - 1][0]);
    }
}