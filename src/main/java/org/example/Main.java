package org.example;

import controllers.AuthController;
import controllers.UrlController;
import grpc.UrlServiceGrpc;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.jasypt.util.text.BasicTextEncryptor;
import services.AuthService;
import services.StatisticService;
import services.UrlService;
import services.UserService;
import controllers.UserController;

import io.grpc.Server;
import io.grpc.ServerBuilder;


import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread javalinThread = new Thread(() -> {
            Javalin app = Javalin.create(config -> {
                config.staticFiles.add(staticFileConfig -> {
                    staticFileConfig.hostedPath = "/";
                    staticFileConfig.directory = "/public";
                    staticFileConfig.location = Location.CLASSPATH;
                    staticFileConfig.precompress = false;
                    staticFileConfig.aliasCheck = null;
                });
                config.plugins.enableCors(corsContainer -> corsContainer.add(CorsPluginConfig::anyHost));
            }).start(3000);

            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();

            UserService userService = new UserService();
            AuthService authService = new AuthService(textEncryptor);
            UrlService urlService = new UrlService();
            StatisticService statisticService = new StatisticService();


            new UserController(app, userService).applyRoutes();
            new AuthController(app, userService, authService).applyRoutes();
            new UrlController(app, urlService, userService, statisticService).applyRoutes();
            //addInitialData(userService, urlService, statisticService, textEncryptor);

        });
        javalinThread.start();

        Server server = ServerBuilder
                .forPort(8080)
                .addService(new UrlServiceGrpc()).build();

        server.start();
        server.awaitTermination();
    }

    public static void addInitialData(UserService userService, UrlService urlService, StatisticService statisticService, BasicTextEncryptor textEncryptor) {
        userService.create("johndoe", "johndoe@example.com", "John Doe", textEncryptor.encrypt("gone"), true, true);
        urlService.create("https://www.example.com", "johndoe", Date.from(LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), userService.findByUsername("johndoe"), true, 100, null);
        urlService.create("https://www.example.com/2", "https://www.example.com/longerurl", Date.from(LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), userService.findByUsername("johndoe"), true, 20, null);
        urlService.create("https://www.example.com/3", "https://www.example.com/longerurl", Date.from(LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), userService.findByUsername("johndoe"), false, 40, null);
        statisticService.create("Safari", "Mac OS", "127.0.0.1");
        urlService.addStatistic(urlService.findByUrl("https://www.example.com"), statisticService.findByIpDirection("127.0.0.1"));
        urlService.addStatistic(urlService.findByUrl("https://www.example.com/2"), statisticService.findByIpDirection("127.0.0.1"));
        urlService.addStatistic(urlService.findByUrl("https://www.example.com/3"), statisticService.findByIpDirection("127.0.0.1"));
    }
}