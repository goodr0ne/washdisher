package goodr0ne.washdisher;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * Triggered by outside quartz service job class.
 * Launches current washdisher status check
 */
public class WashdisherCheckStatusJob implements Job {
  public void execute(JobExecutionContext context) {
    WashdisherStatusController.checkStatus();
  }
}
