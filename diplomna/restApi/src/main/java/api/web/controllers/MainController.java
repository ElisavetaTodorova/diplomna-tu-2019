package api.web.controllers;

import api.LogsReaderService;
import api.ServiceUtil;
import api.models.EventLogsData;
import api.models.Search;
import api.models.UserAction;
import hadoop.UserWithIdActionsCount;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(value = "/")
public class MainController {

  @Autowired
  private ServiceUtil serviceUtil;

  @GetMapping(value = "/searchLogsGet")
  public String searchLogsGet(@ModelAttribute("search") Search search, Model model) throws Exception {

    return "logsSearch";
  }


  @PostMapping(value = "/searchLogs")
  public String searchLogs(@ModelAttribute("search") Search search, Model model) throws Exception {

    String data = search.getData();

    List<EventLogsData> logsEvents = LogsReaderService.getLogsForGivenDate(data);
    model.addAttribute("logsEvents", logsEvents);


    return "logsSearch";
  }

  @RequestMapping(value = "/homePage")
  public String login(Model model) throws IOException {

    Collection<Integer> values = serviceUtil.getUsersActivity().values();
    Integer[] integers = values.stream().sorted().toArray(Integer[]::new);
    Map<String, Object> result = new HashMap<>();


    Integer sum = values.stream()
        .reduce(0, Integer::sum);


    model.addAttribute("canvasData", new JSONArray(values));
    model.addAttribute("mostActiveLog", integers[11]);
    model.addAttribute("totalOfLogs", sum);
    model.addAttribute("totalOfUsers", serviceUtil.getNumberOfUsers());
    model.addAttribute("numberOfCourses", serviceUtil.getNumberOfCourses());
    model.addAttribute("pieCharData", new JSONArray(serviceUtil.getEventsList()));

//    response.setContentType(ContentType.APPLICATION_JSON.toString());
    return "index";
  }


  @RequestMapping(value = "/getUserActions", method = {RequestMethod.GET, RequestMethod.POST})
  public String getUserActionsByIdMainPage(@ModelAttribute("search") Search search, Model model) throws Exception {
    return "table";
  }

  @PostMapping("/getUserActionsPost")
  public String greetingSubmit(@ModelAttribute("search") Search search, Model model) throws Exception {

    int id = search.getId();

    Map<String, Integer> userActionsById = getUserActionsById(id + "");

    List<UserAction> userActions = new ArrayList<>();


    for (String userId : userActionsById.keySet()) {

      userActions.add(new UserAction(userId, userActionsById.get(userId)));
    }

    model.addAttribute("userActions", userActions);
    return "table";
  }


  @GetMapping(value = "/getUserIds")
  public String getUserIds(@ModelAttribute("search") Search search, Model model) throws Exception {

    File file = new File("/Users/i338442/diplomna/tesst/src/main/resources/outputUserIds/part-r-00000");
    BufferedReader br = new BufferedReader(new FileReader(file));

    Map<String, Integer> result = new HashMap<>();
    String st;
    while ((st = br.readLine()) != null) {
      String[] split = st.split("\t");

      result.put(split[0], Integer.parseInt(split[1]));
    }

    List<UserAction> userActions = new ArrayList<>();

    for (String userId : result.keySet()) {

      userActions.add(new UserAction(userId, result.get(userId)));
    }

    model.addAttribute("userActions", userActions);
    model.addAttribute("style", "visibility:hidden");

    return "table";
  }

  @GetMapping(value = "/getEvents")
  public String getTypesOfEvents(@ModelAttribute("search") Search search, Model model) throws Exception {

    File file = new File("/Users/i338442/diplomna/tesst/src/main/resources/outputEventContext/part-r-00000");
    BufferedReader br = new BufferedReader(new FileReader(file));

    Map<String, Integer> result = new HashMap<>();
    String st;
    br.readLine();
    while ((st = br.readLine()) != null) {
      String[] split = st.split("\t");

      result.put(split[0], Integer.parseInt(split[1]));
    }

    List<UserAction> userActions = new ArrayList<>();

    for (String userId : result.keySet()) {

      userActions.add(new UserAction(userId, result.get(userId)));
    }

    model.addAttribute("userActions", userActions);
    model.addAttribute("style", "visibility:hidden");

    return "table";
  }


  @RequestMapping(value = "/test/{id}", method = RequestMethod.GET)
  public String getUserActionsById(@PathVariable String id, Model model) throws Exception {

    Map<String, Integer> userActionsById = getUserActionsById(id);

    List<UserAction> userActions = new ArrayList<>();


    for (String userId : userActionsById.keySet()) {

      userActions.add(new UserAction(userId, userActionsById.get(userId)));
    }

    model.addAttribute("userActions", userActions);
    return "table";
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

}
