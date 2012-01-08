package dbathon.nurikabe.board;

public class SimpleCell extends Cell {

  private boolean white = false;
  private boolean black = false;

  private FixedCell fixedCell = null;

  public SimpleCell(Board board, int x, int y) {
    super(board, x, y);
  }

  @Override
  public boolean isFixed() {
    return false;
  }

  @Override
  public boolean isWhite() {
    return white;
  }

  @Override
  public void setWhite() {
    white = true;
    black = false;
  }

  @Override
  public boolean isBlack() {
    return black;
  }

  private IllegalArgumentException fixedCellException() {
    return new IllegalArgumentException("only white cells can be associated with a fixed cell");
  }

  @Override
  public void setBlack() {
    if (getFixedCell() != null) {
      throw fixedCellException();
    }
    white = false;
    black = true;
  }

  @Override
  public void setUnknown() {
    if (getFixedCell() != null) {
      throw fixedCellException();
    }
    white = false;
    black = false;
  }

  @Override
  public FixedCell getFixedCell() {
    return fixedCell;
  }

  @Override
  public void setFixedCell(FixedCell fixedCell) {
    if (!isWhite() && fixedCell != null) {
      throw fixedCellException();
    }
    this.fixedCell = fixedCell;
  }

  @Override
  public String toString() {
    return "SimpleCell(" + (isWhite() ? "w" : (isBlack() ? "b" : "?")) + ")";
  }

}
