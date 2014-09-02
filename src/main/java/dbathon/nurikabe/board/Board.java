package dbathon.nurikabe.board;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Board implements Iterable<Cell> {

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

  public boolean isLegalCoord(int x, int y) {
    return !(x < 0 || x >= width) && !(y < 0 || y >= height);
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

  public int getCount(CellColor cellColor) {
    int result = 0;
    for (final Cell cell : cells) {
      if (cell.getColor() == cellColor) {
        ++result;
      }
    }
    return result;
  }

  public int getMaxNumber() {
    return maxNumber;
  }

  public Set<Cell> getNeighbors(Cell cell) {
    if (cell.getBoard() != this) {
      throw new IllegalArgumentException("cell");
    }
    final Set<Cell> result = new HashSet<Cell>();

    final int x = cell.getX();
    final int y = cell.getY();
    if (isLegalCoord(x + 1, y)) {
      result.add(getCell(x + 1, y));
    }
    if (isLegalCoord(x - 1, y)) {
      result.add(getCell(x - 1, y));
    }
    if (isLegalCoord(x, y + 1)) {
      result.add(getCell(x, y + 1));
    }
    if (isLegalCoord(x, y - 1)) {
      result.add(getCell(x, y - 1));
    }

    return result;
  }

  public void connectWhiteCells() {
    int changes = 0;
    while (true) {
      for (final Cell cell : cells) {
        if (cell.isWhite() && cell.getFixedCell() == null) {
          for (final Cell neighbor : getNeighbors(cell)) {
            if (neighbor.getFixedCell() != null) {
              cell.setFixedCell(neighbor.getFixedCell());
              ++changes;
              break;
            }
          }
        }
      }
      if (changes == 0) {
        // done
        return;
      }
      else {
        // try again, maybe there are more changes possible now
        changes = 0;
      }
    }
  }

  public Map<FixedCell, Set<Cell>> getWhiteGroupsWithFixedCell() {
    connectWhiteCells();

    final Map<FixedCell, Set<Cell>> result = new HashMap<FixedCell, Set<Cell>>();
    for (final Cell cell : cells) {
      final FixedCell fixedCell = cell.getFixedCell();
      if (fixedCell != null) {
        Set<Cell> groupCells = result.get(fixedCell);
        if (groupCells == null) {
          groupCells = new HashSet<Cell>();
          result.put(fixedCell, groupCells);
        }
        groupCells.add(cell);
      }
    }
    return result;
  }

  /**
   * Recursively search all connected cells.
   */
  private void findConnectedCells(Cell cell, Set<Cell> result) {
    result.add(cell);
    for (final Cell neighbor : getNeighbors(cell)) {
      if (neighbor.getColor() == cell.getColor() && !result.contains(neighbor)) {
        findConnectedCells(neighbor, result);
      }
    }
  }

  public Set<Set<Cell>> getGroups(CellColor cellColor) {
    final Set<Cell> cellsWithColor = new HashSet<Cell>();
    for (final Cell cell : cells) {
      if (cell.getColor() == cellColor) {
        cellsWithColor.add(cell);
      }
    }

    final Set<Set<Cell>> result = new HashSet<Set<Cell>>();
    while (!cellsWithColor.isEmpty()) {
      final Set<Cell> group = new HashSet<Cell>();
      final Cell startCell = cellsWithColor.iterator().next();
      findConnectedCells(startCell, group);
      result.add(group);

      cellsWithColor.removeAll(group);
    }

    return result;
  }

  public boolean isSolution() {
    if (getSolutionWhiteCount() != getCount(CellColor.WHITE)
        || getSolutionBlackCount() != getCount(CellColor.BLACK)) {
      return false;
    }
    connectWhiteCells();

    // no non-connected white cells
    for (final Cell cell : cells) {
      if (cell.isWhite() && cell.getFixedCell() == null) {
        return false;
      }
    }

    // all white groups are complete
    final Map<FixedCell, Set<Cell>> whiteGroups = getWhiteGroupsWithFixedCell();
    for (final Entry<FixedCell, Set<Cell>> entry : whiteGroups.entrySet()) {
      if (entry.getKey().getNumber() != entry.getValue().size()) {
        return false;
      }
    }

    // all fixedCell neighbors belong together
    for (final Cell cell : cells) {
      final FixedCell fixedCell = cell.getFixedCell();
      if (fixedCell != null) {
        for (final Cell neighbor : getNeighbors(cell)) {
          if (neighbor.getFixedCell() != null && neighbor.getFixedCell() != fixedCell) {
            return false;
          }
        }
      }
    }

    // at most one black groups
    if (getGroups(CellColor.BLACK).size() > 1) {
      return false;
    }

    // no black "blocks"
    for (int x = 0; x < getWidth() - 1; ++x) {
      for (int y = 0; y < getHeight() - 1; ++y) {
        final Cell cell1 = getCell(x, y);
        final Cell cell2 = getCell(x + 1, y);
        final Cell cell3 = getCell(x, y + 1);
        final Cell cell4 = getCell(x + 1, y + 1);
        if (cell1.isBlack() && cell2.isBlack() && cell3.isBlack() && cell4.isBlack()) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  public Iterator<Cell> iterator() {
    return Arrays.asList(cells).iterator();
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

  private void addCell(StringBuilder sb, Cell cell, int cellWidth, boolean showFixedCells) {
    FixedCell fixedCell = cell.getFixedCell();
    if (!showFixedCells && fixedCell != cell) {
      fixedCell = null;
    }
    if (fixedCell != null) {
      final int number = fixedCell.getNumber();
      final String numberString = Integer.toString(number);
      addChars(sb, cell.isFixed() ? ' ' : '-', cellWidth - numberString.length());
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

  public String toString(boolean showFixedCells) {
    final int cellWidth = Integer.toString(getMaxNumber()).length() + 1;

    final StringBuilder sb = new StringBuilder();
    addSeparatorLine(sb, cellWidth);
    for (int y = 0; y < getHeight(); ++y) {
      sb.append("\n|");
      for (int x = 0; x < getWidth(); ++x) {
        addCell(sb, getCell(x, y), cellWidth, showFixedCells);
        sb.append('|');
      }
      sb.append('\n');
      addSeparatorLine(sb, cellWidth);
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    return toString(false);
  }

}
