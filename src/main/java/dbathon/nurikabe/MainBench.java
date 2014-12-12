package dbathon.nurikabe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dbathon.nurikabe.board.Board;
import dbathon.nurikabe.board.BoardUtil;
import dbathon.nurikabe.solver.Solver;
import dbathon.nurikabe.solver.SolverStrategy;
import dbathon.nurikabe.solver.strategy.AllValidIslandsStrategy;
import dbathon.nurikabe.solver.strategy.BlackConnectStrategy;
import dbathon.nurikabe.solver.strategy.ExpandStrategy;
import dbathon.nurikabe.solver.strategy.NoBlackBlockStrategy;
import dbathon.nurikabe.solver.strategy.TwoAndBlackStrategy;

public class MainBench {

  private static int successCnt = 0, failCnt = 0;

  public static void main(String[] args) {
    final SolverStrategy[] strategies =
        { new AllValidIslandsStrategy(), new ExpandStrategy(), new NoBlackBlockStrategy(),
            new BlackConnectStrategy(), new TwoAndBlackStrategy() };

    final Solver solver = new Solver(Arrays.asList(strategies));

    final List<Long> times = new ArrayList<>();

    for (int i = 0; i < 10; ++i) {
      final long start = System.currentTimeMillis();
      for (final String boardString : Main.BOARDS_9x9) {
        test(solver, "9:9:" + boardString);
      }
      System.out.println("success: " + successCnt + " - fail: " + failCnt);
      times.add(System.currentTimeMillis() - start);
    }

    System.out.println(times);
  }

  private static void test(Solver solver, String boardString) {
    final Board board = BoardUtil.parseStringToBoard(boardString, true);
    if (solver.tryToSolve(board)) {
      successCnt++;
    }
    else {
      failCnt++;
    }
  }

}
