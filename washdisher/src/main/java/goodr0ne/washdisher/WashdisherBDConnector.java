package goodr0ne.washdisher;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

class WashdisherBDConnector {
  private static final String DB_URL = "washdisherdb-id6qs.azure.mongodb.net/test" +
          "?retryWrites=true";
  private static final String DB_USERNAME = "WashdisherDBUser";
  private static final String DB_PASSWORD = "ectoplasm_47";
  private MongoCollection<Document> collection;

  private static WashdisherBDConnector ourInstance = new WashdisherBDConnector();

  static WashdisherBDConnector getInstance() {
    return ourInstance;
  }

  private WashdisherBDConnector() {
    MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://" + DB_USERNAME + ":" + DB_PASSWORD + "@" + DB_URL);
    MongoClient client = new MongoClient(uri);
    MongoDatabase db = client.getDatabase("washdisherdb");
    collection = db.getCollection("washdisher_status");
  }

  MongoCollection<Document> getCollection() {
    return collection;
  }

  Document retrieveStatusFromCloud() {
    return getCollection().find().first();
  }
}
