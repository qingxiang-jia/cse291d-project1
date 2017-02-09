package rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;

public class MyInvocationHandler implements InvocationHandler {
  private Class stubClass;
  private InetSocketAddress skeletonAddress;
  
  
  public MyInvocationHandler(Class c, InetSocketAddress address) {
    stubClass = c;
    skeletonAddress = address;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String methodName = method.getName();
    InvocationHandler handler = null;
    
    if (methodName.equals("equals") && args != null && args.length == 1) {
      if (args[0] == null || proxy == null || !Proxy.isProxyClass(args[0].getClass())
          || !((handler = Proxy.getInvocationHandler(args[0])) instanceof MyInvocationHandler)
          || !((MyInvocationHandler) handler).getStubClass().equals(this.stubClass)
          || !((MyInvocationHandler) handler).getSkeletonAddress().equals(this.skeletonAddress))
        return false;
      return true;
    } else if (methodName.equals("hashCode") && (args == null || args.length == 0)) {
      int code1 = (stubClass != null) ? stubClass.hashCode() * 17 : 0;
      int code2 = (skeletonAddress != null) ? skeletonAddress.hashCode() * 17 : 0;
      return code1 + code2;
    } else if (methodName.equals("toString") && (args == null || args.length == 0)) {
      StringBuilder sb = new StringBuilder();
      return sb.append(stubClass.getCanonicalName()).append('@')
          .append((skeletonAddress == null) ? "null" : skeletonAddress.toString()).toString();
    }
    if (!new HashSet<>(Arrays.asList(stubClass.getMethods())).contains(method)) {
      RMIException e = new RMIException("Unknown method is called.");
      System.out.println(e.getMessage());
    }
    Socket s = new Socket();
    RemoteReturn mesRet = null;
    try {
      s.connect(skeletonAddress);
      RemoteCall call = new RemoteCall(method, args);
      ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
      
      try {
        out.writeObject(call);
        out.flush();
      } catch(IOException e) {
        throw new RMIException(e.getMessage());
      }
      
      ObjectInputStream in = new ObjectInputStream(s.getInputStream());
      mesRet = (RemoteReturn) in.readObject();

      in.close();
      out.close();
      s.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return mesRet != null ? mesRet.getReturnValue() : null;
  }

  public InetSocketAddress getSkeletonAddress() {
    return skeletonAddress;
  }

  public Class getStubClass() {
    return stubClass;
  }

}
