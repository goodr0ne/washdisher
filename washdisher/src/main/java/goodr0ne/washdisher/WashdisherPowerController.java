package goodr0ne.washdisher;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WashdisherPowerController {

  @RequestMapping("/")
  public String index() {
    return "Welcome to Washdisher!";
  }

  @RequestMapping("/turn_on")
  public String turnOn() {
    return "Washdisher is turned on";
  }

  @RequestMapping("/turn_off")
  public String turnOff() {
    return "Washdisher is turned off";
  }
}
