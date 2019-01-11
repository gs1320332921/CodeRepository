package com.gs.coderepository.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 13203 on 2018/12/27.
 */

public class TimeUtils {
    /**
     * 获取未来 第 past 天的日期
     * @param past
     * @return
     */
    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    /**
     * 获取当前日期是周几
     * @return int  1-周一 2-周二......7-周天
     */
    public static int getWeekNoNow(){
        int[]weeks={7,1,2,3,4,5,6};
        Calendar calendar=Calendar.getInstance();
        int weekNo=calendar.get(Calendar.DAY_OF_WEEK)-1;
        return weeks[weekNo];
    }
}
