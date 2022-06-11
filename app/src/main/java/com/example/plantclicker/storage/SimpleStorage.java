package com.example.plantclicker.storage;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.Set;

/**
 * @author Mr_Told
 */
public class SimpleStorage implements Storage {

    static final String COINS_KEY = "coins";
    static final String PROGRESS_KEY = "progress";
    static final String PLANT_KEY = "currentPlant";

    final SharedPreferences settings, inventory;
    SharedPreferences.Editor settingsEditor, inventoryEditor;

    public SimpleStorage(Context c) {
        settings = c.getSharedPreferences("settings", Context.MODE_PRIVATE);
        inventory = c.getSharedPreferences("inventory", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public String getCoins() {
        applySettings();
        return settings.getString(COINS_KEY, "0");
    }

    @NonNull
    @Override
    public String getPlant(String def) {
        applySettings();
        return settings.getString(PLANT_KEY, def);
    }

    @Override
    public int getProgress() {
        applySettings();
        return settings.getInt(PROGRESS_KEY, 0);
    }

    @NonNull
    @Override
    public Set<String> getItems(String label) {
        applyInventory();
        return inventory.getStringSet(label, Collections.emptySet());
    }

    private void checkSettingsEditor() {
        if (settingsEditor == null) settingsEditor = settings.edit();
    }

    @Override
    public void saveCoins(@NonNull String coins) {
        checkSettingsEditor();
        settingsEditor.putString(COINS_KEY, coins);
    }

    @Override
    public void saveProgress(int progress) {
        checkSettingsEditor();
        settingsEditor.putInt(PROGRESS_KEY, progress);
    }

    @Override
    public void savePlant(@NonNull String plant) {
        checkSettingsEditor();
        settingsEditor.putString(PLANT_KEY, plant);
    }

    @Override
    public void saveItems(@NonNull String label, @NonNull Set<String> items) {
        if (inventoryEditor == null) inventoryEditor = inventory.edit();
        inventoryEditor.putStringSet(label, items);
    }

    @Override
    public void apply() {
        applySettings();
        applyInventory();
    }

    public void applySettings() {
        if (settingsEditor != null) {
            settingsEditor.apply();
            settingsEditor = null;
        }
    }

    public void applyInventory() {
        if (inventoryEditor != null) {
            inventoryEditor.apply();
            inventoryEditor = null;
        }
    }

}
