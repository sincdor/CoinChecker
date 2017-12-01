package virtual_coin_checker.sincdor.coinchecker.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by andre on 28-11-2017.
 */

public class Utils {

    public static final Integer ONE_HOUR_CHANGE = 1;
    public static final Integer TWENTY_FOUR_HOURS_CHANGE = 2;
    public static final Integer SEVEN_DAYS_CHANGE = 3;

    public static String getDateTime(){
        Date currentDate = Calendar.getInstance().getTime();
        return currentDate.toString();
    }

}
