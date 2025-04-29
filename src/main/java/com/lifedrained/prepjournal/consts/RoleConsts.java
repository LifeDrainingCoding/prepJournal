package com.lifedrained.prepjournal.consts;

public enum RoleConsts {
      ADMIN ("ADMIN"),ADMIN1("ROLE_ADMIN") , USER1("ROLE_USER") , USER("USER");
      public final String value;
      public String translation;
     RoleConsts(String value){
         this.value = value;
         switch (value){
             case "ADMIN","ROLE_ADMIN": translation = "Администратор"; break;
             case "USER","ROLE_USER": translation = "Сотрудник";break;
             default:
         }
     }


    @Override
    public String toString() {
        return value;
    }
}
