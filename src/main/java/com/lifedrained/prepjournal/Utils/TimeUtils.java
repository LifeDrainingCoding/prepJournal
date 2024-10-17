package com.lifedrained.prepjournal.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

public class TimeUtils {
    private static final Logger log = LogManager.getLogger(TimeUtils.class);

    private static String refactorHours(String hrs){
        if(hrs.startsWith("0") && hrs.startsWith("0",1)){

            return "";

        }

        if(hrs.startsWith("0")){

            return hrs.replaceFirst("0","");

        }

        return hrs;
    }
    public static String convertToString(int time){
        if(time == 0){
            return "00:00";
        }
        String s = String.valueOf(time);
        if(s.length()>4){
            log.error("error during converting int time to string. Int time: {}",time);
            return "00:00";
        }
        String[] parts = s.split("");
        StringBuilder sb =  new StringBuilder();
        switch (parts.length){
            case 1: return sb.append("00:0").append(parts[0]).toString();
            case 2: return sb.append("00:").append(parts[0]).append(parts[1]).toString();
            case 3: return sb.append("0").append(parts[0]).append(":")
                    .append(parts[1]).append(parts[2]).toString();
            default: return sb.append(parts[0]).append(parts[1]).append(":")
                    .append(parts[2]).append(parts[3]).toString();
        }

    }
    public static int convertToInt(String hrs, String mins){

        try {
            if(hrs.isEmpty()){
                if(mins.startsWith("0") && mins.startsWith("0",1)){

                    return 0;

                }
                if(mins.startsWith("0")){

                    int minsInt = Integer.parseInt(mins.split("")[1]);
                    if(!checkIntMins(minsInt)){

                        return 0;

                    }
                }

            }
            hrs = refactorHours(hrs);
            if(checkIntHrs(Integer.parseInt(hrs))){

                return Integer.parseInt((hrs+mins));

            }

        }catch (NumberFormatException ex){
            log.error(ex);
        }

        return 0;

    }

    private static boolean checkIntMins(int mins){
        if (mins<0 || mins>59){
            log.error("Minutes check failed: {}", mins);
            return false;

        }

        return true;

    }
    private static boolean checkIntHrs(int hrs){
        if(hrs<0 || hrs>23){
            log.error("Hours check failed: {}", hrs);
            return false;

        }

        return true;

    }
}
