package controllers;

import Util.BaseController;
import entities.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import services.UserService;

import java.util.List;
import java.util.Map;


import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class UserController extends BaseController {

 private final UserService userService;

  public UserController(Javalin app, UserService userService) {
    super(app);
    this.userService = userService;
  }

  public void list(Context ctx) {
    String usuario_autoDelete = ctx.sessionAttribute("usuario_autoDelete");
    ctx.sessionAttribute("usuario_autoDelete", null);
    List<User> users = userService.findAll();

    Map<String, Object> modelo =  setModelo(
            "users", users,
            "usuario_autoDelete", usuario_autoDelete);

    ctx.render("/public/templates/html", modelo); //cambiar al archivo html que se va a renderizar

  }

  private void show(Context context) {
    String username = context.pathParam("username");
    User user = userService.find(username);

    if (user == null) {
      context.status(404);
      context.result("Usuario no encontrado");
      return;
    }

    Map<String, Object> modelo = setModelo(
            "user", user);

    context.render("/public/templates/html", modelo); //cambiar al archivo html que se va a renderizar

  }

  private void create(Context ctx) {
    String username = ctx.formParam("username");
    if (userService.find(username) != null) {
      ctx.status(400);
      ctx.result("El usuario ya existe");
      return;
    }

    String email = ctx.formParam("email");
    String name  = ctx.formParam("name");
    String password = ctx.formParam("password");
    boolean admin = ctx.formParam("admin") != null;
    boolean active = ctx.formParam("active") != null;

    userService.create(username, email, name, password, admin, active);
    ctx.redirect("/users/" + username);
  }


  private void update(Context ctx) {
  //Actualizar un usuario. Redirigir a la página de usuario

    String username = ctx.pathParam("username");
    String email = ctx.formParam("email");
    String name = ctx.formParam("name");
    String password = ctx.formParam("password");
    boolean admin = ctx.formParam("admin") != null;
    boolean active = ctx.formParam("active") != null;

    userService.update(username, email, name, password, admin, active);
    ctx.redirect("/users/" + username);
  }

  private void eliminar(Context ctx) {
    String username = ctx.pathParam("username");
    User usuarioLogueado = ctx.sessionAttribute("user");

    assert usuarioLogueado != null;
    if(usuarioLogueado.getUsername().equals(username)) {
      ctx.sessionAttribute("usuario_autoDelete", "No puedes eliminarte a ti mismo.");
      ctx.redirect("/users");
      return;
    }

    userService.desactivate(username);
     ctx.redirect("/users");
  }

    @Override
    public void applyRoutes() {
        app.routes(() -> {
            path("/users", () -> {
                get(this::list);
                get("/{username}", this::show);
                post(this::create);
                put("/{username}", this::update);
                delete("/{username}", this::eliminar);
            });
        });
    }
}

