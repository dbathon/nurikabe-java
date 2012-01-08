package dbathon.nurikabe.board;

public abstract class Cell {

  private final Board board;

  private final int x;
  private final int y;

  public Cell(Board board, int x, int y) {
    this.board = board;
    this.x = x;
    this.y = y;
  }

  public Board getBoard() {
    return board;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public abstract boolean isFixed();

  public abstract boolean isWhite();

  public abstract void setWhite();

  public abstract boolean isBlack();

  public abstract void setBlack();

  public boolean isUnknown() {
    return !isWhite() && !isBlack();
  }

  public abstract void setUnknown();

  /**
   * @return the {@link FixedCell} this cell "belongs to" or <code>null</code> if there is non
   */
  public abstract FixedCell getFixedCell();

  public abstract void setFixedCell(FixedCell fixedCell);

  public boolean isNeighbor(Cell cell) {
    if (getBoard() == cell.getBoard()) {
      if (getX() == cell.getX()) {
        return Math.abs(getY() - cell.getY()) == 1;
      }
      else if (getY() == cell.getY()) {
        return Math.abs(getX() - cell.getX()) == 1;
      }
    }
    return false;
  }

}
