package api.models;

public class EventLogsData {

  private int count;
  private String eventContext;
  private String component;
  private String eventName;
  private String eventDescription;

  public EventLogsData(int count, String eventContext, String component, String eventName, String eventDescription) {
    this.count = count;
    this.eventContext = eventContext;
    this.component = component;
    this.eventName = eventName;
    this.eventDescription = eventDescription;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getEventContext() {
    return eventContext;
  }

  public void setEventContext(String eventContext) {
    this.eventContext = eventContext;
  }

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getEventDescription() {
    return eventDescription;
  }

  public void setEventDescription(String eventDescription) {
    this.eventDescription = eventDescription;
  }
}
