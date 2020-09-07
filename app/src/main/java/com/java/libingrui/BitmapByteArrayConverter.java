package com.java.libingrui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.nio.ByteBuffer;

public class BitmapByteArrayConverter {

    static public byte[] BitmapToByteArray(Bitmap bitmap) {
        int bytes = bitmap.getByteCount();

        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buf);

        return buf.array();
    }

    static public Bitmap ByteArrayToBitmap(byte[] bitmapData) {
        return BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
    }
}
