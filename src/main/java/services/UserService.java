package services;

import Util.DbManagement;
import dev.morphia.query.filters.Filters;
import entities.User;

import java.util.List;

public class UserService extends DbManagement<User> {

  public UserService() {
    super(User.class);
  }

  public void create(String username, String email, String name, String password, boolean isAdmin, boolean isActive) {
    User newUser = isActive ? new User(username, email, name, password, isAdmin, true) : new User(username, email, name, password, isAdmin);
    createDb(newUser);
  }

  public void update(User user) {
    updateDb(user);
  }

  public void delete(String id) {
    deleteDbById(id);
  }

  public User find(String id) {
    return findDbByID(id);
  }

  public List<User> findAll() {
    return findAllDb();
  }

  public User findByUsername(String username) {
    return findDb().filter(Filters.eq("username", username)).first();
  }
}
