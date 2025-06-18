package com.lifedrained.prepjournal.Utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lifedrained.prepjournal.data.Visit;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class SerializationUtils {
    private static final Logger log = LogManager.getLogger(SerializationUtils.class);

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

    public static GlobalVisitor findByVisitor(List<GlobalVisitor> visitors , Visit visit) {
        visitors.removeIf(globalVisitor -> globalVisitor.getId() != visit.getVisitorId());
        if (!visitors.isEmpty()){
            log.error("No visitor found with id " + visit.getVisitorId());
        }
        return visitors.getFirst();
    }
    public static Visit findByUid(String  jsonVisits,String uid){
        List<Visit> visits = toListVisits(jsonVisits);
        return findByUid(visits, uid);
    }
    public static Type getJsonType(){
        return new TypeToken<List<Visit>>(){}.getType();
    }
}
