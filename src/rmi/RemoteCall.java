package rmi;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class RemoteCall implements Serializable {
  private Method method;
  private List<Object> args;

  public RemoteCall(Method method, Object[] args) {
    this.method = method;
    this.args = Arrays.asList(args);
  }

  public Method getMethod() {
    return method;
  }

  public List<Object> getArgs() {
    return args;
  }
  
  public static void main(String[] args) {
    boolean flag = true;
    Method me = null;
  }
}
