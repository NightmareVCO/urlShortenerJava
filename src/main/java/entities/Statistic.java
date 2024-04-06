package entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity("statistics")
public class Statistic {
  @Id
  ObjectId id;
  private String browser;
  private String operatingSystem;
  private String ipDirection;

  public Statistic() {
  }

  public Statistic(ObjectId id, String browser, String operatingSystem, String ipDirection) {
    this.id = id;
    this.browser = browser;
    this.operatingSystem = operatingSystem;
    this.ipDirection = ipDirection;
  }

  public Statistic(String browser, String operatingSystem, String ipDirection) {
    this.browser = browser;
    this.operatingSystem = operatingSystem;
    this.ipDirection = ipDirection;
  }

  public String getBrowser() {
    return browser;
  }

  public void setBrowser(String browser) {
    this.browser = browser;
  }

  public String getOperatingSystem() {
    return operatingSystem;
  }

  public void setOperatingSystem(String operatingSystem) {
    this.operatingSystem = operatingSystem;
  }

  public String getIpDirection() {
    return ipDirection;
  }

  public void setIpDirection(String ipDirection) {
    this.ipDirection = ipDirection;
  }
}
