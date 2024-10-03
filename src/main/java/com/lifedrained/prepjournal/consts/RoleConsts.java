package com.lifedrained.prepjournal.consts;

public enum RoleConsts {
      ADMIN ("ADMIN"),ADMIN1("ROLE_ADMIN") , USER("ROLE_USER") , USER1("USER");
      public final String value;
      public String translation;
     RoleConsts(String value){
         this.value = value;
         switch (value){
             case "ADMIN","ROLE_ADMIN": translation = "Администратор"; break;
             case "ROLE_USER","USER": translation = "Сотрудник";break;
             default:
         }
     }

}
