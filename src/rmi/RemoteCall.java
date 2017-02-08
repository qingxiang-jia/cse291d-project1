package rmi;

import java.io.Serializable;
import java.lang.reflect.Method;

public class RemoteCall implements Serializable {
  private Method method;
  private Object[] args;

  public RemoteCall(Method method, Object[] args) {
    this.method = method;
    this.args = args;
  }

  public Method getMethod() {
    return method;
  }

  public Object[] getArgs() {
    return args;
  }
}
