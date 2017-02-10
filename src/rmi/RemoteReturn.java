package rmi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RemoteReturn implements Serializable{
  private List<Object> returnValue;
  private Class exceptionType;
  private Boolean hasException;
  private Throwable exception;

  RemoteReturn(Object returnValue) {
    this.returnValue = new ArrayList<>();
    this.returnValue.add(returnValue);
    hasException = Boolean.FALSE;
  }

  public Object getReturnValue() {
    return returnValue.get(0);
  }

  public Class getExceptionType() {
    return exceptionType;
  }

  public void setExceptionType(Class exceptionType) {
    this.exceptionType = exceptionType;
  }

  public Boolean getHasException() {
    return hasException;
  }

  public void setHasException(Boolean hasException) {
    this.hasException = hasException;
  }

  public Throwable getException() {
    return exception;
  }

  public void setException(Throwable exception) {
    this.exception = exception;
  }
}
