package com.contenttree.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtil {
    public static String encodeImageToBase64(String imagePath) throws IOException {
        File file = new File(imagePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStream.read(bytes);
        fileInputStream.close();
        return Base64.getEncoder().encodeToString(bytes);
    }
}
