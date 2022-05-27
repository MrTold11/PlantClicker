package com.example.plantclicker.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.plantclicker.R;
import com.example.plantclicker.Utils;
import com.example.plantclicker.shop.ShopCategory;

import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import static com.example.plantclicker.Utils.parseAndAdd;

public class ClickerActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    public static final String APP_PREFERENCES_COINSVALUE = "CoinsValue";
    public static final String APP_PREFERENCES_PROGRESSVALUE = "progressValue";

    ProgressBar progressBar;
    int fingers, auto, clickPower;

    SharedPreferences saveSettings, shopData;
    TextView valueOfCoins, autoClick;
    ImageView plantTop, plantBottom;
    Handler autoClickHandler = new Handler(Looper.getMainLooper());
    Runnable autoClickRunnable = () -> {
        processProgress(auto);
        autoClickHandler.postDelayed(this.autoClickRunnable, 1000);
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plantTop = findViewById(R.id.plantTop);
        plantTop.setOnTouchListener(this);
        plantBottom = findViewById(R.id.plantBottom);
        plantBottom.setOnTouchListener(this);

        progressBar = findViewById(R.id.progressBar);
        valueOfCoins = findViewById(R.id.ValueOfCoins);
        autoClick = findViewById(R.id.AutoClick);

        saveSettings = getSharedPreferences("mySettings", Context.MODE_PRIVATE);
        shopData = getSharedPreferences("shopData", Context.MODE_PRIVATE);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            fingers++;
            if (fingers < 3) {
                processProgress(clickPower);
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP)
            fingers--;
        return true;
    }

    private void processProgress(int add) {
        int delta = progressBar.getProgress() + add - progressBar.getMax();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            progressBar.setProgress(progressBar.getProgress() + add, true);
        else
            progressBar.setProgress(progressBar.getProgress() + add);

        if (progressBar.getProgress() > progressBar.getMax() - 1) {
            randomPlant();
            progressBar.setProgress(0);
            updateProgressMaximum();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                progressBar.setProgress(delta, true);
            else
                progressBar.setProgress(delta);
            valueOfCoins.setText(parseAndAdd((String) valueOfCoins.getText(), progressBar.getMax() / 3, 0));
        }
    }

    private void updateProgressMaximum() {
        progressBar.setMax((int) (20 + Math.pow((auto + clickPower) / 2f, 2.5)));
    }

    final Random random = new Random(System.currentTimeMillis());
    final String[] plantDrawables = new String[] {
            "aloevera",
            //"cactusup",
            "chamomile",
            "chrysanthemum",
            "pineapple",
            "tulip",
            "rose"
    };

    private void randomPlant() {
        String name = plantDrawables[random.nextInt(plantDrawables.length)];
        plantTop.setTag(name);
        plantTop.setImageDrawable(Utils.getDrawable(this, name));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.shoppingCart) {
            Intent intent = new Intent(ClickerActivity.this, ShopActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        valueOfCoins.setText(saveSettings.getString(APP_PREFERENCES_COINSVALUE, "0"));
        progressBar.setProgress(saveSettings.getInt(APP_PREFERENCES_PROGRESSVALUE, 0));

        Set<String> lamps = shopData.getStringSet(ShopCategory.LAMPS.name, Collections.emptySet());
        auto = 0;
        if (lamps.contains(ShopCategory.LAMPS.items[6].name)) auto += 20;
        if (lamps.contains(ShopCategory.LAMPS.items[5].name)) auto += 15;
        if (lamps.contains(ShopCategory.LAMPS.items[4].name)) auto += 10;
        if (lamps.contains(ShopCategory.LAMPS.items[3].name)) auto += 7;
        if (lamps.contains(ShopCategory.LAMPS.items[2].name)) auto += 5;
        if (lamps.contains(ShopCategory.LAMPS.items[1].name)) auto += 2;
        if (lamps.contains(ShopCategory.LAMPS.items[0].name)) auto += 1;

        Set<String> boosts = shopData.getStringSet(ShopCategory.BOOSTERS.name, Collections.emptySet());
        clickPower = 1;
        if (boosts.contains(ShopCategory.BOOSTERS.items[3].name)) clickPower += 10;
        if (boosts.contains(ShopCategory.BOOSTERS.items[2].name)) clickPower += 7;
        if (boosts.contains(ShopCategory.BOOSTERS.items[1].name)) clickPower += 5;
        if (boosts.contains(ShopCategory.BOOSTERS.items[0].name)) clickPower += 2;

        updateProgressMaximum();

        autoClick.setText(String.format(Locale.getDefault(), "+%d/sec", auto));
        autoClickHandler.postDelayed(autoClickRunnable, 1000);

        String plant = saveSettings.getString("currentPlant", plantDrawables[0]);
        plantTop.setTag(plant);
        plantTop.setImageDrawable(Utils.getDrawable(this, plant));
    }

    @Override
    protected void onPause() {
        autoClickHandler.removeCallbacks(autoClickRunnable);
        super.onPause();
        SharedPreferences.Editor editor = saveSettings.edit();
        editor.putString(APP_PREFERENCES_COINSVALUE, valueOfCoins.getText().toString());
        editor.putInt(APP_PREFERENCES_PROGRESSVALUE, progressBar.getProgress());
        try {
            String plant = (String) plantTop.getTag();
            editor.putString("currentPlant", plant);
        } catch (Exception ignored) {}

        editor.apply();
    }
}