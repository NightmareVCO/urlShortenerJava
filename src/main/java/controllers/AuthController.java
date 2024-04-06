package controllers;

import Util.BaseController;
import io.javalin.Javalin;
import io.javalin.http.Context;
import services.AuthService;
import services.UserService;

import static io.javalin.apibuilder.ApiBuilder.*;

public class AuthController extends BaseController {
  private final UserService userService;
  private final AuthService authService;

  public AuthController(Javalin App, UserService userService, AuthService authService) {
    super(app);
    this.userService = userService;
    this.authService = authService;
  }

  public void protect(Context ctx){
    // TODO: Si el usuario está logueado, redirigir a la página principal
  }

  public void loginGet(Context ctx){
    // TODO: Renderizar la página de login

  }

  public void loginPost(Context ctx){
    // TODO: Verificar las credenciales del usuario y redirigir a la página principal

  }

  public void signupGet(Context ctx){
  // TODO: Renderizar la página de registro

  }

  public void sigupPost(Context ctx){
  // TODO: Crear un nuevo usuario (verificar si existe) y redirigir a la página principal

  }

  public void logout(Context ctx){
  // TODO: Cerrar la sesión del usuario y redirigir a la página principal.

  }

  @Override
  public void applyRoutes() {
    app.routes(() -> {
      before("/auth/*", this::protect);
      get("/auth/login", this::loginGet);
      post("/auth/login", this::loginPost);
      get("/auth/singup", this::signupGet) ;
      post("/auth/singup", this::sigupPost);
      get("/auth/logout", this::logout);
    });
  }
}
