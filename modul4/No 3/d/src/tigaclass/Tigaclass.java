package tigaclass;

/**
 *
 * @author Richie
 */
public class Tigaclass {
    public String inipublic;
    protected String iniprotected;
    private String iniprivate;
    
    public Tigaclass(String A, String B, String C){
        this.inipublic = A;
        this.iniprotected = B;
        this.iniprivate = C;
    }
  public String inipublic (){
      return inipublic;
  }
  protected String iniprotected (){
      return iniprotected;
  }
  String iniprivate (){
      return iniprivate;
  }
    }
    
