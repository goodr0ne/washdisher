package goodr0ne.washdisher;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WashdisherDishController {

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
}
