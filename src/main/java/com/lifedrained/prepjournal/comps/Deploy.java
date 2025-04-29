package com.lifedrained.prepjournal.comps;

import com.lifedrained.prepjournal.repo.entities.SubsEntity;
import com.lifedrained.prepjournal.services.SubsService;
import io.github.natanimn.BotClient;
import io.github.natanimn.BotContext;
import io.github.natanimn.errors.ConnectionError;
import io.github.natanimn.handlers.CallbackHandler;
import io.github.natanimn.types.*;
import jakarta.transaction.Transactional;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Component
public class Deploy {
    private static final Logger log = LogManager.getLogger(Deploy.class);
    private Environment env;
    private  BotClient client;
    private String url= "no url";
    private SubsService subsService;
    private LinkedHashSet<SubsEntity> subs;

    //ssh -R 80:localhost:8855 ssh.localhost.run

    private static final String key = "tunneled with tls termination,"
            , tokenPath = "static/API_Token" ;
    public Deploy(Environment env, SubsService subsService) {
        this.env = env;
        this.subsService = subsService;
        subs = subsService.getSubs();
    }


    @EventListener(ApplicationReadyEvent.class)
    protected void  initDeploy(){

        String port = env.getProperty("server.port");
        log.info("port: {}", port);
        if (port == null) {
            log.error("null port");
            return;
        }
        String command = "ssh";
        String[] args = new String[]{"-t", "-o" ,"StrictHostKeyChecking=no","-R" ,"80:localhost:"+port , "ssh.localhost.run",};

        List<String> exec = new ArrayList<>(Arrays.stream(args).toList());
        exec.addFirst(command);
        ProcessBuilder pb = new ProcessBuilder(exec );
        pb.redirectErrorStream(true );
        long startTime = System.currentTimeMillis();
        System.out.println(System.getenv("PATH"));
        pb.environment().put("PATH", System.getenv("PATH"));
        try{
           Process process =  pb.start();
            InputStream in = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            setupBot();
            while(( line =  reader.readLine()) != null){
                if (line.contains(key)){
                    url = line.substring(line.indexOf(key)+key.length()).trim();
                    log.warn("founded url " + line);
                    sendURLAsync();
                    sendNotsToSubAsync();
                    break;
                }
                if (System.currentTimeMillis()- startTime > Duration.ofMinutes(1).toMillis()){
                    log.error("timeout error. cannot connect to localhost.run");
                    break;
                }
            }

            new Thread(() -> {
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(in));
                String s;
                try {
                    while ((s = reader1.readLine()) != null) {
                        if (s.contains(key)){
                            url = s.substring(s.indexOf(key)+key.length()).trim();
                            log.warn("updated url {}" , s);
                            sendURL();
                            sendNotsToSub();
                        }
                    }
                } catch (  IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();


        }catch (IOException e){
            log.error(e);
        }

    }

    private void sendURL(){
        ClassPathResource resource = new ClassPathResource("static/OwnerTG_ID");
        try {
            InputStream in = resource.getInputStream();

            String tgId = new String(IOUtils.readFully(in, in.available()),StandardCharsets.UTF_8);
            client.context.sendMessage(tgId, url)
                    .replyMarkup(new InlineKeyboardMarkup(
                            new InlineKeyboardButton[]{
                                    new InlineKeyboardButton("Получить ссылку")
                                            .callbackData("get"),
                                    new InlineKeyboardButton("Подписаться на рассылку ссылок").callbackData("sub"),
                                    new InlineKeyboardButton("Отписаться от рассылки ссылок").callbackData("unsub")

                            })).exec();
        }catch (IOException ex){
            log.error(ex);
        }
        log.info("message sent");
    }

    private void sendURL(String tgId){
        client.context.sendMessage(tgId, url)
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton[]{
                                new InlineKeyboardButton("Получить ссылку")
                                        .callbackData("get"),
                                new InlineKeyboardButton("Подписаться на рассылку ссылок").callbackData("sub"),
                                new InlineKeyboardButton("Отписаться от рассылки ссылок").callbackData("unsub")

                        })).exec();
    }
    private void sendURLAsync(){
        new Thread(this::sendURL).start();
    }


    private void finishBot(){
        client.stop();
        client.deleteWebhook();
        client = null;
    }
    private void setupBot(){
        ClassPathResource resource = new ClassPathResource(tokenPath);
        try {
            InputStream in = resource.getInputStream();


            String token = new String(IOUtils.readFully(in,in.available()),StandardCharsets.UTF_8);

            client = new BotClient(token);
            setListener();



        }catch (IOException e){
            log.error(e);
        }

    }
    private void setListener(){

        new Thread(() -> {
            client.onMessage(filter -> filter.commands("start", "get"),((context, message) -> {
                log.info("message got");
                sendMsgContext(context);

            }));

            client.onMessage(filter -> filter.commands("sub"),(context, message) -> {
                subUser(message.from.id);
                context.reply("Подписка оформлена").exec();
            });

            client.onMessage(filter -> filter.commands("unsub"),(context, message) -> {
                unsubUser(message.from.id);
                context.reply("Подписка отменена").exec();
            });
            client.onCallback((context, callbackQuery) -> {
                switch (callbackQuery.data){
                    case "get" ->sendMsgContext(context);
                    case "sub" ->{
                        subUser(callbackQuery.from.id) ;
                        context.reply("Подписка оформлена").exec();
                    }
                    case "unsub" ->{
                        unsubUser(callbackQuery.from.id);
                        context.reply("Подписка отменена").exec();
                    }
                }
            });
            try {
                client.run();
            }catch (ConnectionError ex){
                setListener();
            }
        }).start();


    }
    private void sendMsgContext(BotContext context){
        context.sendMessage( url)
                .replyMarkup(new InlineKeyboardMarkup(
                        new InlineKeyboardButton[][]{
                                new InlineKeyboardButton[]{
                                        new InlineKeyboardButton("Получить ссылку")
                                                .callbackData("get")
                                },
                                new InlineKeyboardButton[]{
                                        new InlineKeyboardButton("Подписаться на рассылку ссылок")
                                                .callbackData("sub")
                                },
                                new InlineKeyboardButton[]{
                                        new InlineKeyboardButton("Отписаться от рассылки ссылок")
                                                .callbackData("unsub")
                                }
                        })).exec();
    }

    protected void subUser(long userId){
        SubsEntity subsEntity = new SubsEntity(userId);
        subsService.uploadSub(subsEntity);
    }

    protected void unsubUser(long userId){
        subsService.deleteSub(userId);
    }

    protected void sendNotsToSub(){
        subs = subsService.getSubs();
        subs.forEach(subsEntity -> {
            sendURL(String.valueOf(subsEntity.userId));
        });

    }
    private void sendNotsToSubAsync(){
        new Thread(this::sendNotsToSub).start();
    }
}
