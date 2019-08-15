package helpers;

import java.io.*;

public class ImageUpload {

    public static void saveFile(InputStream uploadedInputStream, String location) throws IOException{

        new FileOutputStream(new File(location));
        OutputStream outputStream;
        int read;
        byte[] bytes = new byte[1024];

        outputStream = new FileOutputStream(new File(location));
        while((read = uploadedInputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,read);
        }
        outputStream.flush();
        outputStream.close();
    }

}
