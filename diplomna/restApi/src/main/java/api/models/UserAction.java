package api.models;

public class UserAction {
  
  public String actionName;
  public int count;

  public UserAction(String actionName, int count) {
    this.actionName = actionName;
    this.count = count;
  }
}
