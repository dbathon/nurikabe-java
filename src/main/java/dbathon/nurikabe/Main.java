package dbathon.nurikabe;

import java.util.Arrays;
import dbathon.nurikabe.board.Board;
import dbathon.nurikabe.board.BoardState;
import dbathon.nurikabe.board.BoardUtil;
import dbathon.nurikabe.solver.Solver;
import dbathon.nurikabe.solver.SolverEvents;
import dbathon.nurikabe.solver.SolverStrategy;
import dbathon.nurikabe.solver.strategy.AllValidIslandsStrategy;
import dbathon.nurikabe.solver.strategy.ExpandStrategy;
import dbathon.nurikabe.solver.strategy.NoBlackBlockStrategy;

public class Main {

  private static final String[] BOARDS_9x9 = {
      "..2.......5...............1.....5.3...........2.5.....3...............1.......3..",
      "............6....7.......6....6...................2....1.......2....2............",
      "....6...3............4.............4.5.....5.2.............2............5...1....",
      ".........6.................2...1.4.............5.8...1.................3.........",
      ".............4..1..9.........3.......2.....2.......1.........2..5..6.............",
      ".................8......3..5......4...........4......5..3......8.................",
      "..............4.......3.3.3.........9.......3.........2.3.4.......7.............."
  };

  public static void main(String[] args) {
    final Board board = BoardUtil.parseStringToBoard("5:5:.... 3.  .7.... ..... 2 ..1....");
    System.out.println(board);
    final BoardState boardState = new BoardState(board);
    System.out.println(boardState);
    board.getCell(1, 1).setWhite();
    board.getCell(1, 0).setBlack();
    System.out.println(board);
    boardState.restoreState();
    System.out.println(board);

    final SolverStrategy[] strategies = {
        new AllValidIslandsStrategy(), new ExpandStrategy(), new NoBlackBlockStrategy(),
    };

    final Solver solver = new Solver(Arrays.asList(strategies));

    final Board board2 = BoardUtil.parseStringToBoard("2:2:.1..");
    trySolve(solver, board2);

    final Board board3 = BoardUtil.parseStringToBoard("2:2:.4..");
    trySolve(solver, board3);

    trySolve(solver, board);

    for (final String boardString : BOARDS_9x9) {
      test(solver, "9:9:" + boardString);
    }
  }

  private static void test(Solver solver, String boardString) {
    final Board board = BoardUtil.parseStringToBoard(boardString);
    trySolve(solver, board);
  }

  private static class LoggingEvents implements SolverEvents {

    private BoardState lastState;

    @Override
    public void onBeginSolve(Board board, Solver solver) {
      lastState = new BoardState(board);
      // System.out.println("beginning to solve:\n" + board);
    }

    @Override
    public void onStrategyExecuted(Board board, SolverStrategy strategy, Solver solver) {
      final BoardState newState = new BoardState(board);
      if (!newState.equals(lastState)) {
        // System.out.println("solution improved by " + strategy + ":\n" + board);
        lastState = newState;
      }
    }

  }

  private static void trySolve(Solver solver, Board board) {
    System.out.println("-------------------------------------------------");
    System.out.println(board);
    System.out.println("try solve: "
        + solver.tryToSolve(board, Arrays.<SolverEvents>asList(new LoggingEvents())));
    System.out.println(board);
  }

}
