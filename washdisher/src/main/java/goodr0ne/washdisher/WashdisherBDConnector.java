package goodr0ne.washdisher;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * WashdisherBDConnector is a singleton class, used for retrieving stored washdisher
 * status data from mongo cloud db and provide access for write operations for
 * status data saving background threads
 */
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

  /**
   * Connection metadata initialized once upon class creation stage
   */
  private WashdisherBDConnector() {
    MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://" + DB_USERNAME + ":" + DB_PASSWORD + "@" + DB_URL);
    MongoClient client = new MongoClient(uri);
    MongoDatabase db = client.getDatabase("washdisherdb");
    collection = db.getCollection("washdisher_status");
  }

  /**
   * Returns MongoCollection instance, used for all db read/write operations
   * @return MongoCollection instance
   */
  MongoCollection<Document> getCollection() {
    return collection;
  }

  /**
   * Provides stored in cloud document with status data
   * @return Bson Document object
   */
  Document retrieveStatusFromCloud() {
    return getCollection().find().first();
  }
}
