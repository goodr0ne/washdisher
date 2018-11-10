package goodr0ne.washdisher;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class WashdisherDishController {
  private static final int MAX_CAPACITY = 20;
  private static final long MAX_DURATION_SECONDS = 5 * 60;
  private static WashdisherStatus status = WashdisherStatus.getInstance();

  @RequestMapping(value = "/load", method = GET)
  public String loadBlank() {
    if (!WashdisherStatus.IS_TURN_ON()) {
      return WashdisherPowerController.POWER_OFF_MESSAGE;
    }
    return "Please specify items quantity after slash within 1 - "
            + MAX_CAPACITY + " range";
  }

  //Correctly intercept parsing exceptions for itemsQuantity parameter
  @RequestMapping(value = "/load/{itemsQuantity}", method = GET)
  public String load(@PathVariable int itemsQuantity) {
    if (!WashdisherStatus.IS_TURN_ON()) {
      return WashdisherPowerController.POWER_OFF_MESSAGE;
    }
    if (status.getIsOperational()) {
      return "Washdisher is operational, wait until finish or stop it manually";
    }
    if (status.getIsCleaned()) {
      return "Washdisher already has some washed dishes, unload it first";
    }
    try {
      if (itemsQuantity < 1) {
        return "Please specify items quantity after slash within 1 - "
                + MAX_CAPACITY + " range";
      }
    } catch (Exception e) {
      return "Could not parse itemsQuantity parameter";
    }
    int capacity = status.getCapacity();
    if ((capacity + itemsQuantity) > MAX_CAPACITY) {
      return "Cannot load " + itemsQuantity + " items, washdisher already has "
              + status.getCapacity() + " and maximum capacity is " + MAX_CAPACITY;
    }
    status.wipe();
    status.setCapacity(capacity + itemsQuantity);
    status.saveStatus();
    return itemsQuantity + " items was successfully loaded into Washdisher, " +
            "current capacity is " + (capacity + itemsQuantity);
  }

  @RequestMapping(value = "/unload", method = GET)
  public String unload() {
    if (!WashdisherStatus.IS_TURN_ON()) {
      return WashdisherPowerController.POWER_OFF_MESSAGE;
    }
    int capacity = status.getCapacity();
    if (capacity == 0) {
      return "Washdisher doesn't have any dishes inside";
    }
    if (status.getIsOperational()) {
      return "Washdisher is operational and currently washing dishes, " +
              "wait until finish or stop it manually to unload dishes";
    }
    boolean isCleaned = status.getIsCleaned();
    status.wipe();
    status.saveStatus();
    String output = capacity + " items was unloaded from washdisher";
    if (isCleaned) {
      output = output + ", they are clean and shiny!";
    } else {
      output = output + ", they are not clean enough";
    }
    return output;
  }

  @RequestMapping(value = "/start", method = GET)
  public String startBlank() {
    if (!WashdisherStatus.IS_TURN_ON()) {
      return WashdisherPowerController.POWER_OFF_MESSAGE;
    }
    if (status.getDuration() < 1) {
      return "Please specify washing time in seconds after slash within 1 - "
              + MAX_DURATION_SECONDS + " range";
    } else {
      if (status.getIsOperational()) {
        return "Could not resume washing cycle - washdisher is already operational";
      }
      if (status.getCapacity() < 1) {
        return "Could not resume washing cycle - washdisher is empty, load it first";
      }
      if (status.getIsCleaned()) {
        return "Could not resume washing cycle - " +
                "washdisher have clean dishes inside, unload it first";
      }
      status.setIsOperational(true);
      status.saveStatus();
      return "resuming last washing cycle...";
    }
  }

  //Correctly intercept parsing exceptions for seconds parameter
  @RequestMapping(value = "/start/{seconds}", method = GET)
  public String start(@PathVariable int seconds) {
    if (!WashdisherStatus.IS_TURN_ON()) {
      return WashdisherPowerController.POWER_OFF_MESSAGE;
    }
    if (status.getIsOperational()) {
      return "Washdisher is already operational";
    }
    if (status.getCapacity() < 1) {
      return "Washdisher is empty, load it first";
    }
    if (status.getIsCleaned()) {
      return "Washdisher have clean dishes inside, unload it first";
    }
    try {
      if (seconds < 1) {
        return "Please specify washing time in seconds after slash within 1 - "
                + MAX_DURATION_SECONDS + " range";
      }
      if (seconds > MAX_DURATION_SECONDS) {
        return "Please specify washing time in seconds within 1 - "
                + MAX_DURATION_SECONDS + " range";
      }
    } catch (Exception e) {
      return "Could not parse seconds parameter";
    }
    status.setIsOperational(true);
    status.setDuration(seconds * 1000);
    status.setWashedTime(0);
    status.saveStatus();
    return "Washdisher is started";
  }

  @RequestMapping(value = "/stop", method = GET)
  public String stop() {
    if (!WashdisherStatus.IS_TURN_ON()) {
      return WashdisherPowerController.POWER_OFF_MESSAGE;
    }
    if (!status.getIsOperational()) {
      return "Washdisher is not operational and could not be stopped";
    }
    status.setIsOperational(false);
    status.saveStatus();
    return "Washdisher is stopped";
  }
}
