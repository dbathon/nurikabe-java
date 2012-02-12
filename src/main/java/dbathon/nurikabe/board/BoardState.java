package dbathon.nurikabe.board;

import java.util.ArrayList;
import java.util.List;

/**
 * Can be used to save and later restore a particular state of a {@link Board}.
 */
public class BoardState {

  private final Board board;
  private final List<CellColor> colors = new ArrayList<CellColor>();
  private final List<FixedCell> fixedCells = new ArrayList<FixedCell>();

  public BoardState(Board board) {
    this.board = board;

    for (int x = 0; x < board.getWidth(); ++x) {
      for (int y = 0; y < board.getHeight(); ++y) {
        final Cell cell = board.getCell(x, y);
        colors.add(cell.getColor());
        fixedCells.add(cell.getFixedCell());
      }
    }
  }

  public Board getBoard() {
    return board;
  }

  public void restoreState() {
    int index = 0;
    for (int x = 0; x < board.getWidth(); ++x) {
      for (int y = 0; y < board.getHeight(); ++y) {
        final Cell cell = board.getCell(x, y);
        final CellColor color = colors.get(index);
        final FixedCell fixedCell = fixedCells.get(index);
        if (fixedCell != null) {
          // first set the color
          cell.setColor(color);
          cell.setFixedCell(fixedCell);
        }
        else {
          // first set the fixed cell
          cell.setFixedCell(fixedCell);
          cell.setColor(color);
        }
        ++index;
      }
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((board == null) ? 0 : board.hashCode());
    result = prime * result + ((colors == null) ? 0 : colors.hashCode());
    result = prime * result + ((fixedCells == null) ? 0 : fixedCells.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final BoardState other = (BoardState) obj;
    if (board == null) {
      if (other.board != null) return false;
    }
    else if (!board.equals(other.board)) return false;
    if (colors == null) {
      if (other.colors != null) return false;
    }
    else if (!colors.equals(other.colors)) return false;
    if (fixedCells == null) {
      if (other.fixedCells != null) return false;
    }
    else if (!fixedCells.equals(other.fixedCells)) return false;
    return true;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("BoardState(");
    for (final CellColor color : colors) {
      sb.append(color.name().charAt(0));
    }
    return sb.append(")").toString();
  }

}
