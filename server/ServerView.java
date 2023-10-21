package MyQQ_Final.server;

import MyQQ_Final.com.Message;
import MyQQ_Final.com.MessageType;
import MyQQ_Final.com.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class ServerView {
    public static HashMap<String, User> ALL_USERS = new HashMap<>();
    public static HashMap<String, ServerThread> ALL_ONLNES = new HashMap<>();
    public static ArrayList<User> ALL_USERS_Arr = new ArrayList<>();
    private static ServerFunction serverFunction = new ServerFunction();

    static {
        try {
            serverFunction.userIinit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ServerView() {
        ServerSocket serverSocket = null;
        try {
            System.out.println("==========服务端启动=======");
            serverSocket = new ServerSocket(8888);
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message message1 = (Message) ois.readObject();
                User user = message1.getUser();
                String name = user.getName();
                String password = user.getPassword();
                Message message = new Message();
                if (message1.getType().equals(MessageType.SIGN)) {
                    System.out.println(name + " " + password);
                    if (ALL_USERS.containsKey(name)) {
                        message.setType(MessageType.SIGN_FAILED);
                        oos.writeObject(message);
                        System.out.println("用户:" + name + "注册失败");
                    } else {
                        message.setType(MessageType.SIGN_OK);
                        oos.writeObject(message);
                        System.out.println("用户:" + name + "注册成功 " + "注册密码是:" + user.getPassword());
                        ALL_USERS.put(name, user);
                        ALL_USERS_Arr.add(user);
                        serverFunction.updateAllUsers();
                    }
                    socket.close();
                } else if (message1.getType().equals(MessageType.LOGIN)) {
                    if (serverFunction.serverCheck(name, password)) {
                        if (serverFunction.serverCheckonline(name)) {
                            message.setType(MessageType.LOGIN_OK);
                            System.out.println("用户:" + name + " 上线了");
                            ServerThread serverThread = new ServerThread(socket);
                            ALL_ONLNES.put(name, serverThread);
                            serverThread.start();
                            oos.writeObject(message);
                        } else {
                            message.setType(MessageType.User_EXIST);
                            oos.writeObject(message);
                            socket.close();
                        }
                    } else {
                        message.setType(MessageType.LOGIN_FAILED);
                        System.out.println("用户:" + name + " 登录失败" + " 使用的登录密码是:" + password);
                        oos.writeObject(message);
                        socket.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
