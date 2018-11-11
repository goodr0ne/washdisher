package goodr0ne.washdisher;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class WashdisherSaveStatusThread implements Runnable {
  private static final String DB_URL = "washdisherdb-id6qs.azure.mongodb.net/test" +
          "?retryWrites=true";
  private static final String DB_USERNAME = "WashdisherDBUser";
  private static final String DB_PASSWORD = "ectoplasm_47";
  private static Gson gson = new Gson();

  @Override
  public void run() {
    long startTime = System.currentTimeMillis();
    System.out.println("Saving status data to mongodb atlas cloud background task");
    try {
      MongoClientURI uri = new MongoClientURI(
              "mongodb+srv://" + DB_USERNAME + ":" + DB_PASSWORD + "@" + DB_URL);
      MongoClient client = new MongoClient(uri);
      MongoDatabase db = client.getDatabase("washdisherdb");
      MongoCollection<Document> collection = db.getCollection("washdisher_status");
      Document statusDoc = Document.parse(gson.toJson(WashdisherStatus.getInstance().getJson()));
      collection.drop();
      collection.insertOne(statusDoc);
    } catch (Exception e) {
      System.out.println("Saving washdisher status - exception arrived, " + e.toString());
    }
    System.out.println("Total spent time - " + (System.currentTimeMillis() - startTime));
  }
}
