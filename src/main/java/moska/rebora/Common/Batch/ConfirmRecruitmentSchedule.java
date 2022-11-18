package moska.rebora.Common.Batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConfirmRecruitmentSchedule {

    private final Job confirmRecruitmentJob;
    private final JobLauncher jobLauncher;

    @Scheduled(fixedDelay = 60 * 1000L)
    public void executeConfirmJob(){
        try {
            log.info("fetch gas station job start");
            jobLauncher.run(
                    confirmRecruitmentJob,
                    new JobParametersBuilder()
                            .addString("datetime", LocalDateTime.now().toString())
                            .toJobParameters()  // job parameter 설정
            );
            log.info("successfully complete job\n\n");
        } catch (JobExecutionException ex) {
            log.info("jobException={}",ex.getMessage());
        }
    }
}
