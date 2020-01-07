import java.lang.reflect.Field;

public class Test {

  private String test;


  public static void main(String[] args) throws Exception {
    Test t = new Test();
    // Test.class.getDeclaredField("test")
    Test.class.getDeclaredField("test").set("test", "hj");


    System.out.println(t.test);
  }


}