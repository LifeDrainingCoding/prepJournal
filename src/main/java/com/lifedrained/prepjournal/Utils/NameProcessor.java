package com.lifedrained.prepjournal.Utils;
import com.lifedrained.prepjournal.consts.RoleConsts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class NameProcessor {

   private static final Logger log = LogManager.getLogger(NameProcessor.class);

   public static String convertRoleToReadable(String role) {

      List<RoleConsts> roles = List.of(RoleConsts.values());
      for (RoleConsts roleConsts : roles) {
         if (roleConsts.value.equals(role)) {
            return roleConsts.translation;
         }
      }

      throw new IllegalArgumentException("Entered role that doesn't exists: "+role);
   }

}
