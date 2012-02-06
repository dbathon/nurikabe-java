package dbathon.nurikabe.solver.strategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import dbathon.nurikabe.board.Board;
import dbathon.nurikabe.board.Cell;
import dbathon.nurikabe.board.FixedCell;
import dbathon.nurikabe.solver.SolverStrategy;

/**
 * Unreachable unknown cells -> black.
 * <p>
 * White cells that do not yet belong to a fixed cell, but are only reachable from one fixed cell ->
 * they belong to that fixed cell.
 */
public class ReachabilityStrategy implements SolverStrategy {

  @Override
  public void improveSolution(Board board) {
    // first we need to ensure that all cells have fixed cell set (if possible)
    board.connectWhiteCells();

    // for each cell, from which other cells it can be reached
    final Map<Cell, Set<FixedCell>> reachableMap = new HashMap<Cell, Set<FixedCell>>();

    // initialize the map for each open cell
    for (final Cell cell : board) {
      if (isOpen(cell)) {
        reachableMap.put(cell, new HashSet<FixedCell>());
      }
    }

    // build reachableMap
    for (final Entry<FixedCell, Set<Cell>> entry : board.getWhiteGroupsWithFixedCell().entrySet()) {
      final FixedCell fixedCell = entry.getKey();
      final Set<Cell> whiteGroup = entry.getValue();
      final int missingCells = fixedCell.getNumber() - whiteGroup.size();
      if (missingCells > 0) {
        for (final Cell cell : whiteGroup) {
          findReachable(board, fixedCell, cell, reachableMap, missingCells);
        }
      }
    }

    for (final Entry<Cell, Set<FixedCell>> entry : reachableMap.entrySet()) {
      final Cell cell = entry.getKey();
      final Set<FixedCell> reachableFrom = entry.getValue();
      if (reachableFrom.isEmpty()) {
        cell.setBlack();
      }
      else if (cell.isWhite() && reachableFrom.size() == 1) {
        cell.setFixedCell(reachableFrom.iterator().next());
      }
    }
  }

  private boolean isOpen(Cell cell) {
    return cell.isUnknown() || (cell.isWhite() && cell.getFixedCell() == null);
  }

  private void findReachable(Board board, FixedCell fixedCell, Cell cell,
      Map<Cell, Set<FixedCell>> reachableMap, int steps) {
    if (steps < 1) return;

    outer: for (final Cell neighbor : board.getNeighbors(cell)) {
      if (isOpen(neighbor)) {
        // check if any neighbor has a different fixed cell
        for (final Cell neighborNeighbor : board.getNeighbors(neighbor)) {
          if (neighborNeighbor.getFixedCell() != null
              && !neighborNeighbor.getFixedCell().equals(fixedCell)) {
            // neighboring different fixed cells are not possible
            continue outer;
          }
        }
        // okay, neighbor is reachable by fixedCell
        reachableMap.get(neighbor).add(fixedCell);
        // continue search
        findReachable(board, fixedCell, neighbor, reachableMap, steps - 1);
      }
    }
  }

}
