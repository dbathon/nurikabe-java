package dbathon.nurikabe.solver.strategy;

import java.util.Map.Entry;
import java.util.Set;
import dbathon.nurikabe.board.Board;
import dbathon.nurikabe.board.Cell;
import dbathon.nurikabe.board.FixedCell;
import dbathon.nurikabe.solver.SolverStrategy;

/**
 * If a white group is complete all neighboring cells must be black.
 */
public class HullStrategy implements SolverStrategy {

  @Override
  public void improveSolution(Board board) {
    for (final Entry<FixedCell, Set<Cell>> entry : board.getWhiteGroups().entrySet()) {
      final FixedCell fixedCell = entry.getKey();
      final Set<Cell> cells = entry.getValue();
      if (fixedCell.getNumber() == cells.size()) {
        for (final Cell cell : cells) {
          for (final Cell neighbor : board.getNeighbors(cell)) {
            if (neighbor.isUnknown()) {
              neighbor.setBlack();
            }
          }
        }
      }
    }
  }

}
