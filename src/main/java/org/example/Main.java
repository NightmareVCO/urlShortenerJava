package org.example;

import controllers.AuthController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.jasypt.util.text.BasicTextEncryptor;
import services.AuthService;
import services.StatisticService;
import services.UrlService;
import services.UserService;
import controllers.UserController;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
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

//        Comentado porque ya están creados.
//        userService.create("johndoe", "johndoe@example.com", "John Doe", "gone", true, true);
//        urlService.create("https://www.example.com", "johndoe", new Date(), userService.findByUsername("johndoe"));
//        statisticService.create("Safari", "Mac OS", "127.0.0.1");

        app.get("/", ctx -> ctx.render("/public/templates/index.html"));
    }
}