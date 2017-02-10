package rmi;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoteCall implements Serializable {
  private String methodName;
  private List<Class> paraTypes;
  private List<Object> args;

  public RemoteCall(Method method, Object[] args) {
    methodName = method.getName();
    paraTypes = new ArrayList<Class>(Arrays.asList(method.getParameterTypes()));
    if (args == null) {
      this.args = new ArrayList<>(0);
    } else {
      this.args = new ArrayList<>(Arrays.asList(args));
    }
  }
  
  public String getMethodName(){
    return methodName;
  }
  
  public List<Class> getParaTypes(){
    return paraTypes;
  }

  public List<Object> getArgs() {
    return args;
  }
  
  public static void main(String[] args) {
    boolean flag = true;
    Method me = null;
  }
}
