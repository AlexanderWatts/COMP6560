import org.epochx.gp.model.EvenParity;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.op.crossover.UniformPointCrossover;
import org.epochx.life.GenerationAdapter;
import org.epochx.life.Life;
import org.epochx.op.selection.TournamentSelector;
import org.epochx.stats.StatField;
import org.epochx.stats.Stats;
import org.epochx.tools.random.MersenneTwisterFast;

public class Main {
  public static void main(String[] args) {
    run();    
  }

  public static void run() {
    GPModel model = new EvenParity(4);

    model.setPopulationSize(500);
    model.setNoGenerations(100);
    model.setMaxDepth(8);

    model.setCrossover(new UniformPointCrossover(model));
    model.setProgramSelector(new TournamentSelector(model, 7));
    model.setRNG(new MersenneTwisterFast());

    Life.get().addGenerationListener(new GenerationAdapter() {
      public void onGenerationEnd() {
        Stats.get().print(StatField.GEN_NUMBER, StatField.GEN_FITNESS_MIN, StatField.GEN_FITTEST_PROGRAM);
      }
    });

    model.run();
  }
}
