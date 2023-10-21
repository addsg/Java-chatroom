package MyQQ_Final.server;

import MyQQ_Final.com.DatePrase;
import MyQQ_Final.com.Message;
import MyQQ_Final.com.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Set;


public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if (message.getType().equals(MessageType.SEND_SINGLE_ONLINE_FILE)) {//发文件
                    System.out.println("用户" + message.getSender() + " 在 " + DatePrase.Prase(message.getDate()) + " 对 " + message.getReceiver() + " 发送了文件");
                    sendFile(message);
                } else if (message.getType().equals(MessageType.SEND_ALL_ONLINE_COM)) {//群发
                    System.out.println("用户" + message.getSender() + " 在 " + DatePrase.Prase(message.getDate()) + " 对所有人说：" + message.getContent());
                    sendToAll(message.getSender(), message.getContent(), message.getDate());
                } else if (message.getType().equals(MessageType.SEND_SINGLE_ONLINE_COM)) {//私聊
                    System.out.println("用户" + message.getSender() + " 在 " + DatePrase.Prase(message.getDate()) + " 对 " + message.getReceiver() + " 说：" + message.getContent());
                    singleChat(message);
                } else if (message.getType().equals(MessageType.Quit)) {//退出
                    System.out.println(message.getSender() + "下线了");
                    retQuit(message);
                    break;
                } else if (message.getType().equals(MessageType.GET_ALL_ONLINE)) {//获取在线用户列表用户
                    System.out.println(message.getSender() + "请求获取在线用户");
                    retAllOnline();
                } else if (message.getType().equals(MessageType.User_LOGIN)) {
                    sendAlllonlie();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendFile(Message message) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ServerView.ALL_ONLNES.get(message.getReceiver()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void singleChat(Message message) {
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ServerView.ALL_ONLNES.get(message.getReceiver()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void retQuit(Message message) {
        try {
            ServerView.ALL_ONLNES.remove(message.getSender());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            sendAlllonlie();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAlllonlie() {
        Set<String> keys = ServerView.ALL_ONLNES.keySet();
        String ret = "";
        for (String key : keys) {
            ret += key + " ";
        }
        System.out.println(ret);
        Message message1 = new Message();
        message1.setType(MessageType.RET_ALL_ONLINE);
        message1.setContent(ret);
        for (String key : keys) {
            try {
                message1.setReceiver(key);
                ObjectOutputStream oos = new ObjectOutputStream(ServerView.ALL_ONLNES.get(key).getSocket().getOutputStream());
                oos.writeObject(message1);
                System.out.println("发送:" + key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendToAll(String sender, String text, Date date) {
        Set<String> keys = ServerView.ALL_ONLNES.keySet();
        for (String key : keys)
            if (!key.equals(sender)) {
                try {
                    Message message = new Message();
                    message.setType(MessageType.SEND_ALL_ONLINE_COM);
                    message.setSender(sender);
                    message.setContent(text);
                    message.setDate(date);
                    ObjectOutputStream oos = new ObjectOutputStream(ServerView.ALL_ONLNES.get(key).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    public Socket getSocket() {
        return socket;
    }

    public void retAllOnline() {
        try {
            Set<String> keys = ServerView.ALL_ONLNES.keySet();
            String ret = "";
            for (String key : keys) {
                ret += key + " ";
            }
            Message message = new Message();
            message.setType(MessageType.RET_ALL_ONLINE);
            message.setContent(ret);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
