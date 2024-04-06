package entities;

import java.util.Date;
import java.util.List;

public class Url {
  private String id;
  private String url;
  private String originalUrl;
  private Date date;
  private List<Statistic> statistics;

  public Url(String id, String url, String originalUrl, Date date, List<Statistic> statistics) {
    this.id = id;
    this.url = url;
    this.originalUrl = originalUrl;
    this.date = date;
    this.statistics = statistics;
  }


}