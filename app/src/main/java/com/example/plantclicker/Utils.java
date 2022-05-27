package com.example.plantclicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

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

}
