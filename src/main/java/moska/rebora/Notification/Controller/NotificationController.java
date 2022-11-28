package moska.rebora.Notification.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Notification.NotificationDto;
import moska.rebora.Notification.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
@Tag(name = "알림")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Tag(name = "알림")
    @GetMapping("/getPageByUser")
    public BasePageResponse<NotificationDto> getNotificationList(@PageableDefault Pageable pageable) {

        BasePageResponse<NotificationDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setResult(true);
        basePageResponse.setPage(notificationService.getNotificationList(pageable, SecurityContextHolder.getContext().getAuthentication().getName()));

        return basePageResponse;
    }
}
