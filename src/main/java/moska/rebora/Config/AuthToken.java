package moska.rebora.Config;

public interface AuthToken<T>{
    String AUTHORITIES_KEY = "role";
    boolean validate();
    T getData();
}
