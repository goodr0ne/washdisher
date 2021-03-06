package goodr0ne.washdisher;

import com.google.gson.JsonObject;
import org.bson.Document;

import java.util.Objects;

/**
 * Washdisher data class, designed as singleton real-time system data access point.
 * Use WashdisherDBConnector upon instance creation stage for possible retrieval
 * of stored washdisher status data in mongodb atlas cloud service
 */
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

  /**
   * Upon instance creation will try to recover stored status data from mongo cloud db.
   * Fill status with default zero data if recovery fails.
   */
  private WashdisherStatus() {
    try {
      retrieveStatus();
    } catch (Exception e) {
      System.out.println("WashdisherStatus instance is not retrieved, creating new one");
      wipe();
    }
  }

  static void TURN_OFF() {
    instance = new WashdisherStatus();
    WashdisherStatus.isTurnOn = false;
  }

  static void TURN_ON() {
    WashdisherStatus.isTurnOn = true;
  }

  static boolean IS_TURN_ON() {
    return isTurnOn;
  }

  boolean getIsCleaned() {
    return isCleaned;
  }

  void setIsCleaned() {
    this.isCleaned = true;
  }

  boolean getIsOperational() {
    return isOperational;
  }

  void setIsOperational(boolean isOperational) {
    this.isOperational = isOperational;
  }

  int getCapacity() {
    return capacity;
  }

  void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  int getDuration() {
    return duration;
  }

  void setDuration(int duration) {
    this.duration = duration;
  }

  long getLastCheckTime() {
    return lastCheckTime;
  }

  void setLastCheckTime(long lastCheckTime) {
    this.lastCheckTime = lastCheckTime;
  }

  long getWashedTime() {
    return washedTime;
  }

  void setWashedTime(long washedTime) {
    this.washedTime = washedTime;
  }

  private void retrieveStatus() {
    System.out.println("Trying to retrieve Washdisher status stored in db");
    try {
      Document storedStatus = WashdisherBDConnector.getInstance().retrieveStatusFromCloud();
      restoreFromDocument(Objects.requireNonNull(storedStatus));
      System.out.println("Washdisher status successfully restored!");
    } catch (Exception e) {
      System.out.println("Washdisher status restoring problems - " + e.toString());
    }
  }

  /**
   * Try to fill status data with document, expected mongo bson doc data as input.
   * Due to data type parsing problems in mongo bson doc there are a lot of try blocks.
   * @param doc Bson Document with stored status data expected
   */
  private void restoreFromDocument(Document doc) {
    try {
      capacity = doc.getInteger("capacity");
    } catch (Exception e) {
      capacity = Integer.parseInt(doc.getInteger("capacity").toString());
    }
    try {
      duration = doc.getInteger("duration");
    } catch (Exception e) {
      duration = Integer.parseInt(doc.getInteger("duration").toString());
    }
    try {
      washedTime = doc.getLong("washedTime");
    } catch (Exception e) {
      try {
        washedTime = Long.parseLong(doc.getLong("washedTime").toString());
      } catch (Exception ex) {
        try {
          washedTime = doc.getInteger("washedTime");
        } catch (Exception exe) {
          washedTime = Integer.parseInt(doc.getInteger("washedTime").toString());
        }
      }
    }
    try {
      lastCheckTime = doc.getLong("lastCheckTime");
    } catch (Exception e) {
      try {
        lastCheckTime = Long.parseLong(doc.getLong("lastCheckTime").toString());
      } catch (Exception ex) {
        try {
          lastCheckTime = doc.getInteger("lastCheckTime");
        } catch (Exception exe) {
          lastCheckTime = Integer.parseInt(doc.getInteger("lastCheckTime").toString());
        }
      }
    }
    isCleaned = doc.getBoolean("isCleaned");
    isOperational = doc.getBoolean("isOperational");
  }

  /**
   * Parse current status data for storage purposes.
   * Power status is excepted from the process.
   * @return json string with current status data
   */
  JsonObject getJson() {
    JsonObject statusObj = new JsonObject();
    statusObj.addProperty("capacity", capacity);
    statusObj.addProperty("duration", duration);
    statusObj.addProperty("washedTime", washedTime);
    statusObj.addProperty("lastCheckTime", lastCheckTime);
    statusObj.addProperty("isCleaned", isCleaned);
    statusObj.addProperty("isOperational", isOperational);
    return statusObj;
  }

  /**
   * Fill up status with zero-data
   */
  void wipe() {
    capacity = 0;
    duration = 0;
    washedTime = 0;
    lastCheckTime = 0;
    isCleaned = false;
    isOperational = false;
  }
}
