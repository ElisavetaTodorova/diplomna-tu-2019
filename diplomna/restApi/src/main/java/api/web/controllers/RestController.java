package api.web.controllers;

import com.UsersIdFormatter;
import hadoop.UserWithIdActionsCount;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@org.springframework.web.bind.annotation.RestController
public class RestController {

  @RequestMapping("/getUserIdsMostActiveUser")
  public String getUserCount() {
    UsersIdFormatter usersIdFormatter = new UsersIdFormatter();

    Map<Integer, Integer> userIdsSortedByMostActiveUser = usersIdFormatter.getUserIdsSortedByMostActiveUser();

    JSONObject jsonObject = new JSONObject(userIdsSortedByMostActiveUser);

    return jsonObject.toString();
  }

  @RequestMapping("getUsersCountAcceding")
  public String getUserCountAcceding() {
    UsersIdFormatter usersIdFormatter = new UsersIdFormatter();

    Map<Integer, Integer> userIdsSortedByMostActiveUser = usersIdFormatter.getUserIdsSortedByMostActiveUser();

    JSONObject jsonObject = new JSONObject(userIdsSortedByMostActiveUser);

    return jsonObject.toString();
  }

  @RequestMapping(value = "/actions/{id}", method = GET)
  @ResponseBody
  public String getUserActions(@PathVariable String id) throws Exception {


    Map<String, Integer> userActionsById = getUserActionsById(id);

    JSONObject jsonObject = new JSONObject(userActionsById);

    return jsonObject.toString();
  }

  private Map<String, Integer> getUserActionsById(String userId) throws Exception {
    UserWithIdActionsCount.execute(new String[]{userId});

    File file = new File("/Users/i338442/diplomna/tesst/src/main/resources/outputUserIdsActionsCount" + userId + "/part-r-00000");
    BufferedReader br = new BufferedReader(new FileReader(file));

    Map<String, Integer> result = new HashMap<>();
    String st;
    while ((st = br.readLine()) != null) {
      String[] split = st.split("\t");

      result.put(split[0], Integer.parseInt(split[1]));
    }

    return result;
  }

  @RequestMapping(value = "/test/test", method = GET)
  public JSONArray getData(HttpServletResponse response) throws IOException, ParseException {
    Map<Integer, Integer> resultMap = getUsersActivity();

    response.setContentType(ContentType.APPLICATION_JSON.toString());

    return new JSONArray(resultMap.values().toString());
  }


  @RequestMapping(value = "/getMainPageData", method = GET)
  public Map<String, Object> getMainPageData(HttpServletResponse response) throws IOException {

    Collection<Integer> values = getUsersActivity().values();
    Integer[] integers = values.stream().sorted().toArray(Integer[]::new);
    Map<String, Object> result = new HashMap<>();


    Integer sum = values.stream()
        .reduce(0, Integer::sum);


    result.put("canvasData", new JSONArray(values));
    result.put("mostActiveLog", integers[11]);
    result.put("totalOfLogs", sum);
    result.put("totalOfUsers", getNumberOfUsers());
    result.put("numberOfCourses", getNumberOfCourses());
    result.put("pieCharData", getEventsList());

    response.setContentType(ContentType.APPLICATION_JSON.toString());

    return result;
  }
  
  public int[] getEventsList() throws IOException {
    File file = new File("/Users/i338442/diplomna/tesst/src/main/resources/outputEventContext/part-r-00000");
    BufferedReader br = new BufferedReader(new FileReader(file));

    Map<String, Integer> result = new HashMap<>();
    
    int all = 0;
    
    result.put("Конспект", 0);
    result.put("Лекция", 0);
    result.put("Упражнение", 0);
    result.put("Други", 0);

    
    String st;
    
    int[] resultArray = new int[4];
    while ((st = br.readLine()) != null) {

      String[] split = st.split("\t");

      int count = Integer.parseInt(split[1]);
      all += count;
      
      String type = split[0];
      
      if (type.contains("Конспект")) {
        int before = result.get("Конспект");
        result.put("Конспект", before + count);
        
      } else if (type.contains("Лекция")) {
        int before = result.get("Лекция");
        result.put("Лекция", before + count);
        
      } else if (type.contains("Упражнение")) {
        int before = result.get("Упражнение");
        result.put("Упражнение", before + count);
        
      } else {
        int before = result.get("Други");
        result.put("Други", before + count);
      }
      
    }

    resultArray[0] = result.get("Конспект") ;
    resultArray[1] = result.get("Лекция");
    resultArray[2] = result.get("Упражнение") ;
    resultArray[3] = result.get("Други") ;
    
    return resultArray;
  }
  
  private int getNumberOfCourses() throws IOException {
    File file = new File("/Users/i338442/diplomna/tesst/src/main/resources/outputCourses/part-r-00000");
    BufferedReader br = new BufferedReader(new FileReader(file));

    int result = 0;

    String st;
    while ((st = br.readLine()) != null) {
      result++;

    }
    
    return result;
  }

  private int getNumberOfUsers() throws IOException {

    File file = new File("/Users/i338442/diplomna/tesst/src/main/resources/outputUserIds/part-r-00000");
    BufferedReader br = new BufferedReader(new FileReader(file));

    int result = 0;
    
    String st;
    while ((st = br.readLine()) != null) {
      result++;

    }
    return result;
  }

  private Map<Integer, Integer> getUsersActivity() throws IOException {
    File file = new File("/Users/i338442/diplomna/tesst/src/main/resources/usersActiveTime/part-r-00000");
    BufferedReader br = new BufferedReader(new FileReader(file));

    Map<Integer, Integer> resultMap = new HashMap<>();


    String st;
    while ((st = br.readLine()) != null) {
      String[] split = st.split("\t");

      if (!split[0].equalsIgnoreCase("time")) {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yy");

        String date = split[0].substring(1);

        LocalDate localDate = LocalDate.parse(date, formatter);

        int year = localDate.getYear();
        int mount = localDate.getMonthValue();

        if (year == 2018) {

          int value = (resultMap.get(mount) == null ? 0 : resultMap.get(mount)) + Integer.parseInt(split[1]);
          resultMap.put(mount, value);
        }


      }

    }

    // Check for mounts with no activity
    for (int i = 1; i <= 12; i++) {
      if (!resultMap.containsKey(i)) {
        resultMap.put(i, 0);
      }
    }

    return resultMap;
  }

  @RequestMapping(value = "/avrgUserActivity", method = RequestMethod.GET)
  public String getAverageUserActivity() throws IOException {

    return (getUsersActivity().values().stream().mapToInt(Integer::intValue).sum() / 12) + "";
  }

  @RequestMapping(value = "/mostActiveTime", method = RequestMethod.GET)
  public int getMostActiveTime() throws IOException {

    Integer[] integers = getUsersActivity().values().stream().sorted().toArray(Integer[]::new);

    return integers[11];
  }

}
