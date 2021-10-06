import java.util.Arrays;
import java.util.Random;

/**
 * Simple (skeleton) GA for the OneMax problem.
 * 
 * @author Fernando Otero
 * @version 1.1
 */
public class GA
{
    /**
     * Number of bits of the individual encoding.
     */
    private static final int BITS = 5;
    
    /**
     * The population size.
     */
    private static final int POPULATION_SIZE = 4;
    
    /**
     * The number of generations.
     */
    private static final int MAX_GENERATION = 50;
    
    /**
     * Random number generation.
     */
    private Random random = new Random();
        
    /**
     * The current population;
     */
    private boolean[][] population = new boolean[POPULATION_SIZE][BITS];
    
    /**
     * Fitness values of each individual of the population.
     */
    private int[] fitness = new int[POPULATION_SIZE];
    
    /**
     * Starts the execution of the GA.
     */
    public void run() {
        //--------------------------------------------------------------//
        // initialises the population                                   //
        //--------------------------------------------------------------//
        initialise();
        
        //--------------------------------------------------------------//
        // evaluates the propulation                                    //
        //--------------------------------------------------------------//
        evaluate();
        
        for (int g = 0; g < MAX_GENERATION; g++) {
            //----------------------------------------------------------//
            // creates a new population                                 //
            //----------------------------------------------------------//
            
            // TODO
            
            //----------------------------------------------------------//
            // evaluates the new population                             //
            //----------------------------------------------------------//
            evaluate();
        }
        
        // prints the value of the best individual
        
        // TODO
    }
    
    /**
     * Retuns the index of the selected parent using a roulette wheel.
     * 
     * @return the index of the selected parent using a roulette wheel.
     */
    private int select() {
        // prepares for roulette wheel selection
        double[] roulette = new double[POPULATION_SIZE];
        double total = 0;
            
        for (int i = 0; i < POPULATION_SIZE; i++) {
            total += fitness[i];
        }
            
        double cumulative = 0.0;
            
        for (int i = 0; i < POPULATION_SIZE; i++) {
            roulette[i] = cumulative + (fitness[i] / total);
            cumulative = roulette[i];
        }
            
        roulette[POPULATION_SIZE - 1] = 1.0;
        
        int parent = -1;
        double probability = random.nextDouble();
        
        //selects a parent individual
        for (int i = 0; i < POPULATION_SIZE; i++) {
            if (probability <= roulette[i]) {
                parent = i;
                break;
            }
        }

        return parent;
    }
    
    /**
     * Initialises the population.
     */
    private void initialise() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            boolean[] individual = new boolean[BITS];

            for (int j = 0; j < BITS; j++) {
                individual[j] = random.nextBoolean();
            }

            population[i] = individual;
        }
    }

    /**
     * Print the initialised population
     */
    public void printPopulation() {
        Arrays.stream(population).forEach(individual -> System.out.println(Arrays.toString(individual)));
    }
    
    /**
     * Calculates the fitness of each individual.
     */
    private void evaluate() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int sum = 0;

            for (int j = 0; j < BITS; j++) {
                sum += population[i][j] ? 1 : 0;
            }

            fitness[i] = sum;
        }
    }

    /**
     * Print fitness values
     */
    public void printFitnessValues() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            System.out.println(Arrays.toString(population[i]) + " = " + fitness[i]);
        }
    }
}