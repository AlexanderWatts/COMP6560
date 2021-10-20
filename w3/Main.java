import org.epochx.gp.model.EvenParity;
import org.epochx.gp.model.GPModel;
import org.epochx.life.GenerationAdapter;
import org.epochx.life.Life;
import org.epochx.stats.StatField;
import org.epochx.stats.Stats;

public class Main {
  public static void main(String[] args) {
    run();    
  }

  public static void run() {
    GPModel model = new EvenParity(4);

    Life.get().addGenerationListener(new GenerationAdapter() {
      public void onGenerationEnd() {
        Stats.get().print(StatField.GEN_NUMBER, StatField.GEN_FITNESS_MIN, StatField.GEN_FITTEST_PROGRAM);
      }
    });

    model.run();
  }
}
