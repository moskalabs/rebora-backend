package moska.rebora.Main.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Main.Dto.MainDto;
import moska.rebora.Main.Service.MainService;
import net.minidev.json.JSONObject;
import org.jboss.jandex.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "메인" , description = "메인 화면")
@RequestMapping("/api/main")
@RestController
public class MainController {

    @Autowired
    MainService mainService;

    @Tag(name = "메인")
    @Operation(summary = "메인 페이지 가져오기")
    @GetMapping("/")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메인 정보 가져오기 성공", content = @Content(schema = @Schema(implementation = MainDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    public MainDto main() {
        return mainService.getMain();
    }
}
