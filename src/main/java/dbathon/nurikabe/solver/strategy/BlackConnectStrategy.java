package dbathon.nurikabe.solver.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import dbathon.nurikabe.board.Board;
import dbathon.nurikabe.board.Cell;
import dbathon.nurikabe.board.CellColor;
import dbathon.nurikabe.solver.SolverStrategy;

/**
 * For each unknown neighbor cell of a black group, assume that it is white and then try to find a
 * path to another black group, if there is none, than that cell must be black.
 */
public class BlackConnectStrategy implements SolverStrategy {

  @Override
  public void improveSolution(Board board) {
    final Set<Set<Cell>> blackGroups = board.getGroups(CellColor.BLACK);
    if (blackGroups.size() < 2) {
      // we need at least 2 groups
      return;
    }
    for (final Set<Cell> blackGroup : blackGroups) {
      final Set<Cell> unknownNeighbors = new HashSet<Cell>();
      for (final Cell blackCell : blackGroup) {
        for (final Cell neighbor : board.getNeighbors(blackCell)) {
          if (neighbor.isUnknown()) {
            unknownNeighbors.add(neighbor);
          }
        }
      }

      for (final Cell unknownNeighbor : unknownNeighbors) {
        if (!pathToOtherGroupExists(board, blackGroup, unknownNeighbor)) {
          unknownNeighbor.setBlack();
          // return when one is found, we would need to start from the beginning anyway...
          return;
        }
      }
    }
  }

  private boolean pathToOtherGroupExists(Board board, Set<Cell> blackGroup, Cell tabooCell) {
    // "flood fill" starting from one of the blackCells
    final Cell startCell = blackGroup.iterator().next();
    final Set<Cell> seen = new HashSet<Cell>();
    final List<Cell> todo = new ArrayList<Cell>();
    todo.add(startCell);
    while (!todo.isEmpty()) {
      final Cell current = todo.remove(todo.size() - 1);
      if (!seen.add(current)) {
        // already done
        continue;
      }
      for (final Cell neighbor : board.getNeighbors(current)) {
        if (neighbor.isBlack() && !blackGroup.contains(neighbor)) {
          // we found a path to another black group
          return true;
        }
        else if (!neighbor.isWhite() && !neighbor.equals(tabooCell)) {
          todo.add(neighbor);
        }
      }
    }
    // nothing found
    return false;
  }

}