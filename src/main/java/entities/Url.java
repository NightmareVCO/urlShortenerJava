package entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity("urls")
public class Url {
  @Id
  ObjectId id;
  private String url;
  private String originalUrl;
  private Date date;
  private Boolean status;
  private int clicks;
  @Reference
  private List<Statistic> statistics = new ArrayList<>();
  @Reference
  private User user;

  private String session;

  public Url() {
  }

  public Url(ObjectId id, String url, String originalUrl, Date date, List<Statistic> statistics, User user, Boolean status, int clicks) {
    this.id = id;
    this.url = url;
    this.originalUrl = originalUrl;
    this.date = date;
    this.statistics = statistics;
    this.user = user;
    this.status = status;
    this.clicks = clicks;
  }

  public Url(String url, String originalUrl, Date date, User user, Boolean status, int clicks) {
    this.url = url;
    this.originalUrl = originalUrl;
    this.date = date;
    this.user = user;
    this.status = status;
    this.clicks = clicks;
  }

  public Url(String url, String originalUrl, Date date, Boolean status, int clicks, String session) {
    this.url = url;
    this.originalUrl = originalUrl;
    this.date = date;
    this.status = status;
    this.clicks = clicks;
    this.session = session;
  }

  public Url(String url, String originalUrl, Date date, List<Statistic> statistics, User user, Boolean status, int clicks) {
    this.url = url;
    this.originalUrl = originalUrl;
    this.date = date;
    this.statistics = statistics;
    this.user = user;
    this.status = status;
    this.clicks = clicks;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public List<Statistic> getStatistics() {
    return statistics;
  }

  public void setStatistics(List<Statistic> statistics) {
    this.statistics = statistics;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public int getClicks() {
    return clicks;
  }

  public void setClicks(int clicks) {
    this.clicks = clicks;
  }

  public String getSession() {
    return session;
  }

  public void setSession(String session) {
    this.session = session;
  }
}