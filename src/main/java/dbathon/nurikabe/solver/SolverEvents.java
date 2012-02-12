package dbathon.nurikabe.solver;

import dbathon.nurikabe.board.Board;

public interface SolverEvents {

  void onBeginSolve(Board board, Solver solver);

  void onStrategyExecuted(Board board, SolverStrategy strategy, long nanos, Solver solver);

}
