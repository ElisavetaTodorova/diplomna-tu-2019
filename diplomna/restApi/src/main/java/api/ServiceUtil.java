package api;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceUtil {

  
  public Map<Integer, Integer> getUsersActivity() throws IOException {
    File file = new File("/Users/i338442/diplomna/backend/src/main/resources/usersActiveTime/part-r-00000");
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

  public int[] getEventsList() throws IOException {
    File file = new File("/Users/i338442/diplomna/backend/src/main/resources/outputEventContext/part-r-00000");
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

  public int getNumberOfCourses() throws IOException {
    File file = new File("/Users/i338442/diplomna/backend/src/main/resources/outputCourses/part-r-00000");
    BufferedReader br = new BufferedReader(new FileReader(file));

    int result = 0;

    String st;
    while ((st = br.readLine()) != null) {
      result++;

    }

    return result;
  }

  public int getNumberOfUsers() throws IOException {

    File file = new File("/Users/i338442/diplomna/backend/src/main/resources/outputUserIds/part-r-00000");
    BufferedReader br = new BufferedReader(new FileReader(file));

    int result = 0;

    String st;
    while ((st = br.readLine()) != null) {
      result++;

    }
    return result;
  }
}



