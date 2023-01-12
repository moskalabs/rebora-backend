package moska.rebora.Batch.Schedule;

import lombok.AllArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@AllArgsConstructor
public class RepayFailureJobRunner extends JobRunner {

    @Override
    protected void doRun(ApplicationArguments args) {

        JobDetail jobDetail = buildJobDetail(RepayFailureSchJob.class, "repayFailureJob", "batch", new HashMap());
        Trigger trigger = buildJobTrigger("0 0 9,15 * * ?"); // 30초마다 실행

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private Scheduler scheduler;
}
