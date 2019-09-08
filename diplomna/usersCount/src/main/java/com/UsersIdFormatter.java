package com;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UsersIdFormatter {
  
  private static Map<Integer, Integer> map = new HashMap<Integer, Integer>();
  
  
  static {
    try {
      fillUseIdsMap();
    } catch (IOException e) {
      e.printStackTrace();
      // may be write some logs here 
    }
  }
  
  public Map<Integer, Integer> getUserIdsSortedByMostActiveUser() {
    final Map<Integer, Integer> userWithMostActivity = map.entrySet()
        .stream()
        .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    
    return userWithMostActivity;
  }
  
  public Map<Integer, Integer> getUsersIdByNameInAccedingOrder() {
    final Map<Integer, Integer> userWithMostActivity1 = map.entrySet()
        .stream()
        .sorted(Map.Entry.<Integer, Integer>comparingByKey())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    
    return userWithMostActivity1;
  }
  
  
  private static void fillUseIdsMap() throws IOException {
    File file = new File("/Users/i338442/diplomna/tesst/src/main/resources/outputUserIds/part-r-00000");
    BufferedReader br = new BufferedReader(new FileReader(file));
    

    String st;
    while ((st = br.readLine()) != null) {
      String[] split = st.split("\\s");

      UsersIdFormatter.map.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }
  } 

}

