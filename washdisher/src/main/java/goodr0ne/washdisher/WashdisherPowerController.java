package goodr0ne.washdisher;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Controller for maintaining http requests of actions related with washdisher
 * power management operations. Help instructions also could be retrieved here
 * Full list of actions: index, help, turn on, turn off.
 * Initialization of quartz scheduler also placed here.
 */
@RestController
public class WashdisherPowerController {
  static final String POWER_OFF_MESSAGE = "You are pressing the button " +
          "but nothing happens - seems that washdisher is power off";

  /**
   * welcome message, placed upon /index or core host url requests
   * @return String with welcome message
   */
  @RequestMapping(value = {"/", "/index"}, method = GET)
  public String index() {
    if (!WashdisherStatus.IS_TURN_ON()) {
      return "Washdisher standing right in front of you and keep silence.\n" +
              "Maybe how-to instruction have some tips (use /help url)";
    }
    return "Welcome to Washdisher! See instruction to get it started (via /help url)";
  }

  /**
   * accessed via /help request list of all possible operations
   * @return String with all operations instruction
   */
  @RequestMapping(value = "/help", method = GET)
  public String help() {
    return  "Here is the list of possible operations:                      <br>" +
            "/index or just localhost - &emsp; welcome message             <br>" +
            "/turn_on &emsp;&emsp;&emsp;&emsp;&emsp;&ensp; "                     +
            "- &emsp; turns washdisher power on, status will be restored   <br>" +
            "/turn_off &emsp;&emsp;&emsp;&emsp;&emsp;&ensp;"                     +
            "- &emsp; turns washdisher power off                           <br>" +
            "/status &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp;"                 +
            "- &emsp; outputs current washdisher status data in json format<br>" +
            "/load/{integer} &emsp;&emsp;&emsp;"                                 +
            "- &emsp; loads portion of items into washdisher               <br>" +
            "/unload &emsp;&emsp;&emsp;&emsp;&emsp;&ensp;&ensp;"                 +
            "- &emsp; unloads all items from washdisher                    <br>" +
            "/start/{integer} &emsp;&emsp;&emsp;"                                +
            "- &emsp; starts washing cycle for specified time in seconds   <br>" +
            "/start &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp;&nbsp; "           +
            "- &emsp; try to resume stopped washing cycle                  <br>" +
            "/stop &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp;&nbsp; "            +
            "- &emsp; stops current washing cycle if it's running          <br>" +
            "/help &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp;&nbsp; "            +
            "- &emsp; view this instruction                                <br>";
  }

  //initialization of quartz check washdisher status task
  @Bean
  public JobDetail jobDetail() {
    return JobBuilder.newJob().ofType(WashdisherCheckStatusJob.class)
            .storeDurably()
            .withIdentity("Washdisher_Check_Status_Job_Detail")
            .withDescription("Watch how dishes washed...")
            .build();
  }

  //this task triggered each second
  @Bean
  public Trigger trigger(JobDetail job) {
    return TriggerBuilder.newTrigger().forJob(job)
            .withIdentity("Washdisher_Check_Status_Job_Trigger")
            .withDescription("You just want to be sure that everything goes well...")
            .withSchedule(simpleSchedule().repeatForever().withIntervalInSeconds(1))
            .build();
  }

  /**
   * /turn_on interaction will try to power on washdisher. Washdisher is turned off by default.
   * Turned off washdisher will be unavailable for use by any operation, except /help and /turn_on.
   * Turn on operation also will try to recover stored washdisher status from mongo cloud db.
   * @return String with output
   */
  @RequestMapping(value = "/turn_on", method = GET)
  public String turnOn() {
    if (WashdisherStatus.IS_TURN_ON()) {
      return "Washdisher is already power on and ready for your commands";
    }
    WashdisherStatus.TURN_ON();
    return "Washdisher is turned on,<br>lights are blinking and engines are shaking, " +
            "<br>you can insert your dishes needed to be washed!";
  }

  /**
   * /turn_off interaction will shut down washdisher. Washdisher is turned off by default.
   * Turned off washdisher will be unavailable for use by any operation, except /help and /turn_on.
   * Turn off operation also will erase all current non-saved to cloud status data.
   * @return String with output
   */
  @RequestMapping(value = "/turn_off", method = GET)
  public String turnOff() {
    if (!WashdisherStatus.IS_TURN_ON()) {
      return POWER_OFF_MESSAGE;
    }
    WashdisherStatus.TURN_OFF();
    return "Washdisher is turned off";
  }
}
