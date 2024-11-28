package com.lifedrained.prepjournal.data.searchengine;

public interface SearchTypes {
   enum SCHEDULE_TYPE {
       BY_DATE(1), BY_DATE_BETWEEN(2), BY_MASTER_NAME(3),
       BY_DURATION(4), BY_DURATION_BETWEEN(5);

       public final int value;
       public final String string;

       SCHEDULE_TYPE(int value){
           this.value = value;
           switch (value){
               case 1: string = "По дате:"; break;
               case 2: string = "По диапазону дат:"; break;
               case 3: string = "По Фамилии/Имени/Отчеству/ФИО:"; break;
               case 4: string = "По длительности занятия:"; break;
               case 5: string = "По диапазону длительностей занятий:"; break;
               default: string = null;
           }
       }

   }

   enum VISITOR_TYPE{
       BY_NAME(1), BY_DATE(2), BY_AGE(3), BY_MASTER_NAME(4),
       BY_SPECIALITY(5), BY_GROUP(6), BY_VISITS_NUM(7), BY_NOTES(8);

       public final int value;
       public final String string;

       VISITOR_TYPE(int value){
           this.value = value;
           switch (value){
               case 1 -> string = "По имени:";
               case 2 -> string = "По дате рождения:";
               case 3 -> string = "По возрасту:";
               case 4 -> string = "По Фамилии/Имени/Отчеству/ФИО Педагога:";
               case 5 -> string = "По направлению:";
               case 6 -> string = "По группе:";
               case 7 -> string = "По кол-ву посещений в этом учебном году:";
               case 8 -> string = "По примечанию:";
               default -> string = null;
           }
       }


       @Override
       public String toString() {
           return string;
       }
   }


}
