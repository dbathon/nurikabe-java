package dbathon.nurikabe.solver.strategy;

import java.util.HashSet;
import java.util.Set;
import dbathon.nurikabe.board.Board;
import dbathon.nurikabe.board.Cell;
import dbathon.nurikabe.board.FixedCell;
import dbathon.nurikabe.solver.SolverStrategy;

/**
 * Unknown cells having neighbours with different non-null FixedCells -> black
 */
public class NonConnectedWhiteNeighborsStrategy implements SolverStrategy {

  @Override
  public void improveSolution(Board board) {
    for (final Cell cell : board) {
      if (cell.isUnknown()) {
        final Set<FixedCell> fixedCellNeighbors = new HashSet<FixedCell>();
        for (final Cell neighbor : board.getNeighbors(cell)) {
          fixedCellNeighbors.add(neighbor.getFixedCell());
        }
        fixedCellNeighbors.remove(null);
        if (fixedCellNeighbors.size() > 1) {
          cell.setBlack();
        }
      }
    }
  }

}
