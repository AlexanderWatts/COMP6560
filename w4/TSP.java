import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Simple GA for the Travelling Salesman Problem.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class TSP
{

    private static final int tourSize = 24;
    /**
     * The population size.
     */
    private static final int POPULATION_SIZE = 100;

    /**
     * The number of generations.
     */
    private static final int MAX_GENERATION = 50;

    /**
     * Probability of the mutation operator.
     */
    private static final double MUTATION_PROBABILITY = 0.1;

    /**
     * Probability of the crossover operator.
     */
    private static final double CROSSOVER_PROBABILITY = 0.9;

    /**
     * Random number generation.
     */
    private Random random = new Random();

    /**
     * The current population;
     */
    private int[][] population = new int[POPULATION_SIZE][];

    /**
     * Fitness values of each individual of the population.
     */
    private int[] fitness = new int[POPULATION_SIZE];

    /**
     * The number of cities of the TSP instance.
     */
    private int SIZE;

    /**
     * TSP cost matrix.
     */
    private int[][] COST;

    /**
     * Starts the execution of the GA.
     *
     * @param filename the TSP file instance.
     */
    public void run(String filename) {
        //--------------------------------------------------------------//
        // loads the TSP problem                                        //
        //--------------------------------------------------------------//
        load(filename);

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

            int[][] newPopulation = new int[POPULATION_SIZE][SIZE];
            // index of the current individual to be created
            int current = 0;

            while (current < POPULATION_SIZE) {
                double probability = random.nextDouble();

                // should we perform mutation?
                    if (probability <= MUTATION_PROBABILITY || (POPULATION_SIZE - current) == 1) {
                    int parent = select();

                    int[] offspring = mutation(parent);
                    // copies the offspring to the new population
                    copy(newPopulation, offspring, current);
                    current += 1;
                }
                // otherwise we perform a crossover
                else {
                    int first = select();
                    int second = select();

                    int[][] offspring = crossover(first, second);
                    // copies the offspring to the new population
                    copy(newPopulation, offspring[0], current);
                    current += 1;
                    copy(newPopulation, offspring[1], current);
                    current += 1;
                }
            }

            population = newPopulation;

            //----------------------------------------------------------//
            // evaluates the new population                             //
            //----------------------------------------------------------//
            evaluate();
        }

        // prints the value of the best individual
        int best = 0;

        for (int i = 1; i < POPULATION_SIZE; i++) {
            if (fitness[best] > fitness[i]) {
                best = i;
            }
        }

        System.out.println("Best individual: length " + fitness[best]);
        System.out.print("[");

        for (int i = 1; i < SIZE; i++) {
            System.out.print(" ");
            System.out.print(population[best][i]);
        }

        System.out.println(" ]");
    }

    /**
     * [Exercise 1]
     *
     * Initialises the population. The population is represented by a
     * 2-dimensional array attribute named population.
     */
    public void initialise() {
        List<Integer> cities = new ArrayList<>();

        for (int i = 0; i < tourSize; i++) {
            int value = i + 1;
            cities.add(value);
        }

        for (int i = 0; i < population.length; i++) {
            Collections.shuffle(cities);

            population[i] = cities.stream().mapToInt(city->city).toArray();

            System.out.println(Arrays.toString(population[i]));
        }
    }

    /**
     * [Exercise 2]
     *
     * Calculates the fitness of each individual.
     */
    private void evaluate() {
    }

    /**
     * [Exercise 3]
     *
     * Crossover operator.
     *
     * @param first index of the first parent individual from the population.
     * @param second index of the second parent individual from the population.
     *
     * @return the offspring generated by crossing over the parent individuals.
     */
    private int[][] crossover(int first, int second) {
        int[][] offspring = new int[2][SIZE];

        return offspring;
    }

    /**
     * [Exercise 4]
     *
     * Mutation operator.
     *
     * @param parent index of the parent individual from the population.
     *
     * @return the offspring generated by mutating the parent individual.
     */
    private int[] mutation(int parent) {
        int[] offspring = new int[SIZE];

        return offspring;
    }

    /**
     * Returns the index of the selected parent using a roulette wheel.
     *
     * @return the index of the selected parent using a roulette wheel.
     */
    private int select() {
        // prepares for roulette wheel selection
        double[] roulette = new double[POPULATION_SIZE];
        double total = 0;

        for (int i = 0; i < POPULATION_SIZE; i++) {
            roulette[i] = 1.0 / (double) fitness[i];
            total += roulette[i];
        }

        double cumulative = 0.0;

        for (int i = 0; i < POPULATION_SIZE; i++) {
            roulette[i] = cumulative + (roulette[i] / total);
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
     * Copies the offspring array into the new population.
     *
     * @param newPopulation the new GA population.
     * @param offspring the offspring to copy.
     * @param position the position in the population array.
     */
    private void copy(int[][] newPopulation, int[] offspring, int position) {
        for (int i = 0; i < SIZE; i++) {
            newPopulation[position][i] = offspring[i];
        }
    }

    /**
     * Loads the TSP file. This method will initialise the variables
     * size and COST.
     */
    private void load(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = null;

            int row = 0;
            int column = 0;
            boolean read = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("DIMENSION")) {
                    String[] tokens = line.split(":");
                    SIZE = Integer.parseInt(tokens[1].trim());
                    COST = new int[SIZE][SIZE];
                }
                else if (line.startsWith("EDGE_WEIGHT_TYPE")) {
                    String[] tokens = line.split(":");
                    if (tokens.length < 2 || !tokens[1].trim().equals("EXPLICIT"))
                    {
                        throw new RuntimeException("Invalid EDGE_WEIGHT_TYPE: " + tokens[1]);
                    }
                }
                else if (line.startsWith("EDGE_WEIGHT_FORMAT")) {
                    String[] tokens = line.split(":");
                    if (tokens.length < 2 || !tokens[1].trim().equals("LOWER_DIAG_ROW"))
                    {
                        throw new RuntimeException("Invalid EDGE_WEIGHT_FORMAT: " + tokens[1]);
                    }
                }
                else if (line.startsWith("EDGE_WEIGHT_SECTION")) {
                    read = true;
                }
                else if (line.startsWith("EOF")) {
                    break;
                }
                else if (read) {
                    String[] tokens = line.split("\\s");

                    for (int i = 0; i < tokens.length; i++)
                    {
                        String v = tokens[i].trim();

                        if (v.length() > 0)
                        {
                            int value = Integer.parseInt(tokens[i].trim());
                            COST[row][column] = value;
                            column++;

                            if (value == 0)
                            {
                                row++;
                                column = 0;
                            }
                        }
                    }
                }
            }

            reader.close();

            // completes the cost matrix
            for (int i = 0; i < COST.length; i++) {
                for (int j = (i + 1); j < COST.length; j++) {
                    COST[i][j] = COST[j][i];
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Could not load file: " + filename, e);
        }
    }
}
