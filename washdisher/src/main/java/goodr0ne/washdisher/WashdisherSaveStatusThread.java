package goodr0ne.washdisher;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class WashdisherSaveStatusThread implements Runnable {
  private static Gson gson = new Gson();

  @Override
  public void run() {
    long startTime = System.currentTimeMillis();
    System.out.println("Saving status data to mongodb atlas cloud background task");
    try {
      MongoCollection<Document> collection = WashdisherBDConnector.getInstance().getCollection();
      Document statusDoc = Document.parse(gson.toJson(WashdisherStatus.getInstance().getJson()));
      collection.drop();
      collection.insertOne(statusDoc);
    } catch (Exception e) {
      System.out.println("Saving washdisher status - exception arrived, " + e.toString());
    }
    System.out.println("Total spent time - " + (System.currentTimeMillis() - startTime));
  }
}
