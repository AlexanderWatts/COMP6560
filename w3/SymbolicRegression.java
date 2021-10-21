import java.util.List;
import java.util.ArrayList;

import org.epochx.epox.DoubleLiteral;
import org.epochx.epox.DoubleVariable;
import org.epochx.epox.Node;
import org.epochx.epox.dbl.AddFunction;
import org.epochx.epox.dbl.MultiplyFunction;
import org.epochx.epox.dbl.ProtectedDivisionFunction;
import org.epochx.epox.dbl.SubtractFunction;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.life.GenerationAdapter;
import org.epochx.life.Life;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.StatField;
import org.epochx.stats.Stats;

public class SymbolicRegression extends GPModel {
  private DoubleVariable x;

  private static Double[] inputs = {
    -10.,
    -9.,
    -8.,
    -7.,
    -6.,
    -5.,
    -4.,
    -3.,
    -2.,
    -1.,
    0.,
    1.,
    2.,
    3.,
    4.,
    5.,
    6.,
    7.,
    8.,
    9.,
    10.,
  };

  private static Double[] outputs = {
    153., 
    120., 
    91.,
    66.,
    45.,
    28.,
    15.,
    6.,
    1.,
    0.,
    3.,
    10.,
    21.,
    36.,
    55.,
    78.,
    105.,
    136.,
    171.,
    210.,
    253.,
  };

  public SymbolicRegression() {
    x = new DoubleVariable("x");

    List<Node> syntax = new ArrayList<>();

    syntax.add(x);
    syntax.add(new DoubleLiteral((double) 0));
    syntax.add(new DoubleLiteral((double) 1));
    syntax.add(new DoubleLiteral((double) 2));
    syntax.add(new DoubleLiteral((double) 3));
    syntax.add(new DoubleLiteral((double) 4));

    syntax.add(new AddFunction());
    syntax.add(new SubtractFunction());
    syntax.add(new MultiplyFunction());
    syntax.add(new ProtectedDivisionFunction());

    setSyntax(syntax);
  }

  /**
   * Target solution
   * 
   * Correct program we need to generate - 2x^2+5x+3
   */
  @Override
  public double getFitness(CandidateProgram p) {
    GPCandidateProgram program = (GPCandidateProgram) p;
    
    double sum = 0;

    for (int i = 0; i < inputs.length; i++) {
      x.setValue(inputs[i]);

      System.out.println(program.evaluate());

      Double result = (Double) program.evaluate();

      double score = Math.pow(result - outputs[i], 2);

      sum += score;
    }

    double mse = sum / inputs.length;

    return mse;
  }

  public static void main(String[] args) {
    Life.get().addGenerationListener(new GenerationAdapter() {
      public void onGenerationEnd() {
        System.out.println("--------------------Start-----------------------");
        Stats.get().print(StatField.GEN_NUMBER);
        Stats.get().print(StatField.GEN_FITNESS_MIN);
        Stats.get().print(StatField.GEN_FITTEST_PROGRAM);
        System.out.println("--------------------End-----------------------");
      }
    });

    new SymbolicRegression().run();
  }
}
