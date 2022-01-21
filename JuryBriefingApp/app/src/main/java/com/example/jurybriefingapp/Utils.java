package com.example.jurybriefingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {
    public static Bitmap Base64ToBitmap(String base64String) {
        byte[] imageByteArray = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        return decodedByte;
    }

    public static ZipInputStream Base64ToZipStream(String base64String) {
        byte[] imageByteArray = Base64.decode(base64String, Base64.DEFAULT);

        InputStream inputStream = new ByteArrayInputStream(imageByteArray);
        ZipInputStream  zipInputStream = new ZipInputStream(inputStream);

        return zipInputStream;
    }

    public static void UnzipStream(ZipInputStream zipInputStream, Context context) throws IOException {
        byte[] buffer = new byte[2048];
        try
        {
            String root = context.getFilesDir().toString();
            ZipEntry entry;
            while((entry = zipInputStream.getNextEntry()) != null)
            {
                String outpath = root + "/" + Constants.SLIDES_DIR + "/" + entry.getName();
                FileOutputStream output = null;
                try
                {
                    output = new FileOutputStream(outpath);
                    int len = 0;
                    while ((len = zipInputStream.read(buffer)) > 0)
                    {
                        output.write(buffer, 0, len);
                    }
                }
                finally
                {
                    if(output != null) output.close();
                }
            }
        } finally
        {
            zipInputStream.close();
        }
    }

    public static void DeleteFilesInDirectory(File dir) {
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }
}
