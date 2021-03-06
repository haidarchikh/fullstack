package se.kry.codetest.migrate;

import io.vertx.core.Vertx;
import se.kry.codetest.DBConnector;

public class DBMigration {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    DBConnector connector = new DBConnector(vertx);

    // TODO: VARCHAR(max)
    connector.query("DROP TABLE IF EXISTS service").setHandler(res -> {
      connector.query("CREATE TABLE service (" +
              "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
              "name VARCHAR(200) NOT NULL, " +
              "url VARCHAR(200) NOT NULL UNIQUE, " +
              "status VARCHAR(20) DEFAULT 'UNKNOWN', " +
              "lastPoll DEFAULT NULL, " +
              "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP " +
              ")").setHandler(done -> {

                // TODO: run migration each start or allow cleaning the db?
//                connector.query("CREATE TRIGGER disable_service_delete\n" +
//                "BEFORE DELETE ON service\n" +
//                "BEGIN\n" +
//                "    SELECT RAISE(ABORT, 'There's no way to delete individual services');\n" +
//                "END");
        if (done.succeeded()) {
          System.out.println("completed db migrations");
        } else {
          done.cause().printStackTrace();
        }

        vertx.close(shutdown -> { System.exit(0); });
      });
    });
  }
}
