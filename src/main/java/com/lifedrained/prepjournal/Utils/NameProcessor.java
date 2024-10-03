package com.lifedrained.prepjournal.Utils;
import static com.lifedrained.prepjournal.consts.RoleConsts.*;

public class NameProcessor {

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
}
