package goodr0ne.washdisher;

public class WashdisherStatus {
  private int capacity;
  private long duration;
  private long washedTime;
  private boolean isOperational;
  private boolean isCleaned;

  private static WashdisherStatus instance = new WashdisherStatus();

  public static WashdisherStatus getInstance() {
    return instance;
  }

  private WashdisherStatus() {
    try {
      retrieveStatus();
      System.out.println("WashdisherStatus instance successfully restored");
    } catch (Exception e) {
      System.out.println("WashdisherStatus instance is not retrieved, creating new one");
      wipe();
    }
  }


  public synchronized boolean isLoaded() {
    return capacity > 0;
  }

  public synchronized boolean getIsCleaned() {
    return isCleaned;
  }

  public synchronized void setIsCleaned(boolean isCleaned) {
    this.isCleaned = isCleaned;
  }

  public synchronized int getCapacity() {
    return capacity;
  }

  public synchronized void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public synchronized void saveStatus() {
    System.out.println("Trying to save WashdisherStatus to db");
  }

  private synchronized void retrieveStatus() throws Exception {
    System.out.println("Trying to retrieve WashdisherStatus stored in db");
    throw new Exception();
  }

  public synchronized void wipe() {
    capacity = 0;
    duration = 0;
    washedTime = 0;
  }
}
