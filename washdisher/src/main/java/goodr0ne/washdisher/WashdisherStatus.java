package goodr0ne.washdisher;

public class WashdisherStatus {
  private int capacity;
  private int duration;
  private long lastCheckTime;
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

  public synchronized boolean getIsOperational() {
    return isOperational;
  }

  public synchronized void setIsOperational(boolean isOperational) {
    this.isOperational = isOperational;
  }

  public synchronized int getCapacity() {
    return capacity;
  }

  public synchronized void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public synchronized int getDuration() {
    return duration;
  }

  public synchronized void setDuration(int duration) {
    this.duration = duration;
  }

  public synchronized long getLastCheckTime() {
    return lastCheckTime;
  }

  public synchronized void setLastCheckTime(long lastCheckTime) {
    this.lastCheckTime = lastCheckTime;
  }

  public synchronized long getWashedTime() {
    return washedTime;
  }

  public synchronized void setWashedTime(long washedTime) {
    this.washedTime = washedTime;
  }

  //stub for further db usage
  public synchronized void saveStatus() {
    System.out.println("Trying to save WashdisherStatus to db");
  }

  //stub for further db usage
  private synchronized void retrieveStatus() throws Exception {
    System.out.println("Trying to retrieve WashdisherStatus stored in db");
    throw new Exception();
  }

  public synchronized void wipe() {
    capacity = 0;
    duration = 0;
    washedTime = 0;
    lastCheckTime = 0;
    isCleaned = false;
    isOperational = false;
    saveStatus();
  }
}
