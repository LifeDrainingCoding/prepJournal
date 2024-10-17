package com.lifedrained.prepjournal.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.lifedrained.prepjournal.consts.RoleConsts.*;

public class NameProcessor {

   private static final Logger log = LogManager.getLogger(NameProcessor.class);

   public static String convertRoleToReadable(String role) {
      if(role.equals(ADMIN.value)|| role.equals(ADMIN1.value)){
         return "Администратор";
      }else if (role.equals(USER1.value)||role.equals(USER.value)){
         return "Сотрудник";
      }else if(role.equals("права")){
         return "права";
      }

      throw new IllegalArgumentException("Entered role that doesn't exists: "+role);
   }
   public static String getStringFromDate (Date date){
      return new SimpleDateFormat("dd.MM.yyyy").format(date);
   }
   public static Date getDateFromString(String stringDate){
      try {
        return new SimpleDateFormat("dd.MM.yyyy").parse(stringDate);
      }catch (ParseException ex){
         log.error("Error during parsing date  ", ex);
         return new Date();
      }
   }
}
