package Util;

public enum StaticFiles {

  USER("USER"),
  URL_MONGO("URL_MONGO"),
  DB_NAME("DB_NAME"),;

  private final String value;

  StaticFiles(String value){
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
