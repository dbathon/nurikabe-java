package dbathon.nurikabe.solver;

import dbathon.nurikabe.board.Board;

public interface SolverStrategy {

  /**
   * The strategy implementation should try to improve the existing partial solution on the given
   * <code>board</code>.
   * 
   * @param board
   */
  void improveSolution(Board board);

}
