package goodr0ne.washdisher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Controller for maintaining http request of retrieve current washdisher status action.
 * Also used by background check status quartz tasks for updating current washing status.
 */
@RestController
public class WashdisherStatusController {
  private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private static WashdisherStatus status = WashdisherStatus.getInstance();

  @RequestMapping(value = "/status", method = GET)
  public String status() {
    status = WashdisherStatus.getInstance();
    if (!WashdisherStatus.IS_TURN_ON()) {
      return WashdisherPowerController.POWER_OFF_MESSAGE;
    }
    checkStatus();
    return gson.toJson(WashdisherStatus.getInstance());
  }

  static void checkStatus() {
    status = WashdisherStatus.getInstance();
    if (!WashdisherStatus.IS_TURN_ON() || !status.getIsOperational() || status.getIsCleaned()) {
      return;
    }
    long time = System.currentTimeMillis();
    long lastCheck = status.getLastCheckTime();
    status.setLastCheckTime(time);
    long CHECK_FAIL_TIME_LIMIT = 10 * 1000;
    if ((time - lastCheck) < CHECK_FAIL_TIME_LIMIT) {
      long washedTime = status.getWashedTime();
      long totalTime = washedTime + (time - lastCheck);
      status.setWashedTime(totalTime);
      if (totalTime > status.getDuration()) {
        status.setIsCleaned();
        status.setIsOperational(false);
      }
    }
    new Thread(new WashdisherSaveStatusThread()).start();
  }
}
