package dbathon.nurikabe.solver.strategy;

import java.util.Arrays;
import java.util.List;

import dbathon.nurikabe.board.Board;
import dbathon.nurikabe.board.Cell;
import dbathon.nurikabe.solver.SolverStrategy;

/**
 * 2x2 block with three black -> remaining is white.
 */
public class NoBlackBlockStrategy implements SolverStrategy {

  @Override
  public void improveSolution(Board board) {
    for (int x = 0; x < board.getWidth() - 1; ++x) {
      for (int y = 0; y < board.getHeight() - 1; ++y) {
        final List<Cell> blockCells =
            Arrays.asList(board.getCell(x, y), board.getCell(x + 1, y), board.getCell(x, y + 1),
                board.getCell(x + 1, y + 1));

        if (blockCells.stream().filter(cell -> cell.isBlack()).count() == 3) {
          blockCells.stream().filter(cell -> cell.isUnknown()).forEach(cell -> cell.setWhite());
        }
      }
    }
  }

}
