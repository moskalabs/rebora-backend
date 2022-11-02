package moska.rebora.User.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseTimeEntity;
import moska.rebora.Enum.PolicySubject;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Policy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id", nullable = false)
    private Long id;

    @Lob
    @Column
    private String policyContent;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PolicySubject policySubject;

    @Builder
    public Policy(String policyContent, PolicySubject policySubject) {
        this.policyContent = policyContent;
        this.policySubject = policySubject;
    }
}
