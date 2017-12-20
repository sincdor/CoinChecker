package virtual_coin_checker.sincdor.coinchecker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static java.text.DateFormat.getDateTimeInstance;

/**
 * Created by andre on 28-11-2017.
 */

public class Utils {

    public static final Integer ONE_HOUR_CHANGE = 1;
    public static final Integer TWENTY_FOUR_HOURS_CHANGE = 2;
    public static final Integer SEVEN_DAYS_CHANGE = 3;
    public static final String SHARED_FILE_COIN_NAME = "FILE_COIN_LIST";
    public static final String COIN_STRING = "COIN_STRING_LIST";

    public static String getDateTime(){
        Date currentDate = Calendar.getInstance().getTime();
        Format f = getDateTimeInstance();
        return f.format(currentDate);
    }

    public static void saveCoin(String coinid, double units, Context context){
        Map<String, Double> coinsList = getCoinsList(context);
        if(coinsList != null){
            for(String key : coinsList.keySet()){
                if(key.equals(coinid))
                    return;
            }
            coinsList.put(coinid, units);
        }else{
            coinsList = new HashMap<>();
            coinsList.put(coinid, units);
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_FILE_COIN_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(coinsList);
        editor.putString(COIN_STRING, json);
        editor.apply();
    }

    public static void changeUnits(String coinid, double units, Context context){
        Map<String, Double> coinsList = getCoinsList(context);
        if(coinsList != null){
            coinsList.remove(coinid);
            coinsList.put(coinid, units);
        }else{
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_FILE_COIN_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(coinsList);
        editor.putString(COIN_STRING, json);
        editor.apply();
    }

    public static Map<String, Double> getCoinsList(Context context){
        HashMap<String, Double> coinsList;
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_FILE_COIN_NAME, Context.MODE_PRIVATE);
        String jsonPreferences = sharedPreferences.getString(COIN_STRING, "");

        Type type = new TypeToken<HashMap<String, Double>>() {}.getType();
        try{
            coinsList = gson.fromJson(jsonPreferences, type);
            return coinsList;
        }catch (JsonSyntaxException e){
            Log.e(TAG, "getCoinsList: " + e);
            return null;
        }
    }

}
