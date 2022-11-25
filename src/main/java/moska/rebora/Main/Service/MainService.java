package moska.rebora.Main.Service;

import moska.rebora.Main.Dto.MainDto;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Pageable;

public interface MainService {

    MainDto getMain();
}
