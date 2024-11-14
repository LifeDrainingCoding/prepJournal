package com.lifedrained.prepjournal.data.searchengine;

import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.events.SearchEvent;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

public class SearchEngine<ENTITY extends BaseEntity,TYPE extends Enum<TYPE>> {
    private static final Logger log = LogManager.getLogger(SearchEngine.class);
    private final List<ENTITY> entitiesToSearchIn;
    private final List<SearchQuery<TYPE>> searchQueries;
    private final OnSearchEventListener<ENTITY> listener;
    public SearchEngine(List<ENTITY> entitiesToSearchIn, List<SearchQuery<TYPE>> searchQueries,
                        OnSearchEventListener<ENTITY> listener){
        this.entitiesToSearchIn = entitiesToSearchIn;
        this.searchQueries = searchQueries;
        this.listener = listener;

    }
    public void search(){
        searchQueries.forEach(searchQuery -> {
            switch (searchQuery.getType()){
                case SearchTypes.VISITOR_TYPE visitorType ->{
                    List<GlobalVisitor> visitors = (List<GlobalVisitor>) entitiesToSearchIn;
                    Object value  = searchQuery.getValue();
                    switch (visitorType){
                        case BY_AGE ->
                            visitors.removeIf(visitor -> {
                                try{
                                    int age = Integer.parseInt((String) value);
                                    return visitor.getAge()!=age;

                                }catch (ClassCastException ex){
                                    log.error(ex);
                                    return false ;
                                }

                            });

                        case BY_DATE -> visitors.removeIf(visitor -> {
                                   String date = DateUtils.getStringFromDate(visitor.getBirthDate());
                                   return compareStrings(date,value);
                                });

                        case BY_NAME -> visitors.removeIf(visitor ->
                                compareStrings(visitor.getName(), value));

                        case BY_GROUP -> visitors.removeIf(visitor ->
                                compareStrings(visitor.getGroup(), value));

                        case BY_NOTES -> visitors.removeIf(visitor ->
                                compareStrings(visitor.getNotes(),value));

                        case BY_SPECIALITY -> visitors.removeIf(visitor ->
                                compareStrings(visitor.getSpeciality(),value));

                        case BY_VISITS_NUM -> visitors.removeIf(visitor -> {
                            try{
                                int visitsNum = Integer.parseInt((String) value);
                                return visitor.getVisitedSchedulesYear()!=visitsNum;

                            }catch (ClassCastException ex){
                                log.error(ex);
                                return false ;
                            }
                        });

                        case BY_MASTER_NAME -> visitors.removeIf(visitor ->
                                compareStrings(visitor.getLinkedMasterName(),value));
                    }
                    listener.onSearchEvent((SearchEvent<ENTITY>) new SearchEvent<>(visitors));
                }
                default -> throw new IncompatibleSearchTypeException("Unexpected searchType: " + searchQuery);
            }
        });
    }
    private boolean compareStrings(String s1, Object object){
        if (object instanceof String s) {
            return !s1.contains(s);

        }else if(object instanceof Date d){
            String comparableDate = DateUtils.getStringFromDate(d);
            return !s1.contains(comparableDate);
        }
        return false;
    }
}
