package MyQQ_Final.client;

import MyQQ_Final.com.DatePrase;
import MyQQ_Final.com.Message;
import MyQQ_Final.com.MessageType;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;


public class ClientThread extends Thread {
    private Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;

    }

    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if (message.getType().equals(MessageType.RET_ALL_ONLINE)) {
                    showOnline(message);
                } else if (message.getType().equals(MessageType.SEND_ALL_ONLINE_COM)) {
                    System.out.println("\n群聊消息: 用户" + message.getSender() + " 在 " + DatePrase.Prase(message.getDate()) + " 对你说:" + message.getContent());
                    receiveAllMessage(message);
                } else if (message.getType().equals(MessageType.SEND_SINGLE_ONLINE_COM)) {
                    System.out.println("\n私聊消息: 用户" + message.getSender() + " 在 " + DatePrase.Prase(message.getDate()) + " 对你说:" + message.getContent());
                    receiveSingleMessage(message);
                } else if (message.getType().equals(MessageType.SEND_SINGLE_ONLINE_FILE)) {
                    System.out.println(" 用户" + message.getSender() + " 在 " + DatePrase.Prase(message.getDate()) + " 向你发送了一个文件");
                    receiveFileMessage(message);
                } else if (message.getType().equals(MessageType.Quit)) {
                    socket.close();
                    System.out.println("下线成功");
                    System.exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveFileMessage(Message message) {
        JOptionPane.showMessageDialog(null,
                message.getSender() + "向你发送了一个文件,存放在:" + message.getDest(), "提示", JOptionPane.PLAIN_MESSAGE);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(message.getDest()));
            bos.write(message.getFile());
            System.out.println("\n接收成功");
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveSingleMessage(Message message) {
        String text = message.getContent();
        ClientView.chatArea.append("\n" + DatePrase.Prase(message.getDate()));
        ClientView.chatArea.append("\n私聊消息: 用户" + message.getSender() + " 对你说:" + text);
    }

    private void receiveAllMessage(Message message) {
        String text = message.getContent();
        ClientView.chatArea.append("\n" + DatePrase.Prase(message.getDate()));
        ClientView.chatArea.append("\n群聊消息: 用户" + message.getSender() + " 说:" + text);
    }

    private void showOnline(Message message) {
        try {
            String[] all = message.getContent().split(" ");
            ClientView.onlineList.setListData(all);
            ClientView.sendChoice.removeAllItems();
            ClientView.sendChoice.addItem("ALL");
            for (String item : all) {
                if (!item.equals(message.getReceiver())) {
                    ClientView.sendChoice.addItem(item);
                }
            }
            System.out.println("\n当前在线用户:");
            for (String user : all)
                System.out.println("用户:" + user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
