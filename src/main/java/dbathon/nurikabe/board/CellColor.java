package dbathon.nurikabe.board;

public enum CellColor {
  BLACK,
  WHITE,
  UNKNOWN;

  private final String shortName;

  private CellColor() {
    shortName = name().substring(0, 1);
  }

  public String getShortName() {
    return shortName;
  }
}
