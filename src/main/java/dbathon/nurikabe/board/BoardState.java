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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((board == null) ? 0 : board.hashCode());
    result = prime * result + ((state == null) ? 0 : state.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final BoardState other = (BoardState) obj;
    if (board == null) {
      if (other.board != null) return false;
    }
    else if (!board.equals(other.board)) return false;
    if (state == null) {
      if (other.state != null) return false;
    }
    else if (!state.equals(other.state)) return false;
    return true;
  }

  @Override
  public String toString() {
    return "BoardState(" + state + ")";
  }

}
