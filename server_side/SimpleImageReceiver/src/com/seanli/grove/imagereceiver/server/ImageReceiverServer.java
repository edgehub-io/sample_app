package com.seanli.grove.imagereceiver.server;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ImageReceiverServer {
    private static int criminalCount = 1;
    public void init() {
        try {
            ServerSocket serverSocket = new ServerSocket(8090);
            while (true) {
                System.out.println("Getting New Client");
                Socket client = serverSocket.accept();
                System.out.println("New Client");
                new HandlerThread(client);
            }
        } catch (Exception e) {
            System.out.println("Encountered Error " + e);
        }
    }

    private class HandlerThread implements Runnable {
        private Socket socket;

        public HandlerThread(Socket client) throws Exception {
            socket = client;
            new Thread(this).start();
        }

        public void run() {
            try {
                InputStream in = socket.getInputStream();
                String criminalFileName = "/home/ec2-user/imageserver/criminals/criminal-" + criminalCount + ".jpg";
                FileOutputStream fos = new FileOutputStream(criminalFileName);
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = in.read(buf)) != -1)
                {
                    fos.write(buf,0,len);
                }
                System.out.println("Got image "+ criminalFileName);
                OutputStream out = socket.getOutputStream();
                out.write("Got image!".getBytes());
                fos.close();
                in.close();
                out.close();
                criminalCount++;
            } catch (Exception e) {
                System.out.println("Run Failed " + e);
            } finally {
                closeSocket(socket);
            }
        }
    }

    private void closeSocket(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                socket = null;
                System.out.println("Close Failed " + e);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("ImageReceiverServer Starting...");
        ImageReceiverServer server = new ImageReceiverServer();
        server.init();
    }
}
