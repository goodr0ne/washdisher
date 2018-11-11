package goodr0ne.washdisher;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class WashdisherCheckStatusJob implements Job {
  public void execute(JobExecutionContext context) throws JobExecutionException {
    WashdisherStatusController.checkStatus();
  }
}
