package virtual_coin_checker.sincdor.coinchecker;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import virtual_coin_checker.sincdor.coinchecker.adapters.MainActivityAdapter;
import virtual_coin_checker.sincdor.coinchecker.models.Ticker;
import virtual_coin_checker.sincdor.coinchecker.retrofit.TickerRetroS;
import virtual_coin_checker.sincdor.coinchecker.utils.Utils;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {

    ArrayList<Ticker> dataSet;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    TickerRetroS service;
    private static final String ONE_HOUR = "ONE_HOUR";
    private static final String TWENTY_FOUR_HOURS = "TWENTY_FOUR_HOURS";
    private static final String SEVEN_DAYS  = "SEVEN_DAYS";

    SearchView sv;

    ArrayList<String> coins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("vertcoin");
        FirebaseMessaging.getInstance().subscribeToTopic("bitcoin");

        coins = new ArrayList<>();
        coins.add("vertcoin");
        coins.add("bitcoin");
        coins.add("nxt");
        coins.add("litecoin");

        service = new TickerRetroS();

        dataSet = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar_ALU);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.id_content_main_activity_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new MainActivityAdapter(dataSet, Utils.TWENTY_FOUR_HOURS_CHANGE, this);
        recyclerView.setAdapter(adapter);

        updateView(null, coins);

    }

    private void updateView(String sHour, List<String>coins){

        for(String coin : coins) {
            service.getTickerCoin(coin,
                    new Callback<List<Ticker>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Ticker>> call,
                                               @NonNull Response<List<Ticker>> response) {
                            if (response.body() != null) {
                                Ticker t = null;
                                try {
                                    if (response.body().size() > 0)
                                        t = response.body().get(0);
                                    if (t != null) {
                                        Log.d(TAG, "onResponse: Coin:" + t.getId() + " price:" + t.getPrice_usd());
                                        dataSet.add(t);
                                        if (adapter != null)
                                            adapter.notifyDataSetChanged();
                                    }

                                } catch (ClassCastException e) {
                                    Log.e(TAG, "onResponse: Couldn't cast");
                                }
                            }else{
                                Log.e(TAG, "onResponse: response.body() = null");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Ticker>> call,
                                              @NonNull Throwable t) {
                            Log.e(TAG, "onFailure: ");
                        }
                    }
            );
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sv.setSearchableInfo(
                searchManager != null ? searchManager.getSearchableInfo(getComponentName()) : null);
        if(sv != null){
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    sv.setIconified(true);
                    hideKeyboard();
                    if(query != null) {
                        searchForQuery(query);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        return true;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void searchForQuery(final String query){
        service.getTickerCoin(query,
                new Callback<List<Ticker>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Ticker>> call, @NonNull Response<List<Ticker>> response) {
                        try {
                            if (response.body() != null && response.body().size() > 0) {
                                List<Ticker> list = response.body();
                                if (list != null && list.size() > 0) {
                                    Log.d(TAG, "onResponse: Response found for query");
                                    coins.add(query);
                                    ((MainActivityAdapter)adapter).addTicker(list.get(0));
                                    recyclerView.smoothScrollToPosition(0);

                                }
                            } else {
                                CoordinatorLayout cl = findViewById(R.id.id_activity_main_coordinator_layout);
                                if (cl != null)
                                    Snackbar.make(cl, "No results found!", Snackbar.LENGTH_SHORT);
                            }
                        }catch (Exception e){
                            Log.e(TAG, "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Ticker>> call, Throwable t) {
                        CoordinatorLayout cl = findViewById(R.id.id_activity_main_coordinator_layout);
                        if (cl != null)
                            Snackbar.make(cl, "Check your internet connection!", Snackbar.LENGTH_SHORT);
                        Log.e(TAG, "onFailure: " + t);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.id_one_hour_menu:
                ((MainActivityAdapter)adapter).setChangeType(Utils.ONE_HOUR_CHANGE);
                dataSet = new ArrayList<>();
                adapter.notifyDataSetChanged();
                updateView(ONE_HOUR, coins);
                return true;
            case R.id.id_24_hours_menu:
                ((MainActivityAdapter)adapter).setChangeType(Utils.TWENTY_FOUR_HOURS_CHANGE);
                dataSet = new ArrayList<>();
                adapter.notifyDataSetChanged();
                updateView(TWENTY_FOUR_HOURS, coins);
                return true;
            case R.id.id_7_days_menu:
                ((MainActivityAdapter)adapter).setChangeType(Utils.SEVEN_DAYS_CHANGE);
                dataSet = new ArrayList<>();
                adapter.notifyDataSetChanged();
                updateView(SEVEN_DAYS, coins);
                return true;
            case R.id.app_bar_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (!sv.isIconified()) {
            sv.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }
}
