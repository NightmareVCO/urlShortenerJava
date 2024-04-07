package controllers;

import Util.BaseController;
import entities.User;
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
    //Si el usuario está logueado, redirigir a la página principal
    User usuario = ctx.sessionAttribute("user");
    String path = ctx.path();

    if(usuario != null && !path.equals("/auth/login") && !path.equals("/auth/signup")){
      ctx.redirect("/public/templates/home.html");
    }
  }

  public void loginGet(Context ctx){

    ctx.render("/public/templates/login.html");


  }

  public void loginPost(Context ctx){
    //Verificar las credenciales del usuario y redirigir a la página principal
    String username = ctx.formParam("email");
    String password = ctx.formParam("password");
    boolean remember = ctx.formParam("remember") != null;
    User user = userService.findByUsername(username);

    if(user == null){
      ctx.sessionAttribute("error","Usuario no encontrado");
      ctx.redirect("/auth/login");
      return;
    }

    if(!user.isActive()){
      ctx.sessionAttribute("inactive_user", "Usuario inactivo, favor contactar al administrador.");
      ctx.redirect("/auth/login");
      return;
    }

    if(!userService.checkPassword(username, password) || user == null){
      ctx.sessionAttribute("error","Password incorrecto");
      ctx.redirect("/auth/login");
      return;
    }

    ctx.sessionAttribute("user", user);
    ctx.sessionAttribute("username", user.getUsername());
    if(remember){
      String usuarioEncryptado = authService.encryptText(username);
      ctx.cookie("user", usuarioEncryptado, 604800);
    }
    ctx.redirect("/");
  }

  public void signupGet(Context ctx){
  //Renderizar la página de registro
    ctx.render("/public/templates/signup.html");

  }

  public void signupPost(Context ctx){
    String username = ctx.formParam("username");
    if(userService.findByUsername(username) != null){
      System.out.println(userService.findByUsername(username));
      ctx.status(400);
      ctx.result("El usuario ya existe");
      return;
    }

    String email = ctx.formParam("email");
    String name = ctx.formParam("name");
    String password = ctx.formParam("password");
    boolean admin = ctx.formParam("admin") != null;
    boolean active = ctx.formParam("active") != null;

    userService.create(username,email, name, password, admin, active);
    ctx.redirect("/");
  }

  public void logout(Context ctx){
  //Cerrar la sesión del usuario y redirigir a la página principal.
    ctx.req().getSession().invalidate();
    ctx.removeCookie("user");
    ctx.redirect("/");

  }

  @Override
  public void applyRoutes() {
    app.routes(() -> {
      before("/auth/*", this::protect);
      get("/auth/login", this::loginGet);
      post("/auth/login", this::loginPost);
      get("/auth/singup", this::signupGet) ;
      post("/auth/singup", this::signupPost);
      get("/auth/logout", this::logout);
    });
  }
}
