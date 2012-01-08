package dbathon.nurikabe.board;

public class BoardBuilder {

  private final int width;
  private final int height;

  private final int[] numbers;

  public BoardBuilder(int width, int height) {
    if (width < 1) {
      throw new IllegalArgumentException("width");
    }
    if (height < 1) {
      throw new IllegalArgumentException("height");
    }
    this.width = width;
    this.height = height;
    numbers = new int[width * height];
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
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

  public void setNumber(int x, int y, int number) {
    final int index = coordsToIndex(x, y);
    if (number < 0) {
      throw new IllegalArgumentException("number");
    }
    numbers[index] = number;
  }

  public int getNumber(int x, int y) {
    final int index = coordsToIndex(x, y);
    return numbers[index];
  }

}
