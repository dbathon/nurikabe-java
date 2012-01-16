package dbathon.nurikabe.solver.strategy;

import dbathon.nurikabe.board.Board;
import dbathon.nurikabe.board.Cell;
import dbathon.nurikabe.solver.SolverStrategy;

/**
 * If all white cells are there or all black cells are there then just set all unknown cells to the
 * other color.
 */
public class FillupStrategy implements SolverStrategy {

  @Override
  public void improveSolution(Board board) {
    if (board.getBlackCount() == board.getSolutionBlackCount()) {
      // make every unknown cell white
      for (final Cell cell : board) {
        if (cell.isUnknown()) {
          cell.setWhite();
        }
      }
    }
    else if (board.getWhiteCount() == board.getSolutionWhiteCount()) {
      // make every unknown cell black
      for (final Cell cell : board) {
        if (cell.isUnknown()) {
          cell.setBlack();
        }
      }
    }
  }

}
