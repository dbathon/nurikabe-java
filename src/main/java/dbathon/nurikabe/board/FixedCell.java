package dbathon.nurikabe.board;

public class FixedCell extends Cell {

  private final int number;

  public FixedCell(Board board, int x, int y, int number) {
    super(board, x, y);
    if (number < 1) {
      throw new IllegalArgumentException("number");
    }
    this.number = number;
  }

  @Override
  public boolean isFixed() {
    return true;
  }

  @Override
  public boolean isWhite() {
    return true;
  }

  @Override
  public boolean isBlack() {
    return false;
  }

  @Override
  public void setWhite() {}

  @Override
  public void setBlack() {
    throw new IllegalArgumentException();
  }

  @Override
  public void setUnknown() {
    throw new IllegalArgumentException();
  }

  @Override
  public FixedCell getFixedCell() {
    return this;
  }

  @Override
  public void setFixedCell(FixedCell fixedCell) {
    if (fixedCell != this) {
      throw new IllegalArgumentException();
    }
  }

  public int getNumber() {
    return number;
  }

  @Override
  public String toString() {
    return "FixedCell(" + number + ")";
  }

}
