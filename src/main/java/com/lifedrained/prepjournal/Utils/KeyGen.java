package com.lifedrained.prepjournal.Utils;



import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;

public class KeyGen {

    public static ArrayList<String> generateKeys(int amount){
        ArrayList<String> keys = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            keys.add(generateKey());
        }
        return keys;
    }
    public static String generateKey(){
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance("AES");
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        generator.init(256);
        SecretKey secretKey = generator.generateKey();
        String string = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println(string);
        if (string.contains("/")){
          string =  string.replaceAll("/", "!");
        }
        return  string;
    }
}
