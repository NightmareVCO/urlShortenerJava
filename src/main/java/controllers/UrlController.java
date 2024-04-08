package controllers;

import Util.BaseController;
import entities.Url;
import entities.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import services.StatisticService;
import services.UrlService;
import services.UserService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ua_parser.Parser;
import ua_parser.Client;

public class UrlController extends BaseController {

  private final UrlService urlService;
  private final UserService userService;
  private final StatisticService statisticService;

  private final String INDEX_PATH = "/";
  private final String DOMAIN = "http://localhost:3000";
  private final String INDEX_RENDER = "public/templates/index.html";
  private final String LOGGED_PATH = "/logged";
  private final String LOGGED_RENDER = "public/templates/logged.html";
  private final String DASHBOARD_PATH = "/dashboard";
  private final String DASHBOARD_RENDER = "public/templates/dashboard.html";
  private final String ANALYTICS_PATH = "/analytics";
  private final String ANALYTICS_RENDER = "public/templates/analytics.html";
  private final String LINKS_PATH = "/links";
  private final String LINKS_RENDER = "public/templates/links.html";


public UrlController(Javalin app, UrlService urlService, UserService userService, StatisticService statisticService) {
  super(app);
  this.urlService = urlService;
  this.userService = userService;
  this.statisticService = statisticService;
}

public void indexGet(Context ctx) {
  String notLoggedUser = ctx.sessionAttribute("notLoggedUser");
  if (notLoggedUser != null) {
    List<Url> notLoggedUrls = urlService.findByNotLoggedUser(notLoggedUser);
    Map<String, Object> model = setModel("urls", notLoggedUrls);
    ctx.render(INDEX_RENDER, model);
    return;
  }

  ctx.redirect(LOGGED_PATH);
}

public void loggedGet(Context ctx) {
  User user = ctx.sessionAttribute("user");
  List<Url> allUserUrls = urlService.findByUser(user);
  Map<String, Object> model = setModel("urls", allUserUrls);
  ctx.render(LOGGED_RENDER, model);
}

public void createUrl(Context ctx) {
  String longUrl = ctx.formParam("url");
  String dashboard = ctx.formParam("dashboard");
  User user = ctx.sessionAttribute("user");
  String notLoggedUser = ctx.sessionAttribute("notLoggedUser");
  assert longUrl != null; String shortUrl = urlService.generateShortUrl(longUrl);

  urlService.create(shortUrl, longUrl, new Date(), user, true, 0, notLoggedUser);

  if (dashboard != null) {
    ctx.redirect(DASHBOARD_PATH);
    return;
  }

  ctx.redirect(INDEX_PATH);
}

public void redirectUrl(Context ctx) {
  String shortUrl = ctx.path();
  Url url = urlService.findByUrl(DOMAIN + shortUrl);
  if (url == null) {
    ctx.redirect(INDEX_PATH);
    return;
  }

  if (!url.getStatus()) {
    ctx.redirect(INDEX_PATH);
    return;
  }

  url.setClicks(url.getClicks() + 1);
  String userAgent = ctx.header("User-Agent");
  Parser parser = new Parser();
  Client client = parser.parse(userAgent);

  statisticService.create(client.userAgent.family, client.os.family, ctx.ip());
  urlService.addStatistic(url, statisticService.findByIpDirection(ctx.ip()));
  urlService.update(url);
  ctx.redirect(url.getOriginalUrl());
}

public void temporalSession(Context ctx) {
  if (ctx.sessionAttribute("notLoggedUser") != null || ctx.sessionAttribute("user") != null){
    return;
  }
  ctx.sessionAttribute("notLoggedUser", generateRandomString());
}

public void protect(Context ctx) {
  if (ctx.sessionAttribute("user") == null) {
    ctx.redirect(INDEX_PATH);
  }
}

public void protectAdmin(Context ctx) {
  User user = ctx.sessionAttribute("user");
  if (user == null || !user.isAdmin()) {
    ctx.redirect(INDEX_PATH);
  }
}

public void dashboardGet(Context ctx) {
  User user = ctx.sessionAttribute("user");
  List<Url> allUserUrls = urlService.findByUser(user);
  Map<String, Object> model = setModel("urls", allUserUrls);
  ctx.render(DASHBOARD_RENDER, model);
}

public void analyticsGet(Context ctx) {
  User user = ctx.sessionAttribute("user");
  List<Url> allUserUrls = urlService.findByUser(user);
  Map<String, Object> model = setModel("urls", allUserUrls);
  ctx.render(ANALYTICS_RENDER, model);
}

public void analyticsSender(Context ctx) {
  User user = userService.findByEmail(ctx.pathParam("email"));
  if (user == null) {
    ctx.result("User not found");
    return;
  }
  List<Url> allUserUrls = urlService.findByUser(user);
  ctx.json(urlService.getAnalytics(allUserUrls));
}

public void getAllLinks(Context ctx) {
  List<Url> allUrls = urlService.findAll();
  Map<String, Object> model = setModel("urls", allUrls);
  ctx.render(LINKS_RENDER, model);
}

public void alterUrl(Context ctx) {
  String id = ctx.pathParam("id");
  Url url = urlService.findDbByID(id);
  url.setStatus(!url.getStatus());
  urlService.update(url);
  ctx.redirect(LINKS_PATH);
}

public String generateRandomString() {
    int length = 10;
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder randomString = new StringBuilder();
    Random random = new Random();

    for (int i = 0; i < length; i++) {
      int index = random.nextInt(characters.length());
      char randomChar = characters.charAt(index);
      randomString.append(randomChar);
    }

    return randomString.toString();
  }

  @Override
  public void applyRoutes() {
    app.routes(() ->{
      app.before("/", this::temporalSession);
      app.get("/", this::indexGet);
      app.get("/logged", this::loggedGet);
      app.before("/dashboard", this::protect);
      app.get("/dashboard", this::dashboardGet);
      app.before("/analytics", this::protect);
      app.get("/analytics", this::analyticsGet);
      app.get("/analyticsSender/{email}", this::analyticsSender);
      app.post("/url", this::createUrl);
      app.before("/links", this::protectAdmin);
      app.get("/links", this::getAllLinks);
      app.get("/url/{shortUrl}", this::redirectUrl);
      app.before("/alter/{shortUrl}", this::protectAdmin);
      app.get("/alter/{id}", this::alterUrl);
    });
  }
}
