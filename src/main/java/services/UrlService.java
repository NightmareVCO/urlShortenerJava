package services;

import Util.DbManagement;
import dev.morphia.query.filters.Filters;
import entities.Statistic;
import entities.Url;
import entities.User;

import java.util.*;

public class UrlService extends DbManagement<Url> {

  private final String URL_DOMAIN = "http://localhost:3000/url/";

  public UrlService() {
    super(Url.class);
  }

  public void create(String url, String originalUrl, Date date, User user, boolean status, int clicks, String notLoggedUser) {
    Url newUrl = user != null ? new Url(url, originalUrl, date, user, status, clicks) : new Url(url, originalUrl, date, status, clicks, notLoggedUser);
    createDb(newUrl);
  }

  public void addStatistic(Url url, Statistic statistic) {
    url.getStatistics().add(statistic);
    updateDb(url);
  }

  public void update(Url url) {
    updateDb(url);
  }

  public void delete(String id) {
    deleteDbById(id);
  }

  public Url find(String id) {
    return findDbByID(id);
  }

  public List<Url> findAll() {
    return findAllDb();
  }

  public Url findByUrl(String url) {
    return findDb().filter(Filters.eq("url", url)).first();
  }

  public List<Url> findByUser(User user) {
    return findDb().filter(Filters.eq("user", user)).iterator().toList();
  }

  public List<Url> findByNotLoggedUser(String notLoggedUser) {
    return findDb().filter(Filters.eq("session", notLoggedUser)).iterator().toList();
  }

  public String generateShortUrl(String longUrl) {
    // I want from https://google.com to google or from https://www.google.com/ to google or from https://www.google.com/search?q=java to google
    String[] urlParts = longUrl.split("\\.");
    String url = urlParts[urlParts.length - 2];
    String[] urlParts2 = url.split("/");
    int random = (int) (Math.random() * 1000);
    return URL_DOMAIN + urlParts2[urlParts2.length - 1] + random;
  }


  public Map<Date, Integer> getAnalytics(List<Url> allUserUrls) {
    Map<Date, Integer> analytics = new HashMap<>();

    for (Url url : allUserUrls) {
      Date urlDate = url.getDate();
      int clicks = url.getClicks();
      analytics.put(urlDate, analytics.getOrDefault(urlDate, 0) + clicks);
    }

    return analytics;
  }
}
