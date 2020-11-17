package se.kry.codetest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class MainVerticle extends AbstractVerticle {

  private static final int PORT = 8080;

  private DBConnector connector;
  private final BackgroundPoller poller = new BackgroundPoller();

  @Override
  public void start(Future<Void> startFuture) {
    connector = new DBConnector(vertx);

    connector.cleanupDB().setHandler(result -> {
      if (result.failed()) {
        System.out.println(result.cause().toString());
      } else {
        connector.insertService(new Service("kry", "https://www.kry.se"));
      }
    });
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    vertx.setPeriodic(1000 * 10, timerId -> poller.pollServices(connector, WebClient.create(vertx)));

    setRoutes(router);
    vertx
        .createHttpServer()
        .requestHandler(router)
        .listen(MainVerticle.PORT, result -> {
          if (result.succeeded()) {
            System.out.println("KRY code test service started: " + MainVerticle.PORT);
            startFuture.complete();
          } else {
            startFuture.fail(result.cause());
          }
        });
  }

  // TODO: move to routes file
  private void setRoutes(Router router) {
    router.route("/*").handler(StaticHandler.create());

    router.get("/service").handler(req -> {

      connector.getAllServices().setHandler(result -> {
        if (result.failed()) {
          req.response().setStatusCode(500).end(result.cause().toString());
        } else {
          // FIXME: this gives a nested object with "json" key!
          req.response().setStatusCode(200).putHeader("content-type", "application/json").end(new JsonArray(result.result()).encode());
        }
      });
    });

    router.post("/service").handler(req -> {
      connector.insertService(new Service(req.getBodyAsJson())).setHandler(result -> {
        if (result.failed()) {
          // FIXME: split to 500 and 409
          req.response().setStatusCode(500).end(result.cause().toString());
        } else {
          req.response().setStatusCode(201).end();
        }
      });
    });
  }
}



