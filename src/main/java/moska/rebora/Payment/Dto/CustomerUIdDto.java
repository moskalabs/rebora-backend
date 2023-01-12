package moska.rebora.Payment.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomerUIdDto {

    Long userRecruitmentId;
    String customerUid;

    public CustomerUIdDto() {
    }
}
