package goodr0ne.washdisher;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class WashdisherDishController {
  private static final int MAX_CAPACITY = 20;
  private static final long MAX_DURATION = 5 * 60 * 1000;
  private static WashdisherStatus status = WashdisherStatus.getInstance();

  @RequestMapping("/load")
  public String loadBlank() {
    return "Please specify items quantity after slash within 1 - "
            + MAX_CAPACITY + " range";
  }

  @RequestMapping(value = "/load/{itemsQuantity}", method = GET)
  public String load(@PathVariable int itemsQuantity) {
    try {
      if (itemsQuantity < 1) {
        return "Please specify items quantity after slash within 1 - "
                + MAX_CAPACITY + " range";
      }
    } catch (Exception e) {
      return "Could not parse itemsQuantity parameter";
    }
    if (status.getIsCleaned()) {
      return "Washdisher already has some washed dishes, unload it first";
    }
    int capacity = status.getCapacity();
    if ((capacity + itemsQuantity) > MAX_CAPACITY) {
      return "Cannot load " + itemsQuantity + " items, washdisher already has "
              + status.getCapacity() + " and maximum capacity is " + MAX_CAPACITY;
    }
    status.setCapacity(capacity + itemsQuantity);
    status.saveStatus();
    return itemsQuantity + " items was successfully loaded into Washdisher, " +
            "current capacity is " + (capacity + itemsQuantity);
  }

  @RequestMapping("/unload")
  public String unload() {
    int capacity = status.getCapacity();
    if (capacity == 0) {
      return "Washdisher doesn't have any dishes inside";
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

  @RequestMapping("/start")
  public String start() {
    return "Washdisher is started";
  }

  @RequestMapping("/stop")
  public String stop() {
    return "Washdisher is stopped";
  }
}
