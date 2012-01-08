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
    setUnknown();
    white = true;
  }

  @Override
  public boolean isBlack() {
    return black;
  }

  @Override
  public void setBlack() {
    setUnknown();
    black = true;
  }

  @Override
  public void setUnknown() {
    white = false;
    black = false;
  }

  @Override
  public FixedCell getFixedCell() {
    return fixedCell;
  }

  @Override
  public void setFixedCell(FixedCell fixedCell) {
    this.fixedCell = fixedCell;
  }

  @Override
  public String toString() {
    return "SimpleCell(" + (isWhite() ? "w" : (isBlack() ? "b" : "?")) + ")";
  }

}
