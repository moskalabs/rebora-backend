package moska.rebora.Config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.ErrorCode;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "x-auth-token";

    @Autowired
    private JwtAuthTokenProvider jwtAuthTokenProvider;

    public JwtFilter(JwtAuthTokenProvider jwtAuthTokenProvider) {
        this.jwtAuthTokenProvider = jwtAuthTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = resolveToken(request);

        try {
            if (token.isPresent()) {
                JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
                if (jwtAuthToken.validate()) {
                    Authentication authentication = jwtAuthTokenProvider.getAuthentication(jwtAuthToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }else{
                setErrorResponse(HttpStatus.UNAUTHORIZED, response, ErrorCode.JWT_UNAUTHORIZED);
            }
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ErrorCode.JWT_UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> resolveToken(HttpServletRequest request) {
        String authToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authToken)) {
            return Optional.of(authToken);
        } else {
            return Optional.empty();
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, ErrorCode errorCode) {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(false);
        baseResponse.setErrorCode(errorCode.getStatus());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", false);
        jsonObject.put("errorCode", errorCode.getStatus());

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(response.getWriter(), jsonObject);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
