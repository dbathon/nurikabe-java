package dbathon.nurikabe.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dbathon.nurikabe.board.Board;
import dbathon.nurikabe.board.BoardState;

public class Solver {

  private final List<SolverStrategy> strategies;

  public Solver(List<SolverStrategy> strategies) {
    this.strategies = new ArrayList<>(strategies);
  }

  /**
   * @param board
   * @param solverEvents
   * @return whether the board could be solved
   */
  public boolean tryToSolve(Board board, Collection<SolverEvents> solverEvents) {
    for (final SolverEvents events : solverEvents) {
      events.onBeginSolve(board, this);
    }

    while (true) {
      final BoardState beforeState = new BoardState(board);

      for (final SolverStrategy strategy : strategies) {
        final long startNanos = System.nanoTime();
        strategy.improveSolution(board);
        final long nanos = System.nanoTime() - startNanos;
        for (final SolverEvents events : solverEvents) {
          events.onStrategyExecuted(board, strategy, nanos, this);
        }
      }

      if (board.isSolution()) {
        return true;
      }
      else {
        final BoardState afterState = new BoardState(board);
        if (afterState.equals(beforeState)) {
          // no improvement
          break;
        }
      }
    }

    return false;
  }

  /**
   * @param board
   * @return whether the board could be solved
   */
  public boolean tryToSolve(Board board) {
    return tryToSolve(board, Collections.<SolverEvents>emptyList());
  }

}
