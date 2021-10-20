import java.util.*;

import org.epochx.epox.*;
import org.epochx.epox.bool.*;
import org.epochx.life.*;
import org.epochx.stats.*;
import org.epochx.gp.representation.*;

import org.epochx.gp.model.GPModel;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.util.BoolUtils;

public class Mux6 extends GPModel {
  private BooleanVariable d3;
  private BooleanVariable d2;
  private BooleanVariable d1;
  private BooleanVariable d0;
  private BooleanVariable a1;
  private BooleanVariable a0;

  // The boolean inputs/outputs that we will test solutions against.
  private boolean[][] inputs;
  private boolean[] outputs;

  public Mux6() {
    // Construct the variables into fields.
    d3 = new BooleanVariable("d3");
    d2 = new BooleanVariable("d2");
    d1 = new BooleanVariable("d1");
    d0 = new BooleanVariable("d0");
    a1 = new BooleanVariable("a1");
    a0 = new BooleanVariable("a0");
    List<Node> syntax = new ArrayList<Node>();

    // Functions.
    syntax.add(new IfFunction());
    syntax.add(new AndFunction());
    syntax.add(new OrFunction());
    syntax.add(new NotFunction());

    // Terminals.
    syntax.add(d3);
    syntax.add(d2);
    syntax.add(d1);
    syntax.add(d0);
    syntax.add(a1);
    syntax.add(a0);
    setSyntax(syntax);

    // Generate set of test inputs and corresponding correct output.
    inputs = BoolUtils.generateBoolSequences(6);
    outputs = generateOutputs(inputs);
  }

  @Override
  public double getFitness(CandidateProgram p) {
    GPCandidateProgram program = (GPCandidateProgram) p;

    double score = 0;
    for (int i = 0; i < inputs.length; i++) {
      // Set the variables.
      a0.setValue(inputs[i][0]);
      a1.setValue(inputs[i][1]);
      d0.setValue(inputs[i][2]);
      d1.setValue(inputs[i][3]);
      d2.setValue(inputs[i][4]);
      d3.setValue(inputs[i][5]);

      Boolean result = (Boolean) program.evaluate();
      if (result == outputs[i]) {
        score++;
      }
    }
    return 64 - score;
  }

  /*
   * Generates the correct outputs for the 6-bit multiplexer from the given inputs
   * to test against.
   */
  private boolean[] generateOutputs(boolean[][] in) {
    boolean[] out = new boolean[in.length];

    for (int i = 0; i < in.length; i++) {
      if (in[i][0] && in[i][1]) {
        out[i] = in[i][2];
      } else if (in[i][0] && !in[i][1]) {
        out[i] = in[i][3];
      } else if (!in[i][0] && in[i][1]) {
        out[i] = in[i][4];
      } else if (!in[i][0] && !in[i][1]) {
        out[i] = in[i][5];
      }
    }
    return out;
  }

  public static void main(String[] args) {
    Life.get().addGenerationListener(new GenerationAdapter() {
      public void onGenerationEnd() {
        Stats.get().print(StatField.GEN_NUMBER, StatField.GEN_FITNESS_MIN, StatField.GEN_FITTEST_PROGRAM);
      }
    });

    new Mux6().run();
  }
}