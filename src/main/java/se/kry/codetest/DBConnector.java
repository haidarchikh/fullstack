package se.kry.codetest;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;

import java.util.List;
import java.util.stream.Collectors;

public class DBConnector {

  private final SQLClient client;

  public DBConnector(Vertx vertx){
    JsonObject config = new JsonObject()
            .put("url", "jdbc:sqlite:servicePoller.db")
            .put("driver_class", "org.sqlite.JDBC")
            .put("max_pool_size", 30);

    client = JDBCClient.createShared(vertx, config);
  }

  public Future<ResultSet> query(String query) { return query(query, new JsonArray()); }

  public Future<ResultSet> query(String query, JsonArray params) {

    if (query == null || query.isEmpty()) { return Future.failedFuture("Query is null or empty");  }

    query = query.endsWith(";") ? query:  query + ";";

    Future<ResultSet> queryResultFuture = Future.future();

    client.queryWithParams(query, params, result -> {
      if (result.failed()){
        queryResultFuture.fail(result.cause());
      } else {
        queryResultFuture.complete(result.result());
      }
    });
    return queryResultFuture;
  }

  public Future<List<Service>> getAllServices() {

    Future<List<Service>> getAllServices = Future.future();

    query("SELECT * FROM service").setHandler(result -> {
      if (result.failed()){
        getAllServices.fail(result.cause());
      } else {
        getAllServices.complete(result.result().getRows().stream().map(Service::new).collect(Collectors.toList()));
      }
    });

    return getAllServices;
  }

  public void updateServiceStatus(Service service) {
    query("UPDATE SERVICE SET status = ?, lastPoll = datetime('now') WHERE id = ?", new JsonArray().add(service.getStatus()).add(service.getId()));
  }

  public Future<String> insertService(Service service) {
    Future<String> insertService = Future.future();

    query("INSERT INTO service (name, url) VALUES (?, ?)", new JsonArray().add(service.getName()).add(service.getUrl())).setHandler(result -> {
      if (result.failed()){
        insertService.fail(result.cause());
      } else {
        insertService.complete("CREATED");
      }
    });

    return insertService;
  }

  public Future<String> cleanupDB() {
    Future<String> cleanupDB = Future.future();

    // TODO: set to invalid
    query("DELETE FROM service").setHandler(result -> {
      if (result.failed()){
        // FIXME: this will fail if migration is not run
        cleanupDB.fail(result.cause());
      } else {
        cleanupDB.complete("DONE");
      }
    });

    return cleanupDB;
  }

}
