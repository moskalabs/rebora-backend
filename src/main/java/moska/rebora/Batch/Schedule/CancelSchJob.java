package moska.rebora.Batch.Schedule;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class CancelSchJob extends QuartzJobBean {

    private Job cancelRecruitmentJob;

    private JobLauncher jobLauncher;

    /**
     * 배치를 실행시키는 구문
     * @param context
     */
    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context)  {

        String requestDate = (String) context.getJobDetail().getJobDataMap().get("requestDate");

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .toJobParameters();

        jobLauncher.run(cancelRecruitmentJob, jobParameters);
    }
}
