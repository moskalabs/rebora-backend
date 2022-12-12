package moska.rebora.User.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthorizationNaverDto {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String error;
    private String error_description;

    @Builder
    public AuthorizationNaverDto(String access_token, String token_type, String refresh_token, String expires_in, String error, String error_description) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
        this.error = error;
        this.error_description = error_description;
    }
}
