package moska.rebora.Common.Controller;

import lombok.AllArgsConstructor;
import moska.rebora.Common.BaseListResponse;
import moska.rebora.Common.Entity.Category;
import moska.rebora.Common.Repository.CategoryRepository;
import moska.rebora.Common.Service.CommonService;
import moska.rebora.Enum.NotificationKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/common")
@AllArgsConstructor
public class CommonController {


    private CategoryRepository categoryRepository;

    private CommonService commonService;

    @GetMapping("/getCategory")
    public BaseListResponse<Category> getCategory(){
        BaseListResponse<Category> baseListResponse = new BaseListResponse<>();
        baseListResponse.setList(categoryRepository.findAll());
        baseListResponse.setResult(true);

        return baseListResponse;
    }

    @GetMapping("/getRegion")
    public BaseListResponse<String> getRegion(){
        BaseListResponse<String> baseListResponse = new BaseListResponse<>();
        List<String> regionList = new ArrayList<>(Arrays.asList("전국", "서울", "경기", "인천", "대전/충청", "대구", "부산/울산", "경상", "광주/전라/제주"));
        baseListResponse.setList(regionList);
        baseListResponse.setResult(true);
        return baseListResponse;
    }

    @PostMapping("/createNotification")
    public void test(
            @Param("notificationSubject") String notificationSubject,
            @Param("notificationContent") String notificationContent,
            @Param("notificationKind") NotificationKind notificationKind,
            @Param("recruitmentId") Long recruitmentId,
            @Param("movieId") Long movieId
    ) {
        commonService.createNotification(notificationSubject, notificationContent, notificationKind, recruitmentId, movieId);
    }
}
