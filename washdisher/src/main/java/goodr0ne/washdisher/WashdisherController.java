package goodr0ne.washdisher;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WashdisherController {

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

  @RequestMapping("/load")
  public String load() {
    return "Washdisher is loaded";
  }

  @RequestMapping("/unload")
  public String unload() {
    return "Washdisher is unloaded";
  }

  @RequestMapping("/start")
  public String start() {
    return "Washdisher is started";
  }

  @RequestMapping("/stop")
  public String stop() {
    return "Washdisher is stopped";
  }

  @RequestMapping("/check_status")
  public String checkStatus() {
    return "Washdisher is operational";
  }
}
