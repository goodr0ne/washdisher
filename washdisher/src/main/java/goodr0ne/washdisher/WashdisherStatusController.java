package goodr0ne.washdisher;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WashdisherStatusController {

  @RequestMapping("/check_status")
  public String checkStatus() {
    return "Washdisher is operational";
  }
}
