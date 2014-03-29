package dbathon.nurikabe.solver.strategy;

import dbathon.nurikabe.board.Board;
import dbathon.nurikabe.board.Cell;
import dbathon.nurikabe.board.FixedCell;
import dbathon.nurikabe.solver.SolverStrategy;

/**
 * TODO: this is not ideal, it should also work with 2-cells at the border and probably should be
 * generalized.
 */
public class TwoAndBlackStrategy implements SolverStrategy {

  @Override
  public void improveSolution(Board board) {
    for (int x = 1; x < board.getWidth() - 1; ++x) {
      for (int y = 1; y < board.getHeight() - 1; ++y) {
        final Cell cell = board.getCell(x, y);
        if (cell.isFixed()) {
          final FixedCell fixed = (FixedCell) cell;
          if (fixed.getNumber() == 2) {
            final Cell top = board.getCell(x, y - 1);
            final Cell left = board.getCell(x - 1, y);
            final Cell right = board.getCell(x + 1, y);
            final Cell bottom = board.getCell(x, y + 1);
            if (top.isBlack() && right.isBlack()) {
              board.getCell(x - 1, y + 1).setBlack();
            }
            if (right.isBlack() && bottom.isBlack()) {
              board.getCell(x - 1, y - 1).setBlack();
            }
            if (bottom.isBlack() && left.isBlack()) {
              board.getCell(x + 1, y - 1).setBlack();
            }
            if (left.isBlack() && top.isBlack()) {
              board.getCell(x + 1, y + 1).setBlack();
            }
          }
        }
      }
    }
  }

}
