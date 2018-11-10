package goodr0ne.washdisher;

class WashdisherStatus {
  private static boolean isTurnOn = false;
  private int capacity;
  private int duration;
  private long lastCheckTime;
  private long washedTime;
  private boolean isOperational;
  private boolean isCleaned;

  private static WashdisherStatus instance = new WashdisherStatus();

  static WashdisherStatus getInstance() {
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

  synchronized static void TURN_OFF() {
    instance = new WashdisherStatus();
    WashdisherStatus.isTurnOn = false;
  }

  synchronized static void TURN_ON() {
    WashdisherStatus.isTurnOn = true;
  }

  synchronized static boolean IS_TURN_ON() {
    return isTurnOn;
  }

  synchronized boolean getIsCleaned() {
    return isCleaned;
  }

  synchronized void setIsCleaned() {
    this.isCleaned = true;
  }

  synchronized boolean getIsOperational() {
    return isOperational;
  }

  synchronized void setIsOperational(boolean isOperational) {
    this.isOperational = isOperational;
  }

  synchronized int getCapacity() {
    return capacity;
  }

  synchronized void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  synchronized int getDuration() {
    return duration;
  }

  synchronized void setDuration(int duration) {
    this.duration = duration;
  }

  synchronized long getLastCheckTime() {
    return lastCheckTime;
  }

  synchronized void setLastCheckTime(long lastCheckTime) {
    this.lastCheckTime = lastCheckTime;
  }

  synchronized long getWashedTime() {
    return washedTime;
  }

  synchronized void setWashedTime(long washedTime) {
    this.washedTime = washedTime;
  }

  //stub for further db usage
  synchronized void saveStatus() {
    System.out.println("Trying to save WashdisherStatus to db");
  }

  //stub for further db usage
  private synchronized void retrieveStatus() throws Exception {
    System.out.println("Trying to retrieve WashdisherStatus stored in db");
    throw new Exception();
  }

  synchronized void wipe() {
    capacity = 0;
    duration = 0;
    washedTime = 0;
    lastCheckTime = 0;
    isCleaned = false;
    isOperational = false;
    saveStatus();
  }
}
