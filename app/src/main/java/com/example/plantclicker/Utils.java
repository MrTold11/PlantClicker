package com.example.plantclicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

public class Utils {

    public static String parseAndAdd(String value, int delta, int def) {
        return String.valueOf(safeParse(value, def) + delta);
    }

    public static int safeParse(String value, int def) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ignored) {
            return def;
        }
    }

    public static float dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static Drawable getDrawable(Context c, String name) {
        return c.getResources().getDrawable(c.getResources().getIdentifier(name, "drawable", c.getPackageName()), c.getTheme());
    }

    public static String getString(Context c, int id) {
        return c.getResources().getString(id);
    }

    public static int getResourceId(Context c, String name, String type) {
        int id = c.getResources().getIdentifier(name, type, c.getPackageName());
        if (id == 0)
            Log.e("PlantClickerUtils", "Failed to get resource id for '" + name + "'!!!");
        return id;
    }

}
