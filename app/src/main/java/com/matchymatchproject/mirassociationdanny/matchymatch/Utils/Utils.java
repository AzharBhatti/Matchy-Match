package com.matchymatchproject.mirassociationdanny.matchymatch.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;

/**
 * Created by HP on 10/3/2018.
 */

public class Utils {
    public static byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,stream);
        return stream.toByteArray();
    }

    public static void removePuzzleName(String name, Context context, String key)
    {
        SharedPreferences myFirst = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myFirst.edit();
        editor.putString(key, "");
        editor.commit();
    }

    public static Bitmap getImage(byte[] data)
    {
        return BitmapFactory.decodeByteArray(data,0,data.length);
    }
    public static final Uri getUriToResource(@NonNull Context context,
                                             @AnyRes int resId)
            throws Resources.NotFoundException {
        /** Return a Resources instance for your application's package. */
        Resources res = context.getResources();
        /**
         * Creates a Uri which parses the given encoded URI string.
         * @param uriString an RFC 2396-compliant, encoded URI
         * @throws NullPointerException if uriString is null
         * @return Uri for this given uri string
         */
        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));
        /** return uri */
        return resUri;
    }
}
