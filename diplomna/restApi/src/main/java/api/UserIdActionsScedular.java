package api;

import com.UsersIdFormatter;
import hadoop.UserWithIdActionsCount;

import java.io.IOException;
import java.util.Map;

public class UserIdActionsScedular {

  public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
    UsersIdFormatter usersIdFormatter = new UsersIdFormatter();

    Map<Integer, Integer> userIdsSortedByMostActiveUser = usersIdFormatter.getUserIdsSortedByMostActiveUser();

    for (Integer integer:userIdsSortedByMostActiveUser.keySet()) {

      UserWithIdActionsCount.execute(new String[] {integer + ""});


    }
    
  }
}
