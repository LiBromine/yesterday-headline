package com.java.libingrui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.nio.ByteBuffer;

import android.util.Log;

public class BitmapByteArrayConverter {

    static public BitmapData BitmapToByteArray(Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap,500,500,true);

        int bytes = bitmap.getByteCount();

        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buf);

        BitmapData bitmapData = new BitmapData();
        bitmapData.bitmap = buf.array();
        bitmapData.height = bitmap.getHeight();
        bitmapData.width = bitmap.getWidth();
        bitmapData.name = bitmap.getConfig().name();

        Log.v("debug", "info:"+bitmap.getHeight()+" "+bitmap.getWidth()+" "+bitmap.getConfig().name());

        return bitmapData;
    }

    static public Bitmap ByteArrayToBitmap(BitmapData bitmapData) {
        Bitmap stitchBmp = Bitmap.createBitmap(bitmapData.width, bitmapData.height, Bitmap.Config.valueOf(bitmapData.name));
        stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(bitmapData.bitmap));
        return stitchBmp;
    }
}
