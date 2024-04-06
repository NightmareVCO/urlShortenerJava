package controllers;

import Util.BaseController;
import io.javalin.Javalin;
import io.javalin.http.Context;
import services.UserService;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UserController extends BaseController {

  private final UserService userService;

  public UserController(Javalin app, UserService userService) {
    super(app);
    this.userService = userService;
  }

  public void list(Context ctx) {
  // TODO: Listar todos los usuarios
  }

  private void show(Context context) {
  // TODO?: Mostrar un usuario
  }

  private void create(Context context) {
  // TODO: Crear un usuario
  }

  private void update(Context context) {
  // TODO: Actualizar un usuario. Redirigir a la página de usuario?
  }

  private void delete(Context context) {
  // TODO: Eliminar un usuario
  }

  @Override
  public void applyRoutes() {
    app.routes(() -> path("/users", () -> {
      get("/", this::list);
      get("/{username}", this::show);
      post("/", this::create);
      post("/{username}", this::update);
      post("/{username}/delete", this::delete);
    }));
  }
}
