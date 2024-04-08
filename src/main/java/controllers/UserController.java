package controllers;

import Util.BaseController;
import entities.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import services.UserService;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UserController extends BaseController {

 private final UserService userService;

  private final String INDEX_PATH = "/";
 private final String USER_PATH = "/users";
 private final String USER_RENDER = "public/templates/users.html";

  public UserController(Javalin app, UserService userService) {
    super(app);
    this.userService = userService;
  }

  public void list(Context ctx) {
    List<User> users = userService.findAll();
    Map<String, Object> model = setModel("users", users);

    ctx.render(USER_RENDER, model);
  }

  private void show(Context ctx) {
    String username = ctx.pathParam("username");
    User user = userService.findByUsername(username);

    if (user == null) {
      ctx.result("User not found");
      return;
    }

    Map<String, Object> model = setModel("user", user);
    ctx.render(USER_RENDER, model);
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

  public void protect(Context ctx) {
    if (ctx.sessionAttribute("user") == null) {
      ctx.redirect(INDEX_PATH);
    }
  }

  public void protectAdmin(Context ctx) {
    User user = ctx.sessionAttribute("user");
    if (user == null || !user.isAdmin()) {
      ctx.redirect(INDEX_PATH);
    }
  }

  private void alterStatus(Context ctx) {
    String username = ctx.pathParam("username");
    User user = userService.findByUsername(username);
    User loggedUser = ctx.sessionAttribute("user");

    assert loggedUser != null;
    if (Objects.equals(user.getUsername(), loggedUser.getUsername())) {
      ctx.redirect(USER_PATH);
      return;
   }

    user.setActive(!user.isActive());
    userService.update(user);
    ctx.redirect(USER_PATH);
  }

  private void alterRoles(Context ctx) {
    String username = ctx.pathParam("username");
    User user = userService.findByUsername(username);
    User loggedUser = ctx.sessionAttribute("user");

    assert loggedUser != null;
    if (Objects.equals(user.getUsername(), loggedUser.getUsername())) {
      ctx.redirect(USER_PATH);
      return;
    }
    user.setAdmin(!user.isAdmin());
    userService.update(user);
    ctx.redirect(USER_PATH);
  }

    @Override
    public void applyRoutes() {
        app.routes(() -> path("/users", () -> {
            before("/", this::protect);
            get("/", this::list);
            post("/", this::create);
            before("/userAlter", this::protectAdmin);
            get("/userAlter/{username}", this::alterStatus);
            before("/levelUserAlter", this::protectAdmin);
            get("/levelUserAlter/{username}", this::alterRoles);
            get("/{username}", this::show);
            post("/{username}", this::update);
            delete("/{username}", this::remove);
        }));
    }
}

