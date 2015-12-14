package io.github.jikuja.TroubleshootingMod;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.ThreadMXBean;
import java.net.InetSocketAddress;
import java.util.List;


public class Http {
    private HttpServer server;
    private static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private static MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private static List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

    public Http (int httpport, String address) throws IllegalStateException {
        try {
            server = HttpServer.create(new InetSocketAddress(address, httpport), 0);
            server.createContext("/", new MyHandler());
            server.setExecutor(null);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create HTTP server", e);
        }
    }

    public void start() {
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle (HttpExchange httpExchange) throws IOException {
            String resp = Utils.beansToString(threadMXBean, memoryMXBean, memoryPoolMXBeans);
            httpExchange.sendResponseHeaders(200, resp.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(resp.getBytes());
            os.close();
        }
    }
}
