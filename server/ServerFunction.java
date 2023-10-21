package MyQQ_Final.server;

import MyQQ_Final.com.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class ServerFunction {
    public boolean serverCheck(String name, String password) {
        User u = ServerView.ALL_USERS.get(name);

        if (u == null)
            return false;
        if (!u.getPassword().equals(password))
            return false;
        return true;
    }

    public void updateAllUsers() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream("src/MyQQ_Final/data/User_Data.txt"));
        ) {
            oos.writeObject(ServerView.ALL_USERS_Arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userIinit() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/MyQQ_Final/data/User_Data.txt"));
        ) {
            ServerView.ALL_USERS_Arr = (ArrayList<User>) ois.readObject();
            for (User user : ServerView.ALL_USERS_Arr) {
                ServerView.ALL_USERS.put(user.getName(), user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean serverCheckonline(String name) {
        if (ServerView.ALL_ONLNES.containsKey(name))
            return false;
        return true;
    }
}

