package moska.rebora.Batch.Schedule;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class CancelWaitRecruitmentSchJob extends QuartzJobBean {

    private Job cancelWaitRecruitmentJob;
    private JobLauncher jobLauncher;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .toJobParameters();

        jobLauncher.run(cancelWaitRecruitmentJob, jobParameters);
    }
}
