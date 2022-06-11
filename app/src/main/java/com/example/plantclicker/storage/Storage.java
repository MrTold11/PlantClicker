package com.example.plantclicker.storage;

import androidx.annotation.NonNull;

import java.util.Set;

/**
 * @author Mr_Told
 */
public interface Storage {

    /**
     * @return stored coins value or zero
     */
    @NonNull
    String getCoins();

    /**
     * @param def default value
     * @return latest plant filename or default
     */
    @NonNull
    String getPlant(String def);

    /**
     * @return stored progress value or zero
     */
    int getProgress();

    /**
     * @param label items key
     * @return stored items' names or empty set
     */
    @NonNull
    Set<String> getItems(String label);

    /**
     * Puts value into editor
     * @param coins coins value as a string
     */
    void saveCoins(@NonNull String coins);

    /**
     * Puts value into editor
     * @param progress progress value
     */
    void saveProgress(int progress);

    /**
     * Puts value into editor
     * @param plant plant filename
     */
    void savePlant(@NonNull String plant);

    /**
     * Puts value into editor
     * @param label items key
     * @param items items set
     */
    void saveItems(@NonNull String label, @NonNull Set<String> items);

    /**
     * Save data from editor
     */
    void apply();

}
