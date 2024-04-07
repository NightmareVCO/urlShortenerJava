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

  // Literal paths to the templates
  private final String INDEX_PATH = "/";
  private final String LOGIN_PATH = "/auth/login";
  private final String SIGNUP_PATH = "/auth/signup";
  private final String LOGIN_RENDER = "public/templates/login.html";
  private final String LOGGED_RENDER = "public/templates/logged.html";
  private final String SIGNUP_RENDER = "public/templates/signup.html";


  public AuthController(Javalin app, UserService userService, AuthService authService) {
    super(app);
    this.userService = userService;
    this.authService = authService;
  }

  public void protect(Context ctx){
    User user = ctx.sessionAttribute("user");
    if(user == null)
      return;

    ctx.redirect(INDEX_PATH);
  }

  public void loginGet(Context ctx){
    ctx.render(LOGIN_RENDER);
  }

  public void loginPost(Context ctx){
    String email = ctx.formParam("email");
    String password = ctx.formParam("password");
    System.out.println("Email: " + email + " Password: " + password);
    boolean remember = ctx.formParam("remember") != null;

    User user = userService.findByEmail(email);
    if(user == null){
      System.out.println("User not found");
      ctx.redirect(LOGIN_PATH);
      return;
    }
    if(!user.isActive()){
      System.out.println("User is not active");
      ctx.redirect(LOGIN_PATH);
      return;
    }

    String userPassword = user.getPassword();
    String decryptedPassword = authService.decryptText(userPassword);
    assert password != null;
    if(!password.equals(decryptedPassword)){
      System.out.println("Password is incorrect");
      ctx.redirect(LOGIN_PATH);
      return;
    }

    ctx.sessionAttribute("user", user);
    ctx.sessionAttribute("email", user.getEmail());
    if(remember){
      String encryptUser = authService.encryptText(email);
      ctx.cookie("user", encryptUser, 604800);
    }
    ctx.redirect(INDEX_PATH);
  }

  public void signupGet(Context ctx){
    ctx.render(SIGNUP_RENDER);
  }

  public void signupPost(Context ctx){
    String email = ctx.formParam("email");
    User user = userService.findByEmail(email);

    if(user!= null){
      ctx.redirect(SIGNUP_PATH);
      return;
    }

    String name = ctx.formParam("name");
    assert email != null; String username = email.split("@")[0];
    String password = ctx.formParam("password");
    String encryptedPassword = authService.encryptText(password);
    boolean admin = ctx.formParam("admin") != null;
    boolean active = ctx.formParam("active") != null;

    userService.create(username, email, name, encryptedPassword, admin, active);
    ctx.redirect(INDEX_PATH);
  }

  public void logout(Context ctx){
    ctx.req().getSession().invalidate();
    ctx.removeCookie("user");
    ctx.redirect(INDEX_PATH);

  }

  @Override
  public void applyRoutes() {
    app.routes(() -> path("/auth", () ->{
      before("/login", this::protect);
      before("/signup", this::protect);
      get("/login", this::loginGet);
      post("/login", this::loginPost);
      get("/signup", this::signupGet) ;
      post("/signup", this::signupPost);
      get("/logout", this::logout);
    }));
  }
}
