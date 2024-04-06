package entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
@Entity("urls")
public class Url {
  @Id
  ObjectId id;
  private String url;
  private String originalUrl;
  private Date date;
  @Reference
  private List<Statistic> statistics;
  @Reference
  private User user;

  public Url() {
  }

  public Url(ObjectId id, String url, String originalUrl, Date date, List<Statistic> statistics, User user) {
    this.id = id;
    this.url = url;
    this.originalUrl = originalUrl;
    this.date = date;
    this.statistics = statistics;
    this.user = user;
  }

  public Url(String url, String originalUrl, Date date, User user) {
    this.url = url;
    this.originalUrl = originalUrl;
    this.date = date;
    this.user = user;
  }

  public Url(String url, String originalUrl, Date date) {
    this.url = url;
    this.originalUrl = originalUrl;
    this.date = date;
  }

  public Url(String url, String originalUrl, Date date, List<Statistic> statistics, User user) {
    this.url = url;
    this.originalUrl = originalUrl;
    this.date = date;
    this.statistics = statistics;
    this.user = user;
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
}