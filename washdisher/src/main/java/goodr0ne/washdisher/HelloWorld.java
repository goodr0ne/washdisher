package goodr0ne.washdisher;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

  @RequestMapping("/")
  public String index() {
    return "Hello dlroW!";
  }
}
