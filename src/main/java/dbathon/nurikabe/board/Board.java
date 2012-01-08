package dbathon.nurikabe.board;

public class Board {

  private final int width;
  private final int height;

  private final int solutionWhiteCount;
  private final int maxNumber;

  private final Cell[] cells;

  public Board(BoardBuilder boardBuilder) {
    width = boardBuilder.getWidth();
    height = boardBuilder.getHeight();

    cells = new Cell[getCellCount()];
    int numberSum = 0;
    final int maxNum = 0;
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
    maxNumber = maxNum;
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

  public int getMaxNumber() {
    return maxNumber;
  }

  private void addChars(StringBuilder sb, char c, int count) {
    for (int i = 0; i < count; ++i) {
      sb.append(c);
    }
  }

  private void addSeparatorLine(StringBuilder sb, int cellWidth) {
    sb.append('+');
    for (int i = 0; i < getWidth(); ++i) {
      addChars(sb, '-', cellWidth);
      sb.append('+');
    }
  }

  private void addCell(StringBuilder sb, Cell cell, int cellWidth) {
    final FixedCell fixedCell = cell.getFixedCell();
    if (fixedCell != null) {
      final int number = fixedCell.getNumber();
      final String numberString = Integer.toString(number);
      addChars(sb, ' ', cellWidth - numberString.length());
      sb.append(numberString);
    }
    else {
      if (cell.isWhite()) {
        addChars(sb, ' ', cellWidth);
      }
      else if (cell.isBlack()) {
        addChars(sb, '#', cellWidth);
      }
      else {
        addChars(sb, '-', cellWidth);
      }
    }
  }

  @Override
  public String toString() {
    final int cellWidth = Math.max(Integer.toString(getMaxNumber()).length(), 2);

    final StringBuilder sb = new StringBuilder();
    addSeparatorLine(sb, cellWidth);
    for (int x = 0; x < getWidth(); ++x) {
      sb.append("\n|");
      for (int y = 0; y < getHeight(); ++y) {
        addCell(sb, getCell(x, y), cellWidth);
        sb.append('|');
      }
      sb.append('\n');
      addSeparatorLine(sb, cellWidth);
    }
    return sb.toString();
  }

}
