package com.lifedrained.prepjournal.Utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lifedrained.prepjournal.data.Visit;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Predicate;

public class SerializationUtils {
    public static String toJsonVisits(List<Visit> visits){
       return new Gson().toJson(visits);
    }
    public static List<Visit> toListVisits(String json){
        Type type = getJsonType();
        return new Gson().fromJson(json, type);
    }
    public static Visit findByUid(List<Visit> visits,String uid){
        visits.removeIf(visit -> !visit.getScheduleUID().equals(uid));
        if (!visits.isEmpty()){
            return visits.get(0);
        }
        return null;
    }
    public static Type getJsonType(){
        return new TypeToken<List<Visit>>(){}.getType();
    }
}
