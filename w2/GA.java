package w2;

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
    private static final int POPULATION_SIZE = 20;

    private static final int TOURNAMENT_SIZE = 2;

    private static final double MUTATION_RATE = 0.2;
    
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
            boolean[][] newPopulation = new boolean[POPULATION_SIZE][BITS];
            int items = 0;

            for (int i = 0; i < POPULATION_SIZE; i++) {
                if (random.nextDouble() < MUTATION_RATE) {
                    newPopulation[i] = bitMutation(tournamentSelect());
                } else {
                    boolean[][] crossedOver = uniformCrossover(tournamentSelect(), tournamentSelect());

                    for (int j = 0; j < crossedOver.length; j++) {
                        if (items < POPULATION_SIZE) {
                            newPopulation[items++] = crossedOver[j];
                        }
                    }
                }
            }

            System.out.println("-------------------Population = " + g + "-------------------");
            printPopulation(population);
            System.out.println("Population best fitness" + getBestFitness());

            population = newPopulation;

            //----------------------------------------------------------//
            // evaluates the new population                             //
            //----------------------------------------------------------//
            evaluate();
        }
        
        // prints the value of the best individual
        System.out.println("Final best fitness " + getBestFitness());
    }

    private boolean[] bitMutation(boolean[] individual) {
        boolean[] mutated = Arrays.copyOf(individual, BITS);
        double chance = 1 / individual.length;

        for (int i = 0; i < BITS; i++) {
            double probability = random.nextDouble();

            if (probability < chance) {
                mutated[i] = !mutated[i];
            }
        }
        
        return mutated;
    }

    private boolean[] tournamentSelect() {
        boolean[][] selected = new boolean[TOURNAMENT_SIZE][];

        // Randomly select
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            selected[i] = population[random.nextInt(POPULATION_SIZE)];
            // System.out.println(getIndividualFitness(selected[i]));
        }

        // printPopulation(selected);

        return getBestIndividual(selected);
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

    public void printPopulation(boolean[][] population) {
        Arrays.stream(population).forEach(individual -> System.out.println(Arrays.toString(individual)));
    }
    

    public int getIndividualFitness(boolean[] individual) {
        int sum = 0;

        for (int i = 0; i < individual.length; i++) {
            sum += individual[i] ? 1 : 0;
        }

        return sum;
    }
    
    /**
     * Calculates the fitness of each individual.
     */
    private void evaluate() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            fitness[i] = getIndividualFitness(population[i]);
        }
    }

    private int getBestFitness() {
        int best = 0;

        for (int i = 0; i < POPULATION_SIZE; i++) {
            int individualFitness = getIndividualFitness(population[i]);

            if (individualFitness > best) {
                best = individualFitness;
            }
        }

        return best;
    }

    private boolean[] getBestIndividual(boolean[][] population) {
        int best = 0;
        boolean[] bestIndividual = new boolean[BITS];

        for (int i = 0; i < population.length; i++) {
            int individualFitness = getIndividualFitness(population[i]);

            if (individualFitness > best) {
                best = individualFitness;
                bestIndividual = population[i];
            }
        }

        return bestIndividual;
    }

    /**
     * Print fitness values
     */
    public void printFitnessValues() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            System.out.println(Arrays.toString(population[i]) + " = " + fitness[i]);
        }
    }

    public boolean[][] twoPointCrossover(boolean[] first, boolean[] second) {
        int k1 = random.nextInt(BITS);
        int k2 = random.nextInt(BITS);

        boolean[] parentOne = Arrays.copyOf(first, BITS);
        boolean[] parentTwo = Arrays.copyOf(second, BITS);

        for (int i = k1; i < k2; i++) {
            boolean buffer = parentOne[i];
            parentOne[i] = parentTwo[i];
            parentTwo[i] = buffer;
        }

        boolean[][] result = new boolean[2][BITS];

        result[0] = parentOne;
        result[1] = parentTwo;

        return result;
    }

    public boolean[][] uniformCrossover(boolean[] first, boolean[] second) {
        boolean[] parentOne = Arrays.copyOf(first, BITS);
        boolean[] parentTwo = Arrays.copyOf(second, BITS);

        for (int i = 0; i < BITS; i++) {
            double probability = random.nextDouble();

            if (probability > 0.5) {
                boolean buffer = parentOne[i];
                parentOne[i] = parentTwo[i];
                parentTwo[i] = buffer;
            }
        }

        boolean[][] result = new boolean[2][BITS];

        result[0] = parentOne;
        result[1] = parentTwo;

        return result;
    }

    public boolean[][] crossover(boolean[] first, boolean[] second) {
        int k = random.nextInt(BITS);

        boolean[] parentOne = Arrays.copyOf(first, BITS);
        boolean[] parentTwo = Arrays.copyOf(second, BITS);

        for (int i = k; i < BITS; i++) {
            boolean buffer = parentOne[i];
            parentOne[i] = parentTwo[i];
            parentTwo[i] = buffer;
        }

        boolean[][] result = new boolean[2][BITS];

        result[0] = parentOne;
        result[1] = parentTwo;

        return result;
    }

    public boolean[][] crossover(int first, int second) {
        int k = random.nextInt(BITS);

        boolean[] parentOne = Arrays.copyOf(population[first], BITS);
        boolean[] parentTwo = Arrays.copyOf(population[second], BITS);

        for (int i = k; i < BITS; i++) {
            boolean buffer = parentOne[i];
            parentOne[i] = parentTwo[i];
            parentTwo[i] = buffer;
        }

        boolean[][] result = new boolean[2][BITS];

        result[0] = parentOne;
        result[1] = parentTwo;

        return result;
    }

    public boolean[] mutation(int parent) {
        int k = random.nextInt(BITS);

        boolean[] mutated = Arrays.copyOf(population[parent], BITS);

        mutated[k] = !mutated[k];

        return mutated;
    }
}