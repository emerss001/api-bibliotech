package server;

import Controller.MaterialController;

import static spark.Spark.get;
import static spark.Spark.port;

public class Server {
    public static void main(String[] args) {
        port(8888);
        new MaterialController();

        get("/", ((request, response) -> "Hello World!"));
    }
}
