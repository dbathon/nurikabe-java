package dbathon.nurikabe.board;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BoardUtil {

  private BoardUtil() {}

  private static final Pattern VALIDATE_PATTERN = Pattern
      .compile("([0-9]+):([0-9]+):([0-9\\.\\s]+)");

  private static final Pattern CELL_PATTERN = Pattern.compile("[0-9]+|\\.");

  private static final Pattern CELL_PATTERN_SINGLE = Pattern.compile("[0-9]|\\.");

  /**
   * Format is "&lt;width&gt;:&lt;height&gt;:&lt;cells&gt;", where cells is a string consisting of
   * <code>width * height</code> "cells", a cell is either a dot ('.') or a number, the cells can be
   * separated by whitespace, the cells are in "rows first" order.
   * <p>
   * Examples:
   * 
   * <pre>
   * 2:2:.1..
   * 5:5:.... 3.  .7.... ..... 2 ..1....
   * 9:9:...6..........3...............6....2.1.....3.3....5...............2..........1...
   * </pre>
   */
  public static BoardBuilder parseStringToBoardBuilder(String puzzleString, boolean singleDigitOnly) {
    final Matcher matcher = VALIDATE_PATTERN.matcher(puzzleString);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("illegal puzzle string: " + puzzleString);
    }
    final int width = Integer.parseInt(matcher.group(1));
    final int height = Integer.parseInt(matcher.group(2));
    final BoardBuilder result = new BoardBuilder(width, height);

    final Matcher cellMatcher =
        (singleDigitOnly ? CELL_PATTERN_SINGLE : CELL_PATTERN).matcher(matcher.group(3));
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        if (!cellMatcher.find()) {
          throw new IllegalArgumentException("no data for cell " + x + ", " + y);
        }
        final String cell = cellMatcher.group();
        if (".".equals(cell)) {
          result.setNumber(x, y, 0);
        }
        else {
          result.setNumber(x, y, Integer.parseInt(cell));
        }
      }
    }
    if (cellMatcher.find()) {
      throw new IllegalArgumentException("to much data");
    }

    return result;
  }

  /**
   * See {@link #parseStringToBoardBuilder(String, boolean)}.
   */
  public static Board parseStringToBoard(String puzzleString, boolean singleDigitOnly) {
    return new Board(parseStringToBoardBuilder(puzzleString, singleDigitOnly));
  }

}
