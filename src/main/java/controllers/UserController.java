package controllers;

import Util.BaseController;
import entities.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import services.UserService;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UserController extends BaseController {

 private final UserService userService;

 private final String USER_PATH = "/users"; //cambiar a la ruta de los usuarios

  public UserController(Javalin app, UserService userService) {
    super(app);
    this.userService = userService;
  }

  public void list(Context ctx) {
    List<User> users = userService.findAll();
    Map<String, Object> model = setModel("users", users);

    ctx.render(USER_PATH, model);
  }

  private void show(Context ctx) {
    String username = ctx.pathParam("username");
    User user = userService.findByUsername(username);

    if (user == null) {
      ctx.result("User not found");
      return;
    }

    Map<String, Object> model = setModel("user", user);
    ctx.render("/public/templates/html", model);
  }

  private void create(Context ctx) {
    String email = ctx.formParam("email");
    User user = userService.findByEmail(email);

    if (user != null) {
      ctx.result("User already exists");
      return;
    }

    String name = ctx.formParam("name");
    assert email != null; String username = email.split("@")[0];
    String password = ctx.formParam("password");
    boolean admin = ctx.formParam("admin") != null;

    userService.create(username, email, name, password, admin, true);
    ctx.redirect(USER_PATH);
  }


  private void update(Context ctx) {
    String email = ctx.formParam("email");
    assert email != null; String username = email.split("@")[0];
    String name = ctx.formParam("name");
    String password = ctx.formParam("password");
    boolean admin = ctx.formParam("admin") != null;
    boolean active = ctx.formParam("active") != null;

    userService.update(username, email, name, password, admin, active);
    ctx.redirect(USER_PATH);
  }

  private void remove(Context ctx) {
    String username = ctx.pathParam("username");
    User loggedUser = ctx.sessionAttribute("user");

    assert loggedUser != null;
    if(loggedUser.getUsername().equals(username)) {
      ctx.result("You can't delete yourself");
      return;
    }

    userService.desactivate(username);
    ctx.redirect(USER_PATH);
  }

    @Override
    public void applyRoutes() {
        app.routes(() -> path("/users", () -> {
            get(this::list);
            post(this::create);
            get("/{username}", this::show);
            post("/{username}", this::update);
            delete("/{username}", this::remove);
        }));
    }
}

