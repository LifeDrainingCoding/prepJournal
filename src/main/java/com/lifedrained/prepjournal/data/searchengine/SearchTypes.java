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
       BY_NAME("По имени:"), BY_BIRTH_DATE("По дате рождения:"), BY_AGE("По возрасту:"),
       BY_ENROLL("По номеру приказа"),
       BY_GROUP("По группе:"), BY_PASSPORT("По паспорту:"),
       BY_INN("По ИНН:"), BY_SNILS("По снилсу:"), BY_LOCAL_ID("По локальному номеру:"),
       BY_ENROLL_DATE("По дате зачисления:"), BY_COURSE("По номеру курса:");

       public final String translation;

       VISITOR_TYPE(String translation){
           this.translation = translation;
       }


       @Override
       public String toString() {
           return translation;
       }
   }


}
