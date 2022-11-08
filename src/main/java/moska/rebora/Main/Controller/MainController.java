package moska.rebora.Main.Controller;

import moska.rebora.Main.Service.MainService;
import net.minidev.json.JSONObject;
import org.jboss.jandex.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    MainService mainService;

    @GetMapping("/api/main")
    public JSONObject main() {
        return mainService.getMain();
    }
}
