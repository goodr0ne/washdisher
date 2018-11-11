package goodr0ne.washdisher;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class WashdisherApplicationTests {
  private static Gson gson = new Gson();
  private static WashdisherStatus status;

  @BeforeClass
  public static void initialize() {
    System.out.println("All tests initialization begins...");
    WashdisherStatus.TURN_ON();
    status = WashdisherStatus.getInstance();
    System.out.println("All tests initialization completed");
  }

  @Before
  public void setUp() {
    System.out.println("Single test setup begins...");
    new WashdisherPowerController().turnOn();
    status.wipe();
    System.out.println("Single test setup completed");
  }


  @Test
  public void testTurningOnAlreadyTurnedOnWashdisher() {
    System.out.println("Testing testTurningOnAlreadyTurnedOnWashdisher...");
    String beforeStatus = gson.toJson(status.getJson());
    Assert.assertEquals("Washdisher is already power on and ready for your commands",
            new WashdisherPowerController().turnOn());
    String afterStatus = gson.toJson(status.getJson());
    Assert.assertEquals(beforeStatus, afterStatus);
    System.out.println("Testing testTurningOnAlreadyTurnedOnWashdisher successfully finished");
  }

  @Test
  public void testTurnedOffWashdisher() {
    System.out.println("Testing testTurnedOffWashdisher...");
    String beforeStatus = gson.toJson(status.getJson());
    new WashdisherPowerController().turnOff();
    Assert.assertEquals(WashdisherPowerController.POWER_OFF_MESSAGE,
            new WashdisherStatusController().status());
    Assert.assertEquals(WashdisherPowerController.POWER_OFF_MESSAGE,
            new WashdisherDishController().load(1));
    Assert.assertEquals(WashdisherPowerController.POWER_OFF_MESSAGE,
            new WashdisherDishController().start(1));
    Assert.assertEquals(WashdisherPowerController.POWER_OFF_MESSAGE,
            new WashdisherDishController().unload());
    Assert.assertEquals(WashdisherPowerController.POWER_OFF_MESSAGE,
            new WashdisherDishController().stop());
    String afterStatus = gson.toJson(status.getJson());
    Assert.assertEquals(beforeStatus, afterStatus);
    System.out.println("Testing testTurnedOffWashdisher successfully finished");
  }

  @Test
  public void testLoadingWrongNumberOfItems() {
    System.out.println("Testing testLoadingTooMuchDishes...");
    String beforeStatus = gson.toJson(status.getJson());
    Assert.assertEquals("Cannot load 100 items, washdisher already has 0 and maximum capacity is 20",
            new WashdisherDishController().load(100));
    Assert.assertEquals("Cannot load 2147483647 items, washdisher already has 0 and maximum capacity is 20",
            new WashdisherDishController().load(2147483647));
    Assert.assertEquals("Please specify items quantity after slash within 1 - 20 range",
            new WashdisherDishController().load((int)21474836479L));
    Assert.assertEquals("Please specify items quantity after slash within 1 - 20 range",
            new WashdisherDishController().load(0));
    Assert.assertEquals("Please specify items quantity after slash within 1 - 20 range",
            new WashdisherDishController().load(-1));
    String afterStatus = gson.toJson(status.getJson());
    Assert.assertEquals(beforeStatus, afterStatus);
    System.out.println("Testing testLoadingTooMuchDishes successfully finished");
  }

  @Test
  public void testSuccessfulDishesLoading() {
    System.out.println("Testing testSuccessfulDishesLoading...");
    String beforeStatus = gson.toJson(status.getJson());
    Assert.assertEquals("7 items was successfully loaded into Washdisher, current capacity is 7",
            new WashdisherDishController().load(7));
    String afterStatus = gson.toJson(WashdisherStatus.getInstance().getJson());
    Assert.assertNotEquals(beforeStatus, afterStatus);
    Assert.assertEquals(7, gson.fromJson(afterStatus, JsonElement.class)
            .getAsJsonObject().get("capacity").getAsInt());
    System.out.println("Testing testSuccessfulDishesLoading successfully finished");
  }

  @Test
  public void testWashingStartWithoutDishes() {
    System.out.println("Testing testWashingStartWithoutDishes...");
    String beforeStatus = gson.toJson(status.getJson());
    Assert.assertEquals("Washdisher is empty, load it first",
            new WashdisherDishController().start(47));
    String afterStatus = gson.toJson(status.getJson());
    Assert.assertEquals(beforeStatus, afterStatus);
    System.out.println("Testing testWashingStartWithoutDishes successfully finished");
  }
}
