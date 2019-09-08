package hadoop;

public class MyThread extends Thread {
  
  private String id;
  
  public MyThread(String id) {
    this.id = id;
  }
  
  @Override
  public void run() {
    try {
      UserWithIdActionsCount.main(new String[] {id});
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
