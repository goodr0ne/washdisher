package goodr0ne.washdisher;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class WashdisherPowerController {

  @RequestMapping(value = "/", method = GET)
  public String index() {
    return "Welcome to Washdisher!";
  }

  @RequestMapping(value = "/turn_on", method = GET)
  public String turnOn() {
    return "Washdisher is turned on";
  }

  @RequestMapping(value = "/turn_off", method = GET)
  public String turnOff() {
    return "Washdisher is turned off";
  }
}
