package se.kry.codetest;

import io.vertx.ext.web.client.WebClient;

public class BackgroundPoller {

  public void pollServices(DBConnector connector, WebClient webClient) {

    connector.getAllServices().setHandler(result -> {

      if (result.failed()) { System.out.println(result.cause().toString()); return; }

        result.result().forEach(service ->
          webClient.getAbs(service.getUrl()).send(res -> {
              service.setStatus(res.failed() || res.result().statusCode() != 200 ? "FAIL": "OK");
              connector.updateServiceStatus(service);
          }));
    });
  }
}
