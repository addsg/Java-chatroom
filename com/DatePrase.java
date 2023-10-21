package MyQQ_Final.com;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DatePrase {
    public static String Prase(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String str = sdf.format(date);
        return str;
    }
}
