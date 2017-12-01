package virtual_coin_checker.sincdor.coinchecker.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import virtual_coin_checker.sincdor.coinchecker.models.Ticker;


public class TickerRetroS {
    private TickerRetro tickerRetro;
    private final static String BASE_URL = "https://api.coinmarketcap.com/v1/ticker/";
    public TickerRetroS(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tickerRetro = retrofit.create(TickerRetro.class);
    }

    public void getTickerCoin(String coin, Callback<List<Ticker>> callback){
        Call<List<Ticker>> tickerCall = tickerRetro.getTickerCoin(coin);
        tickerCall.enqueue(callback);

    }

    public void getTickerAllCoin(String limit, Callback<List<Ticker>> callback){
        Call<List<Ticker>> tickerCAll = tickerRetro.getTickerAllCoins(limit);
        tickerCAll.enqueue(callback);
    }
}
