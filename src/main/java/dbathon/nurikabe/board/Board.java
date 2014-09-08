package dbathon.nurikabe.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

public class Board implements Iterable<Cell> {

  private final int width;
  private final int height;

  private final int solutionWhiteCount;
  private final int maxNumber;

  final Cell[] cells;
  private final List<Cell> cellsListView;

  private final List<Set<Cell>> neighborSets;

  public Board(BoardBuilder boardBuilder) {
    width = boardBuilder.getWidth();
    height = boardBuilder.getHeight();

    cells = new Cell[getCellCount()];
    cellsListView = Collections.unmodifiableList(Arrays.asList(cells));

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

    neighborSets = cellsListView.stream().map(cell -> {
      final int x = cell.getX();
      final int y = cell.getY();
      final Builder<Cell> builder = ImmutableSet.<Cell>builder();
      if (isLegalCoord(x + 1, y)) {
        builder.add(getCell(x + 1, y));
      }
      if (isLegalCoord(x - 1, y)) {
        builder.add(getCell(x - 1, y));
      }
      if (isLegalCoord(x, y + 1)) {
        builder.add(getCell(x, y + 1));
      }
      if (isLegalCoord(x, y - 1)) {
        builder.add(getCell(x, y - 1));
      }
      return builder.build();
    }).collect(Collectors.toList());
  }

  int coordsToIndex(int x, int y) {
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

  public Stream<Cell> getCells() {
    return cellsListView.stream();
  }

  public Stream<Cell> getWhiteCells() {
    return getCells().filter(cell -> cell.isWhite());
  }

  public Stream<Cell> getBlackCells() {
    return getCells().filter(cell -> cell.isBlack());
  }

  public Stream<Cell> getUnknownCells() {
    return getCells().filter(cell -> cell.isUnknown());
  }

  public int getCount(CellColor cellColor) {
    return (int) getCells().filter(cell -> cell.getColor() == cellColor).count();
  }

  public int getMaxNumber() {
    return maxNumber;
  }

  public Set<Cell> getNeighborsSet(Cell cell) {
    if (cell.getBoard() != this) {
      throw new IllegalArgumentException("cell");
    }

    return neighborSets.get(coordsToIndex(cell.getX(), cell.getY()));
  }

  public Stream<Cell> getNeighbors(Cell cell) {
    return getNeighborsSet(cell).stream();
  }

  private void connectWhiteCell(Cell cell) {
    final FixedCell fixedCell = cell.getFixedCell();
    if (fixedCell != null) {
      getNeighbors(cell).filter(neighbor -> neighbor.isWhite() && neighbor.getFixedCell() == null)
          .forEach(neighbor -> {
            neighbor.setFixedCell(fixedCell);
            connectWhiteCell(neighbor);
          });
    }
  }

  public void connectWhiteCells() {
    getWhiteCells().forEach(this::connectWhiteCell);
  }

  public Map<FixedCell, Set<Cell>> getWhiteGroupsWithFixedCell() {
    connectWhiteCells();

    return getCells().filter(cell -> cell.getFixedCell() != null)
        .collect(
            Collectors.groupingBy(Cell::getFixedCell,
                Collectors.toCollection(() -> new CellSet(this))));
  }

  /**
   * Recursively search all connected cells.
   */
  private void findConnectedCells(Cell cell, Set<Cell> result) {
    result.add(cell);
    getNeighbors(cell).filter(neighbor -> neighbor.getColor() == cell.getColor())
        .filter(neighbor -> !result.contains(neighbor))
        .forEach(neighbor -> findConnectedCells(neighbor, result));
  }

  public Set<Set<Cell>> getGroups(CellColor cellColor) {
    final Set<Cell> cellsWithColor = new CellSet(this);
    for (final Cell cell : cells) {
      if (cell.getColor() == cellColor) {
        cellsWithColor.add(cell);
      }
    }

    final Set<Set<Cell>> result = new HashSet<>();
    while (!cellsWithColor.isEmpty()) {
      final Set<Cell> group = new CellSet(this);
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
    if (getWhiteCells().anyMatch(cell -> cell.getFixedCell() == null)) {
      return false;
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
        if (getNeighbors(cell).allMatch(
            neighbor -> neighbor.getFixedCell() != null && neighbor.getFixedCell() != fixedCell)) {
          return false;
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
    return cellsListView.iterator();
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
