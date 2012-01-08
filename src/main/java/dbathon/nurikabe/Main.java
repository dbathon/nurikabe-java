package dbathon.nurikabe;

import dbathon.nurikabe.board.Board;
import dbathon.nurikabe.board.BoardState;
import dbathon.nurikabe.board.BoardUtil;

public class Main {

  public static void main(String[] args) {
    final Board board = BoardUtil.parseStringToBoard("5:5:.... 3.  .7.... ..... 2 ..1....");
    System.out.println(board);
    final BoardState boardState = new BoardState(board);
    System.out.println(boardState);
    board.getCell(1, 1).setWhite();
    board.getCell(1, 0).setBlack();
    System.out.println(board);
    boardState.restoreState();
    System.out.println(board);

    final Board board2 = BoardUtil.parseStringToBoard("2:2:.1..");
    System.out.println(board2);
    System.out.println(board2.isSolution());
    board2.getCell(0, 0).setBlack();
    System.out.println(board2.isSolution());
    board2.getCell(1, 0).setBlack();
    System.out.println(board2.isSolution());
    board2.getCell(1, 1).setBlack();
    System.out.println(board2.isSolution());
    System.out.println(board2);

    final Board board3 = BoardUtil.parseStringToBoard("2:2:.4..");
    System.out.println(board3);
    System.out.println(board3.isSolution());
    board3.getCell(0, 0).setWhite();
    System.out.println(board3.isSolution());
    board3.getCell(1, 0).setWhite();
    System.out.println(board3.isSolution());
    board3.getCell(1, 1).setWhite();
    System.out.println(board3.isSolution());
    System.out.println(board3);
  }

}
