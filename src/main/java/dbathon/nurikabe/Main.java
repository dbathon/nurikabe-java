package dbathon.nurikabe;

import java.util.Arrays;
import dbathon.nurikabe.board.Board;
import dbathon.nurikabe.board.BoardState;
import dbathon.nurikabe.board.BoardUtil;
import dbathon.nurikabe.solver.Solver;
import dbathon.nurikabe.solver.SolverEvents;
import dbathon.nurikabe.solver.SolverStrategy;
import dbathon.nurikabe.solver.strategy.ExpandStrategy;
import dbathon.nurikabe.solver.strategy.FillupStrategy;
import dbathon.nurikabe.solver.strategy.HullStrategy;
import dbathon.nurikabe.solver.strategy.NoBlackBlockStrategy;
import dbathon.nurikabe.solver.strategy.NonConnectedWhiteNeighborsStrategy;

public class Main {

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

    final SolverStrategy[] strategies =
        {
            new HullStrategy(), new NonConnectedWhiteNeighborsStrategy(), new ExpandStrategy(),
            new NoBlackBlockStrategy(), new FillupStrategy()
        };

    final Solver solver = new Solver(Arrays.asList(strategies));

    final Board board2 = BoardUtil.parseStringToBoard("2:2:.1..");
    trySolve(solver, board2);

    final Board board3 = BoardUtil.parseStringToBoard("2:2:.4..");
    trySolve(solver, board3);

    trySolve(solver, board);

    final Board board4 =
        BoardUtil.parseStringToBoard("9:9:..............4.......3.3.3.........9.......3.........2.3.4.......7..............");

    trySolve(solver, board4);
    System.out.println("=================================================================");

    // a small hint...
    board4.getCell(7, 1).setBlack();
    trySolve(solver, board4);
    System.out.println("=================================================================");

    // another small hint...
    board4.getCell(6, 7).setBlack();
    trySolve(solver, board4);
  }

  private static class LoggingEvents implements SolverEvents {

    private BoardState lastState;

    @Override
    public void onBeginSolve(Board board, Solver solver) {
      lastState = new BoardState(board);
      System.out.println("beginning to solve:\n" + board);
    }

    @Override
    public void onStrategyExecuted(Board board, SolverStrategy strategy, Solver solver) {
      final BoardState newState = new BoardState(board);
      if (!newState.equals(lastState)) {
        System.out.println("solution improved by " + strategy + ":\n" + board);
        lastState = newState;
      }
    }

  }

  private static void trySolve(Solver solver, Board board) {
    System.out.println("-------------------------------------------------");
    System.out.println("try solve: "
        + solver.tryToSolve(board, Arrays.<SolverEvents>asList(new LoggingEvents())));
  }

}
