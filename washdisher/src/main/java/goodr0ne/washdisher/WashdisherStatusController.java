package goodr0ne.washdisher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class WashdisherStatusController {
  private static long CHECK_FAIL_TIME_LIMIT = 10 * 1000;
  private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private static WashdisherStatus status = WashdisherStatus.getInstance();

  @RequestMapping(value = "/status", method = GET)
  public String status() {
    checkStatus();
    return gson.toJson(WashdisherStatus.getInstance());
  }

  private void checkStatus() {
    if (!status.getIsOperational() || status.getIsCleaned()) {
      return;
    }
    long time = System.currentTimeMillis();
    long lastCheck = status.getLastCheckTime();
    status.setLastCheckTime(time);
    if ((time - lastCheck) < CHECK_FAIL_TIME_LIMIT) {
      long washedTime = status.getWashedTime();
      long totalTime = washedTime + (time - lastCheck);
      status.setWashedTime(totalTime);
      if (totalTime > status.getDuration()) {
        status.setIsCleaned(true);
        status.setIsOperational(false);
      }
    }
    status.saveStatus();
  }
}
