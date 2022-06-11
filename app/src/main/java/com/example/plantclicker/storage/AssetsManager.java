package com.example.plantclicker.storage;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * @author Mr_Told
 */
public class AssetsManager {

    final Random random = new Random(System.currentTimeMillis());
    final Context context;

    final String[] plants;
    final String plantsDirSep;

    public AssetsManager(Context c, String plantsDir) {
        try {
            plants = c.getAssets().list(plantsDir);
            if (plants == null || plants.length == 0)
                throw new RuntimeException("No plants images in assets folder");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        plantsDirSep = plantsDir + File.separator;
        context = c;
    }

    public String randomPlant() {
        return plants[random.nextInt(plants.length)];
    }

    public Drawable loadPlant(String name) {
        try (InputStream is = context.getAssets().open(plantsDirSep + name)) {
            return Drawable.createFromStream(is, null);
        } catch (IOException ignored) {}
        return null;
    }

}
