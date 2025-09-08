package org.example;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MarsServer {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                SimpleLogger.log("Client connected from " + socket.getInetAddress());
                threadPool.submit(new ClientHandler(socket));
            }

        } catch (Exception e) {
            SimpleLogger.log("Error: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }
}
