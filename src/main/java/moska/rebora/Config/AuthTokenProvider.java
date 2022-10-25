package moska.rebora.Config;

import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.Map;

public interface AuthTokenProvider<T> {
    T createAuthToken(String id, String role, Map<String, String> claims, Date expireDate);
    T convertAuthToken(String token);
    Authentication getAuthentication(T authToken);
}
