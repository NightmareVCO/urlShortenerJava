package entities;

public class Statistic {
  private int clicks;
  private String browser;
  private String operatingSystem;
  private String ipDirection;

  public Statistic(int clicks, String browser, String operatingSystem, String ipDirection) {
    this.clicks = clicks;
    this.browser = browser;
    this.operatingSystem = operatingSystem;
    this.ipDirection = ipDirection;
  }

  public int getClicks() {
    return clicks;
  }

  public void setClicks(int clicks) {
    this.clicks = clicks;
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
