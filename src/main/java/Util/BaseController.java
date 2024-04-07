package Util;

import io.javalin.Javalin;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {
  protected static Javalin app;
  public BaseController(Javalin app){
    this.app = app;
  }
  abstract public void applyRoutes();

  public Map<String, Object> setModel(Object... args){
    Map<String, Object> model = new HashMap<>();
    if (args.length % 2 != 0) {
      throw new IllegalArgumentException("Invalid number of arguments. Arguments should be in pairs (key, value).");
    }
    for (int i = 0; i < args.length; i += 2) {
      if (!(args[i] instanceof String)) {
        throw new IllegalArgumentException("Argument at position " + i + " is not a String. Keys should be Strings.");
      }
      model.put((String) args[i], args[i + 1]);
    }
    return model;
  }
}
