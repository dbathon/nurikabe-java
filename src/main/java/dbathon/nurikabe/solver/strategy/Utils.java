package dbathon.nurikabe.solver.strategy;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import dbathon.nurikabe.board.Board;
import dbathon.nurikabe.board.Cell;
import dbathon.nurikabe.board.CellSet;

public final class Utils {

  private Utils() {}

  /**
   * @param board
   * @param cells
   * @return all unknown neighbors of the given <code>cells</code>
   */
  public static Set<Cell> unknownNeighbors(Board board, Collection<Cell> cells) {
    return cells.stream().flatMap(cell -> board.getNeighbors(cell).filter(Cell::isUnknown))
        .collect(Collectors.toCollection(() -> new CellSet(board)));
  }

}
