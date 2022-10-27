package moska.rebora.Common;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class ErrorPage implements ErrorController {

    @RequestMapping("/error")
    public JSONObject handleError(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (null != status) {
            int statusCode = Integer.valueOf(status.toString());
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                jsonObject.put("result", false);
                jsonObject.put("error", "403");
                return jsonObject;
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                jsonObject.put("result", false);
                jsonObject.put("error", "404");
                return jsonObject;
            }
        }
        jsonObject.put("result", false);
        jsonObject.put("error", "404");
        return jsonObject;
    }
}
