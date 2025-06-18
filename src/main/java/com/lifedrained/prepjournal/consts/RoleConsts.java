package com.lifedrained.prepjournal.consts;

public enum RoleConsts {
       USER_TIER1( "Преподаватель")
    , USER_TIER2( "Секретарь"), USER_TIER3("Заведующий"),
    ADMIN ( "Администратор");
      public final String value;
      public final String translation;
     RoleConsts( String translation) {
         this.value = this.name();
         this.translation = translation;
     }


    @Override
    public String toString() {
        return value;
    }
}
