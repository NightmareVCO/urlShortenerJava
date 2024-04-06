package entities;

public class User {

  private String username;
  private String email;
  private String name;
  private String password;
  private boolean admin;
  private boolean active;

  public User(String username, String email, String name, String password, boolean admin, boolean active) {
    this.username = username;
    this.email = email;
    this.name = name;
    this.password = password;
    this.admin = admin;
    this.active = active;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
