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
public class CancelWaitRecruitmentJobRunner extends JobRunner{

    private Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {

        JobDetail jobDetail = buildJobDetail(CancelWaitRecruitmentSchJob.class, "cancelWaitRecruitmentJob", "batch", new HashMap());
        //Trigger trigger = buildJobTrigger("0 0/30 * * * ?"); // 30초마다 실행
        Trigger trigger = buildJobTrigger("0 0/5 * * * ?"); // 30분마다 실행

        try{
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
