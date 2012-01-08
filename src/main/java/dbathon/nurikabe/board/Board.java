package dbathon.nurikabe.board;

public class Board {

  private final int width;
  private final int height;

  private final int solutionWhiteCount;

  private final Cell[] cells;

  public Board(BoardBuilder boardBuilder) {
    width = boardBuilder.getWidth();
    height = boardBuilder.getHeight();

    cells = new Cell[getCellCount()];
    int numberSum = 0;
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < height; ++y) {
        final int number = boardBuilder.getNumber(x, y);
        if (number == 0) {
          cells[coordsToIndex(x, y)] = new SimpleCell(this, x, y);
        }
        else {
          cells[coordsToIndex(x, y)] = new FixedCell(this, x, y, number);
        }
        numberSum += number;
      }
    }

    if (numberSum <= 0) {
      throw new IllegalArgumentException("no white fixed cells");
    }
    else if (numberSum > getCellCount()) {
      throw new IllegalArgumentException("solution impossible, to many expected white cells");
    }
    solutionWhiteCount = numberSum;
  }

  private int coordsToIndex(int x, int y) {
    if (x < 0 || x >= width) {
      throw new IllegalArgumentException("x");
    }
    if (y < 0 || y >= height) {
      throw new IllegalArgumentException("y");
    }
    return y * width + x;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Cell getCell(int x, int y) {
    return cells[coordsToIndex(x, y)];
  }

  public int getCellCount() {
    return width * height;
  }

  public int getSolutionWhiteCount() {
    return solutionWhiteCount;
  }

  public int getSolutionBlackCount() {
    return getCellCount() - getSolutionWhiteCount();
  }

  public int getWhiteCount() {
    int result = 0;
    for (final Cell cell : cells) {
      if (cell.isWhite()) ++result;
    }
    return result;
  }

  public int getBlackCount() {
    int result = 0;
    for (final Cell cell : cells) {
      if (cell.isBlack()) ++result;
    }
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Board(" + width + ", " + height + "):");
    for (int x = 0; x < width; ++x) {
      sb.append('\n');
      for (int y = 0; y < height; ++y) {
        sb.append(' ').append(getCell(x, y));
      }
    }
    return sb.toString();
  }

}
