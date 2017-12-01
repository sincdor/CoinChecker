package virtual_coin_checker.sincdor.coinchecker.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import virtual_coin_checker.sincdor.coinchecker.models.Ticker;

/**
 * Created by andre on 28-11-2017.
 */

public interface TickerRetro {
    @GET("{coin}/")
    Call<List<Ticker>> getTickerCoin(@Path("coin") String coin);

    @GET()
    Call<List<Ticker>> getTickerAllCoins(@Query("limit") String limit);

}
