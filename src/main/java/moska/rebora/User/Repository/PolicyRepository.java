package moska.rebora.User.Repository;

import moska.rebora.Enum.PolicySubject;
import moska.rebora.User.Entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    Policy getFirstByPolicySubjectOrderByRegDateDesc(PolicySubject policySubject);
}
