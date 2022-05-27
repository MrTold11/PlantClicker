package com.example.plantclicker.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.plantclicker.R;
import com.example.plantclicker.Utils;
import com.example.plantclicker.shop.ShopCategory;
import com.example.plantclicker.shop.ShopItem;
import com.example.plantclicker.shop.ShopRecyclerViewAdapter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.example.plantclicker.Utils.parseAndAdd;
import static com.example.plantclicker.activity.ClickerActivity.APP_PREFERENCES_COINSVALUE;
import static com.example.plantclicker.shop.ShopRecyclerViewAdapter.COLOR_YELLOW;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener, ShopRecyclerViewAdapter.ItemClickListener {

    SharedPreferences shopData, saveSettings;
    RecyclerView recyclerView;
    ShopRecyclerViewAdapter adapter;
    TextView valueOfCoins, categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        shopData = getSharedPreferences("shopData", Context.MODE_PRIVATE);
        saveSettings = getSharedPreferences("mySettings", Context.MODE_PRIVATE);

        valueOfCoins = findViewById(R.id.shopCoins);
        valueOfCoins.setText(saveSettings.getString(APP_PREFERENCES_COINSVALUE, "0"));
        categoryName = findViewById(R.id.categoryName);
        LinearLayout categoryLayout = findViewById(R.id.categoryLayout);

        recyclerView = findViewById(R.id.shopItemsView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        //Fill categories icons
        for (ShopCategory c : ShopCategory.values()) {
            ImageView icon = new ImageView(this);
            ViewGroup.LayoutParams params = icon.getLayoutParams();
            int dp64 = (int) Utils.dpToPx(this, 64);
            if (params == null)
                params = new ViewGroup.LayoutParams(dp64, dp64);
            else {
                params.width = dp64;
                params.height = dp64;
            }
            icon.setLayoutParams(params);
            icon.setScaleX(0.8f);
            icon.setScaleY(0.8f);
            icon.setTag(c);
            icon.setOnClickListener(this);
            icon.setImageDrawable(c.getDrawable(this));
            categoryLayout.addView(icon);
        }
        openCategory(ShopCategory.LAMPS);
    }

    private boolean popupActive = false;

    @Override
    public void onItemClick(View view, int position) {
        if (popupActive) return;
        popupActive = true;

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.item_popup_layout, (ViewGroup) view, false);

        if (((TextView) view.findViewById(R.id.itemPrice)).getCurrentTextColor() != COLOR_YELLOW)
            popupView.findViewById(R.id.buyButton).setEnabled(false);
        ShopItem item = adapter.category.items[position];
        ((TextView) popupView.findViewById(R.id.nameView)).setText(item.name);
        ((TextView) popupView.findViewById(R.id.descriptionView)).setText(item.description);
        ((TextView) popupView.findViewById(R.id.costView)).setText(String.valueOf(item.price));
        ((ImageView) popupView.findViewById(R.id.imageView)).setImageDrawable(item.getDrawable(this));

        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.setElevation(20);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        View container = popupWindow.getContentView().getRootView();
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        wm.updateViewLayout(container, p);

        View.OnClickListener onPopupClick = v -> {
            popupWindow.dismiss();
            if (v.getId() == R.id.buyButton)
                buyItem(adapter.category, adapter.category.items[position]);
        };
        popupWindow.setOnDismissListener(() -> popupActive = false);

        popupView.findViewById(R.id.buyButton).setOnClickListener(onPopupClick);
        popupView.findViewById(R.id.backImageView).setOnClickListener(onPopupClick);
    }

    private void openCategory(ShopCategory category) {
        categoryName.setText(category.name);
        adapter = new ShopRecyclerViewAdapter(this, category,
                shopData.getStringSet(category.name, Collections.emptySet()),
                Utils.safeParse(valueOfCoins.getText().toString(), 0));
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void buyItem(ShopCategory category, ShopItem item) {
        Set<String> set = new HashSet<>(shopData.getStringSet(category.name, Collections.emptySet()));
        set.add(item.name);
        //todo enable?

        SharedPreferences.Editor editor = shopData.edit();
        editor.putStringSet(category.name, set);
        editor.apply();

        editor = saveSettings.edit();
        editor.putString(APP_PREFERENCES_COINSVALUE, parseAndAdd(valueOfCoins.getText().toString(), -1*item.price, 0));
        editor.apply();

        valueOfCoins.setText(saveSettings.getString(APP_PREFERENCES_COINSVALUE, "0"));
        openCategory(category);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.prevActivity) {
            finish();
            return;
        }

        try {
            openCategory((ShopCategory) view.getTag());
        } catch (Exception ignored) {}

    }
}