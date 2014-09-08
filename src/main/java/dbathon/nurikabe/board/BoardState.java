package dbathon.nurikabe.board;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Can be used to save and later restore a particular state of a {@link Board}.
 */
public class BoardState {

  private static class CellState {
    final Cell cell;
    final CellColor color;
    final FixedCell fixedCell;

    public CellState(Cell cell, CellColor color, FixedCell fixedCell) {
      this.cell = cell;
      this.color = color;
      this.fixedCell = fixedCell;
    }

    void restore() {
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
    }

    @Override
    public int hashCode() {
      return Objects.hash(cell, color, fixedCell);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
      final CellState other = (CellState) obj;
      return cell.equals(other.cell) && color == other.color
          && Objects.equals(fixedCell, other.fixedCell);
    }
  }

  private final Board board;
  private final List<CellState> cellStates;

  public BoardState(Board board) {
    this.board = board;

    cellStates =
        board.getCells().map(cell -> new CellState(cell, cell.getColor(), cell.getFixedCell()))
            .collect(Collectors.toList());
  }

  public Board getBoard() {
    return board;
  }

  public void restoreState() {
    cellStates.forEach(CellState::restore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(board, cellStates);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final BoardState other = (BoardState) obj;
    return board.equals(other.board) && cellStates.equals(other.cellStates);
  }

  @Override
  public String toString() {
    return cellStates.stream().map(cellState -> cellState.color.getShortName())
        .collect(Collectors.joining("", "BoardState(", ")"));
  }

}
