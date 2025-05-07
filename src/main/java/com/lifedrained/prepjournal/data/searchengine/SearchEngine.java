package com.lifedrained.prepjournal.data.searchengine;

import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.events.SearchEvent;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import static com.lifedrained.prepjournal.data.searchengine.SearchTypes.VISITOR_TYPE.*;

public class SearchEngine<ENTITY extends BaseEntity,TYPE extends Enum<TYPE>> {
    private static final Logger log = LogManager.getLogger(SearchEngine.class);
    private final List<? extends BaseEntity> entitiesToSearchIn;
    private final List<SearchQuery<TYPE>> searchQueries;
    private final OnSearchEventListener<ENTITY> listener;
    public SearchEngine(List<ENTITY> entitiesToSearchIn, List<SearchQuery<TYPE>> searchQueries,
                        OnSearchEventListener<ENTITY> listener){
        this.entitiesToSearchIn = entitiesToSearchIn;
        this.searchQueries = searchQueries;
        this.listener = listener;

    }
    public void search(){
        if (!searchQueries.stream().allMatch(searchQuery ->{
            String zeroClassName = searchQueries.get(0).getType().getClass().getSimpleName();
            String currentQueryClassname = searchQuery.getType().getClass().getSimpleName();
            return zeroClassName.equals(currentQueryClassname);
        })){
            log.error("Somehow different enum classes being listed together");
            return;
        }


        try {
            switch (searchQueries.get(0).getType()) {
                case SearchTypes.VISITOR_TYPE visitorType -> {
                    try {
                        List<GlobalVisitor> visitors = (List<GlobalVisitor>) entitiesToSearchIn;
                        searchQueries.forEach(searchQuery -> {
                            Object value = searchQuery.getValue();
                            SearchTypes.VISITOR_TYPE typeEnum = (SearchTypes.VISITOR_TYPE) searchQuery.getType();
                            switch (typeEnum) {
                                case BY_AGE -> visitors.removeIf(visitor -> {
                                    try {
                                        if ( ((String) value).isEmpty()){
                                            return false;
                                        }
                                        int age = Integer.parseInt((String) value);
                                        return visitor.getAge() != age;

                                    } catch (ClassCastException ex) {
                                        log.error(ex);
                                        return false;
                                    }

                                });

                                case BY_DATE -> visitors.removeIf(visitor -> {
                                    String date = DateUtils.getStringFromDate(visitor.getBirthDate());
                                    return compareStrings(date, value);
                                });

                                case BY_NAME -> visitors.removeIf(visitor ->
                                        compareStrings(visitor.getName(), value));
                                case BY_GROUP -> visitors.removeIf(visitor ->
                                        compareStrings(visitor.getGroup(), value));

                                case BY_NOTES -> visitors.removeIf(visitor ->
                                        compareStrings(visitor.getNotes(), value));

                                case BY_SPECIALITY -> visitors.removeIf(visitor ->
                                        compareStrings(visitor.getSpeciality(), value));


                                case BY_MASTER_NAME -> visitors.removeIf(visitor ->
                                        compareStrings(visitor.getLinkedMasterName(), value));
                                default -> throw new IllegalStateException("Unexpected value: " + typeEnum);
                            }
                            log.info("searched visitors size {}", visitors.size());

                        });
                        listener.onSearchEvent((SearchEvent<ENTITY>) new SearchEvent<>(visitors));
                    } catch (ClassCastException ex) {
                        log.error("error casting list to one of list entities: ", ex);
                    }

                }
                case SearchTypes.SCHEDULE_TYPE scheduleType -> {

                }
                default ->
                        throw new IncompatibleSearchTypeException("Unexpected searchType: " + searchQueries.get(0).getType());
            }
        }catch (IndexOutOfBoundsException ex){
            Notify.error("Хотя бы 1 поле должно быть заполнено для поиска");
            log.error(ex);
        }
    }
    private boolean compareStrings(String s1, Object object){
        if (object instanceof String s) {
            log.info("comparing {} to {}",s1,s);
            if (s.isEmpty() || s.isBlank()){
                return false;
            }
            return !s1.contains(s);

        }else if(object instanceof Date d){
            String comparableDate = DateUtils.getStringFromDate(d);
            return !s1.contains(comparableDate);
        }
        return false;
    }
}
