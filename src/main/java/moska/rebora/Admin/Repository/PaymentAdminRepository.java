package moska.rebora.Admin.Repository;

import moska.rebora.Admin.Dto.AdminPaymentDto;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PaymentAdminRepository {

    Page<AdminPaymentDto> getPaymentDto(Pageable pageable, UserSearchCondition userSearchCondition, LocalDateTime fromDateTime, LocalDateTime toDateTime);

    AdminPaymentDto getPaymentInfo(String paymentId);
}
