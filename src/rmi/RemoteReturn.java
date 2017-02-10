package rmi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RemoteReturn implements Serializable{
  private List<Object> returnValue;

  RemoteReturn(Object returnValue) {
    this.returnValue = new ArrayList<>();
    this.returnValue.add(returnValue);
  }

  public Object getReturnValue() {
    return returnValue.get(0);
  }
}
