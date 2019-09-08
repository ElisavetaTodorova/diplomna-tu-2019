package api;


import api.models.EventLogsData;
import api.models.UserAction;
import hadoop.LogsPerDate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogsReaderService {
  
  public static List<EventLogsData> getLogsForGivenDate(String date) throws Exception {
    File file = new File("/Users/i338442/diplomna/tesst/src/main/resources/logsPerDate" + date.replace("/", "-") + "/part-r-00000");
    
    if (!file.exists()) {
      LogsPerDate.main(new String[] {date});
    }

    List<EventLogsData> eventLogsDataList = new ArrayList<>();
    Map<String, Integer> result = readFromFile(file);

    for (String eventData: result.keySet()) {
      int count = result.get(eventData);

      String[] logData = eventData.split(",");

      eventLogsDataList.add(new EventLogsData(count, logData[0], logData[1], logData[2], logData[3]));
    }

    return eventLogsDataList;
  }

  public static void main(String[] args) throws Exception {
    LogsPerDate.main(new String[] {"3/11/18"});
  }
  
  private static Map<String, Integer> readFromFile(File file) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(file));
    Map<String, Integer> result = new HashMap<>();
    String st;
    br.readLine();
    while ((st = br.readLine()) != null) {
      String[] split = st.split("\t");

      result.put(split[0], Integer.parseInt(split[1]));
    }
    
    return result;
  }
}
