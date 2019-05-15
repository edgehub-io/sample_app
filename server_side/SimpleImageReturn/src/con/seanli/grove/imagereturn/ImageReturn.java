package con.seanli.grove.imagereturn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class ImageReturn {
    public void init() {
        try {
            ServerSocket serverSocket = new ServerSocket(8091);
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
                System.out.println(in.available());
                byte[] rBytes = new byte[1024];
                byte[] sBytes = "No Updates".getBytes();
                in.read(rBytes, 0, 1024);
                String rStr = new String(rBytes, 0, 1024);
                System.out.println("received msg:"+rStr);
                int currentCriminal = Integer.parseInt(rStr.trim().toString());
                System.out.println("current criminal:" + currentCriminal);
                OutputStream out = socket.getOutputStream();
                final File folder = new File("/home/ec2-user/imageserver/criminals");
                List<File> files = listFilesForFolder(folder);
                List<Integer> fileNames = new ArrayList<>();
                for (File file : files) {
                    fileNames.add(Integer.valueOf(file.getName().split("-")[1].replaceAll(".jpg", "")));
                }
                OptionalInt max = fileNames.stream().mapToInt(Integer::intValue).max();
                int latestCriminal = max.getAsInt();
                if (latestCriminal > currentCriminal) {
                    File latestCriminalImage = new File("/home/ec2-user/imageserver/criminals/criminal-" + latestCriminal + ".jpg");
                    System.out.println("latest criminal:" + latestCriminalImage.getName());
                    FileInputStream fis = new FileInputStream(latestCriminalImage);
                    byte[] buf = new byte[1024];
                    int len = 0;
                    while ((len = fis.read(buf)) != -1) {
                        out.write(buf, 0, len);
                    }
                    socket.shutdownOutput();
                    InputStream ins = socket.getInputStream();
                    byte[] bufIn = new byte[1024];
                    ins.read(bufIn);
                    fis.close();
                    System.out.println("image written");
                   
                    out.close();
                    in.close();
                } else {
                    System.out.println("no need to write image");
                    out.close();
                    in.close();
                }
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
        System.out.println("ImageReturn Starting...");
        ImageReturn server = new ImageReturn();
        server.init();
    }

    public List<File> listFilesForFolder(final File folder) {
        List<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                files.add(fileEntry);
            }
        }
        return files;
    }
}
