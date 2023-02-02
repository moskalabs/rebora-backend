package moska.rebora.Batch.Schedule;

import lombok.AllArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@AllArgsConstructor
public class CancelJobRunner extends JobRunner{


    private Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {

        JobDetail jobDetail = buildJobDetail(CancelSchJob.class, "cancelRecruitmentJob", "batch", new HashMap());

        Trigger trigger = buildJobTrigger("0 0 0 * * ?"); // 00시에 한번 실행
        //Trigger trigger = buildJobTrigger("0/30 * * * * ?"); // 30초마다 실행

        try{
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
