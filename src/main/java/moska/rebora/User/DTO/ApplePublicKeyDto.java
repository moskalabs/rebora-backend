package moska.rebora.User.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.simple.JSONObject;

@Data
public class ApplePublicKeyDto {

    private String kty;
    private String kid;
    private String use;
    private String alg;
    private String n;
    private String e;

    public ApplePublicKeyDto() {

    }

    public ApplePublicKeyDto(JSONObject jsonObject) {
        this.kty = jsonObject.get("kty").toString();
        this.kid = jsonObject.get("kid").toString();
        this.use = jsonObject.get("use").toString();
        this.alg = jsonObject.get("alg").toString();
        this.n = jsonObject.get("n").toString();
        this.e = jsonObject.get("e").toString();
    }
}
