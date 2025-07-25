package gdtcs.base.util;

import com.ibm.icu.util.ChineseCalendar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class DateUtil {
	/***
     * 입력받은 양력일자를 변환하여 음력일자로 반환
     * @param solarDate 양력일자
     * @return 음력일자
     */
    public static Map<String,Object> toLunar(String solarDate){
        String dateString = validChkDate(solarDate);

        Map<String,Object> hm = new HashMap<>();
        hm.put("day","");
        hm.put("leap",0);

        if(dateString.length() != 8){
            return hm;
        }

        Calendar calendar;
        ChineseCalendar lunarCalendar;

        calendar = Calendar.getInstance();
        lunarCalendar = new ChineseCalendar();

        calendar.set(Calendar.YEAR,Integer.parseInt(dateString.substring(0,4)));
        calendar.set(Calendar.MONTH,Integer.parseInt(dateString.substring(4,6)) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString.substring(6,8)));

        lunarCalendar.setTimeInMillis(calendar.getTimeInMillis());

        String year = String.valueOf(lunarCalendar.get(ChineseCalendar.EXTENDED_YEAR) - 2637);
        String month = String.valueOf(lunarCalendar.get(ChineseCalendar.MONTH) +1);
        String day = String.valueOf(lunarCalendar.get(ChineseCalendar.DAY_OF_MONTH));
        String leap = String.valueOf(lunarCalendar.get(ChineseCalendar.IS_LEAP_MONTH));

        String padding4String = "0000";
        String padding2String = "00";

        String retYear = (padding4String + year).substring(year.length());
        String retMoth = (padding2String + month).substring(month.length());
        String retDay = (padding2String + day).substring(day.length());

        String SDay = retYear + retMoth + retDay;

        hm.put("day",SDay);
        hm.put("leap",leap);

        return hm;
    }

    /***
     * 입력된 일자 문자열을 확인하고 8자리로 리턴
     * @param dateString 입력된 일자 문자열
     * @return 변환된 일자
     */
    public static String validChkDate(String dateString){
        String _dateString = dateString;
        if(dateString == null || !(dateString.trim().length() == 8 || dateString.trim().length() == 10)){
            throw new IllegalArgumentException("Invalid date format : "+dateString);
        }
        if(dateString.length() == 10){
            _dateString = dateString.replace("-","");
        }
        return _dateString;
    }

}
