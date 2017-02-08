package rmi;

public class RemoteReturn {
  private Object returnValue;

  RemoteReturn(Object returnValue) {
    this.returnValue = returnValue;
  }

  public Object getReturnValue() {
    return returnValue;
  }
}
