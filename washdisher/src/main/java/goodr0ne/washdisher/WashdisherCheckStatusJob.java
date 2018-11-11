package goodr0ne.washdisher;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class WashdisherCheckStatusJob implements Job {
  public void execute(JobExecutionContext context) {
    WashdisherStatusController.checkStatus();
  }
}
