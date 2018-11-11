package goodr0ne.washdisher;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Objects;

class WashdisherStatus {
  private static boolean isTurnOn = false;
  private static final String DB_URL = "washdisherdb-id6qs.azure.mongodb.net/test" +
          "?retryWrites=true";
  private static final String DB_USERNAME = "WashdisherDBUser";
  private static final String DB_PASSWORD = "ectoplasm_47";
  private static Gson gson = new Gson();
  private int capacity;
  private int duration;
  private long lastCheckTime;
  private long washedTime;
  private boolean isOperational;
  private boolean isCleaned;

  private static WashdisherStatus instance = new WashdisherStatus();

  synchronized static WashdisherStatus getInstance() {
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
    try {
      MongoClientURI uri = new MongoClientURI(
              "mongodb+srv://" + DB_USERNAME + ":" + DB_PASSWORD + "@" + DB_URL);
      MongoClient client = new MongoClient(uri);
      MongoDatabase db = client.getDatabase("washdisherdb");
      MongoCollection<Document> collection = db.getCollection("washdisher_status");
      Document statusDoc = Document.parse(gson.toJson(getJson()));
      collection.drop();
      collection.insertOne(statusDoc);
    } catch (Exception e) {
      System.out.println("Saving washdisher status - exception arrived, " + e.toString());
    }
  }

  //stub for further db usage
  private synchronized void retrieveStatus() {
    System.out.println("Trying to retrieve Washdisher status stored in db");
    try {
      MongoClientURI uri = new MongoClientURI(
              "mongodb+srv://" + DB_USERNAME + ":" + DB_PASSWORD
                      + "@washdisherdb-id6qs.azure.mongodb.net/test?retryWrites=true");
      MongoClient client = new MongoClient(uri);
      MongoDatabase db = client.getDatabase("washdisherdb");
      MongoCollection<Document> collection = db.getCollection("washdisher_status");
      restoreFromDocument(Objects.requireNonNull(collection.find().first()));
      System.out.println("Washdisher status successfully restored!");
    } catch (Exception e) {
      System.out.println("Washdisher status restoring problems - " + e.toString());
      e.printStackTrace();
    }
  }

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

  private JsonObject getJson() {
    JsonObject statusObj = new JsonObject();
    statusObj.addProperty("capacity", capacity);
    statusObj.addProperty("duration", duration);
    statusObj.addProperty("washedTime", washedTime);
    statusObj.addProperty("lastCheckTime", lastCheckTime);
    statusObj.addProperty("isCleaned", isCleaned);
    statusObj.addProperty("isOperational", isOperational);
    return statusObj;
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
