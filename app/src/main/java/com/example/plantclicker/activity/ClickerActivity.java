package com.example.plantclicker.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.example.plantclicker.R;
import com.example.plantclicker.shop.ShopCategory;
import com.example.plantclicker.shop.ShopItem;
import com.example.plantclicker.storage.AssetsManager;
import com.example.plantclicker.storage.SimpleStorage;
import com.example.plantclicker.storage.Storage;

import java.util.Locale;
import java.util.Set;

import static com.example.plantclicker.Utils.parseAndAdd;

public class ClickerActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    ProgressBar progressBar;
    int fingers, auto, clickPower;

    Storage storage;
    AssetsManager assets;
    TextView valueOfCoins, autoClick;
    ImageView plantTop, plantBottom;
    FrameLayout clickerSpace;
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
        plantBottom = findViewById(R.id.plantBottom);
        clickerSpace = findViewById(R.id.plantSpaceLayout);
        clickerSpace.setOnTouchListener(this);

        progressBar = findViewById(R.id.progressBar);
        valueOfCoins = findViewById(R.id.ValueOfCoins);
        autoClick = findViewById(R.id.AutoClick);

        storage = new SimpleStorage(this);
        assets = new AssetsManager(this, "plants");
        plantTop.startAnimation(AnimationUtils.loadAnimation(this, R.anim.plant_rotate));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            fingers++;
            if (fingers < 3) {
                spawnClickPopup(event.getX(), event.getY(), clickPower);
                processProgress(clickPower);
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP)
            fingers--;
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void spawnClickPopup(float x, float y, int value) {
        FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        p.leftMargin = (int) x - 15;
        p.topMargin = (int) y - 50;
        TextView text = new TextView(this);
        text.setLayoutParams(p);
        text.setTextColor(ContextCompat.getColor(this, R.color.light_blue));
        text.setTextSize(20);
        text.setShadowLayer(2, 0,  0, ContextCompat.getColor(this, R.color.blue));
        text.setText("+" + value);
        clickerSpace.addView(text);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.click_popup);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation paramAnimation) {}
            public void onAnimationRepeat(Animation paramAnimation) {}
            public void onAnimationEnd(Animation paramAnimation) {
                clickerSpace.post(() -> clickerSpace.removeView(text));
            }
        });

        text.startAnimation(animation);
    }

    private void processProgress(int add) {
        int delta = progressBar.getProgress() + add - progressBar.getMax();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            progressBar.setProgress(progressBar.getProgress() + add, true);
        else
            progressBar.setProgress(progressBar.getProgress() + add);

        if (progressBar.getProgress() > progressBar.getMax() - 1) {
            setPlantDrawable(assets.randomPlant());
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
        progressBar.setMax((int) (40 + Math.pow((auto + clickPower) / 2f, 2)));
    }

    private void setPlantDrawable(String name) {
        Drawable d = assets.loadPlant(name);
        if (d == null) {
            name = assets.randomPlant();
            d = assets.loadPlant(name);
        }
        plantTop.setTag(name);
        plantTop.setImageDrawable(d);
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
        valueOfCoins.setText(storage.getCoins());

        Set<String> lamps = storage.getItems(ShopCategory.LAMPS.getId());
        auto = 0;
        for (ShopItem item : ShopCategory.LAMPS.items)
            if (lamps.contains(item.getId())) auto += (int) item.value;

        Set<String> boosts = storage.getItems(ShopCategory.BOOSTERS.getId());
        clickPower = 1;
        for (ShopItem item : ShopCategory.BOOSTERS.items)
            if (boosts.contains(item.getId())) clickPower += (int) item.value;

        updateProgressMaximum();
        progressBar.setProgress(storage.getProgress());

        autoClick.setText(String.format(Locale.getDefault(), getString(R.string.autoclick_value), auto));
        autoClickHandler.postDelayed(autoClickRunnable, 1000);

        String plant = storage.getPlant(assets.randomPlant());
        setPlantDrawable(plant);
    }

    @Override
    protected void onPause() {
        autoClickHandler.removeCallbacks(autoClickRunnable);
        super.onPause();

        storage.saveCoins(valueOfCoins.getText().toString());
        storage.saveProgress(progressBar.getProgress());
        try {
            storage.savePlant((String) plantTop.getTag());
        } catch (Exception ignored) {}
        storage.apply();
    }
}