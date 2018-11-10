package goodr0ne.washdisher;

public class WashdisherStatus {
  public static final int MAX_CAPASITY = 20;
  public static final long MAX_DURATION = 5 * 60 * 1000;
  private int capasity;
  private long duration;
  private long washedTime;
  private boolean isOperational;
  private boolean isCleaned;
}
