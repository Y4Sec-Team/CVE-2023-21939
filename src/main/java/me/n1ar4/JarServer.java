package me.n1ar4;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class JarServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8887), 0);
        server.createContext("/exploit.jar", new BinaryHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8887");
    }
    public static byte[] readInputStream(InputStream inputStream) {
        byte[] temp = new byte[4096];
        int readOneNum = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while ((readOneNum = inputStream.read(temp)) != -1) {
                bos.write(temp, 0, readOneNum);
            }
            inputStream.close();
        } catch (Exception ignored) {
        }
        return bos.toByteArray();
    }
    static class BinaryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("get request");
            byte[] data = readInputStream(JarServer.class.getClassLoader()
                    .getResourceAsStream("exploit.jar"));
            exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, data.length);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(data);
            responseBody.close();
        }
    }
}
