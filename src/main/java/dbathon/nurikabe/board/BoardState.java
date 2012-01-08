package dbathon.nurikabe.board;

/**
 * Can be used to save and later restore a particular state of a {@link Board}.
 */
public class BoardState {

  private final Board board;
  private final String state;

  public BoardState(Board board) {
    this.board = board;

    final StringBuilder sb = new StringBuilder(board.getCellCount());
    for (int x = 0; x < board.getWidth(); ++x) {
      for (int y = 0; y < board.getHeight(); ++y) {
        final Cell cell = board.getCell(x, y);
        if (cell.isWhite()) {
          sb.append('w');
        }
        else if (cell.isBlack()) {
          sb.append('b');
        }
        else {
          sb.append('?');
        }
      }
    }
    state = sb.toString();
  }

  public Board getBoard() {
    return board;
  }

  public void restoreState() {
    int index = 0;
    for (int x = 0; x < board.getWidth(); ++x) {
      for (int y = 0; y < board.getHeight(); ++y) {
        final Cell cell = board.getCell(x, y);
        switch (state.charAt(index)) {
        case 'w':
          cell.setWhite();
          break;
        case 'b':
          cell.setBlack();
          break;
        case '?':
          cell.setUnknown();
          break;
        default:
          throw new IllegalStateException("unexpected char in " + state);
        }
        ++index;
      }
    }
  }

  @Override
  public String toString() {
    return "BoardState(" + state + ")";
  }

}
