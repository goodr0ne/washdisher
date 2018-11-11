package goodr0ne.washdisher;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class WashdisherBDConnector {
  private static final String DB_URL = "washdisherdb-id6qs.azure.mongodb.net/test" +
          "?retryWrites=true";
  private static final String DB_USERNAME = "WashdisherDBUser";
  private static final String DB_PASSWORD = "ectoplasm_47";
  private MongoClientURI uri;
  private MongoClient client;
  private MongoDatabase db;
  private MongoCollection<Document> collection;

  private static WashdisherBDConnector ourInstance = new WashdisherBDConnector();

  public static WashdisherBDConnector getInstance() {
    return ourInstance;
  }

  private WashdisherBDConnector() {
    uri = new MongoClientURI(
            "mongodb+srv://" + DB_USERNAME + ":" + DB_PASSWORD + "@" + DB_URL);
    client = new MongoClient(uri);
    db = client.getDatabase("washdisherdb");
    collection = db.getCollection("washdisher_status");
  }

  public MongoCollection<Document> getCollection() {
    return collection;
  }
}
