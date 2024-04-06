package services;

import Util.DbManagement;
import dev.morphia.query.filters.Filters;
import entities.Url;
import entities.User;

import java.util.Date;
import java.util.List;

public class UrlService extends DbManagement<Url> {

  public UrlService() {
    super(Url.class);
  }

  public void create(String url, String originalUrl, Date date, User user) {
    Url newUrl = user != null ? new Url(url, originalUrl, date, user) : new Url(url, originalUrl, date);
    createDb(newUrl);
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

}
