package org.example;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class SensorTemperature {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        String sensorType = "Temperature";
        try (Socket socket = new Socket(HOST, PORT);
             OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true)) {
            SimpleLogger.log("Client: " + socket.getInetAddress() + " connected to server (" + sensorType + " censor)");

            while (true) {
                int value = new Random().nextInt(101) - 50;
                writer.println(sensorType); // sender sensornavn
                writer.println(value); // sender værdi
                Thread.sleep(5000); // venter 5 sekunder
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}