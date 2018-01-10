package random;

import java.util.Calendar;
import java.util.Random;

/**
 * @author 98267
 */
public class RandomHandler {

    /**
     * 年、月、日、时、分、秒的长度必须为两位数
     */
    private final int TIMEMINSIZE=2;
    /**
     * 获取当前时间信息，用于生成id,id的唯一性
     * @return id的时间部分（如：20171219150500）（14位）
     */
    private String getTimeInfo(){
        Calendar now = Calendar.getInstance();
        String year=String.valueOf(now.get(Calendar.YEAR));
        String month=String.valueOf(now.get(Calendar.MONTH)+1);
        String day=String.valueOf(now.get(Calendar.DAY_OF_MONTH));
        String hour=String.valueOf(now.get(Calendar.HOUR_OF_DAY));
        String minute=String.valueOf(now.get(Calendar.MINUTE));
        String sec=String.valueOf(now.get(Calendar.SECOND));
        if(year.length()>TIMEMINSIZE){
            year=year.substring(year.length()-2);
        }
        if(month.length()<TIMEMINSIZE){
            month="0"+month;
        }
        if(day.length()<TIMEMINSIZE){
            day="0"+day;
        }
        if(hour.length()<TIMEMINSIZE){
            hour="0"+hour;
        }
        if(month.length()<TIMEMINSIZE){
            minute="0"+minute;
        }
        if(month.length()<TIMEMINSIZE){
            sec="0"+sec;
        }
        return year+month+day+hour+minute+sec;
    }
    /**
     * 获取一个长度为length的字符串
     * @return 一个长度为length的字符串
     */
    private String getRandom(int length){
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    /**
     * 获取固定长度的id
     * @param length id要求的长度
     * @return 唯一的id
     */
    public String getId(int length){
        String result=getTimeInfo();
        if(length >result.length()){
            result=result+getRandom(length-result.length());
        }
        return  result;
    }
    /**
     * 获取随机题号
     * @param length 题目列表的大小
     * @return 题目的序号
     */
//    public int getQuestion(int length){
//        Random random = new Random();
//        int result=random.nextInt(length);
//        return result;
//    }

}
