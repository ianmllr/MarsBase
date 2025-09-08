package org.example;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            String message = "hhh";
            String sensorType;
            String valueLine;
            // Læs beskeder fra klienten og send et svar tilbage
            while ((sensorType = reader.readLine()) != null) {
                valueLine = reader.readLine();
                if (valueLine == null) {
                    break;
                }
                int value = 0;
                try {
                    value = Integer.parseInt(valueLine);
                } catch (NumberFormatException e) {
                    writer.println("Invalid value received from : " + socket.getInetAddress() + ": " + valueLine);
                }

                boolean outOfRange = false;
                switch (sensorType) {
                    case "Temperature" -> {
                        if (value < -15 || value > 35) {
                            outOfRange = true;
                        }
                    }
                    case "Pressure" -> {
                        if (value < 650 || value > 1199) {
                            outOfRange = true;
                        }
                    }
                    case "Oxygen" -> {
                        if (value < 14 || value > 100) {
                            outOfRange = true;
                        }
                    }
                    case "CO2" -> {
                        if (value < 200 || value > 2000) {
                            outOfRange = true;
                        }
                    }
                }
                if (outOfRange) {
                    message = "Received from client " + socket.getInetAddress() + ": " + sensorType + " out of range! (value: " + value + ")";
                    System.out.println(message);
                    writer.println(message);
                    SimpleLogger.log(message);
                } else {
                    System.out.println(message);
                    message = "Received from client " + socket.getInetAddress() + ": " + sensorType + ": " + value;
                    writer.println(message);
                }
            }
        } catch (Exception e) {
            SimpleLogger.log("Error: " + e.getMessage());
        } finally {
            SimpleLogger.log("Client disconnected (" + socket.getInetAddress() + ")");
        }
    }
}
