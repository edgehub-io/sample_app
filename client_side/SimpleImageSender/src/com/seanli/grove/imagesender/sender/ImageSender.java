package com.seanli.grove.imagesender.sender;
 
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
 
public class ImageSender {
    public static void main(String[] args) throws Exception{
        String badguyPic = args[0];
        FileInputStream fis = new FileInputStream(badguyPic);
        Socket s = new Socket("34.217.40.191",8090);
        System.out.println("Connected!");
        OutputStream out = s.getOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = fis.read(buf)) != -1)
        {
            out.write(buf,0,len);
        }
        s.shutdownOutput();
        InputStream in = s.getInputStream();
        byte[] bufIn = new byte[1024];
        int num = in.read(bufIn);
        System.out.println(new String(bufIn,0,num));
        fis.close();
        out.close();
        in.close();
        s.close();
    }
}