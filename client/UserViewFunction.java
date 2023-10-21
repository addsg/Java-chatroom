package MyQQ_Final.client;

import MyQQ_Final.com.DatePrase;
import MyQQ_Final.com.Message;
import MyQQ_Final.com.MessageType;
import MyQQ_Final.com.User;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Date;


public class UserViewFunction {


    public int checkUser(String username, String password) {
        int flag = 0;
        try {
            Socket socket = new Socket("127.0.0.1", 8888);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = new Message();
            message.setUser(new User(username, password));
            message.setType(MessageType.LOGIN);
            oos.writeObject(message);
            Message msg = (Message) ois.readObject();
            if (msg.getType().equals(MessageType.LOGIN_OK)) {
                flag = 0;
                ClientThread clientThread = new ClientThread(socket);
                clientThread.start();
                Message message2 = new Message();
                message2.setType(MessageType.User_LOGIN);
                ObjectOutputStream oos1 = new ObjectOutputStream(socket.getOutputStream());
                oos1.writeObject(message2);
                ClientView.ALL_THREADS.put(username, clientThread);
            } else if (msg.getType().equals(MessageType.User_EXIST)) {
                flag = 1;

                socket.close();

            } else {
                flag = 2;
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void signUser(String name, String password) {
        try {
            Socket socket = new Socket("127.0.0.1", 8888);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = new Message();
            message.setUser(new User(name, password));
            message.setType(MessageType.SIGN);
            oos.writeObject(message);
            Message msg = (Message) ois.readObject();
            if (msg.getType().equals(MessageType.SIGN_OK)) {
                JOptionPane.showMessageDialog(null, "注册成功", "成功", JOptionPane.PLAIN_MESSAGE);
                System.out.println("注册成功");
            } else {
                JOptionPane.showMessageDialog(null, "用户已经存在，注册失败，请换一个用户名", "错误", JOptionPane.ERROR_MESSAGE);
                System.out.println("用户已经存在，注册失败，请换一个用户名");
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void quit(String name) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ClientView.ALL_THREADS.get(name).getSocket().getOutputStream());
            Message message = new Message();
            message.setType(MessageType.Quit);
            message.setSender(name);
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAllMessage(String name, String text) {
        try {

            ObjectOutputStream oos = new ObjectOutputStream(ClientView.ALL_THREADS.get(name).getSocket().getOutputStream());
            Message message = new Message();
            message.setType(MessageType.SEND_ALL_ONLINE_COM);
            message.setSender(name);
            message.setContent(text);
            message.setDate(new Date());
            ClientView.chatArea.append("\n" + DatePrase.Prase(message.getDate()));
            ClientView.chatArea.append("\n你对所有人说：" + message.getContent());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSingleMessage(String name, String to, String text) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ClientView.ALL_THREADS.get(name).getSocket().getOutputStream());
            Message message = new Message();
            message.setType(MessageType.SEND_SINGLE_ONLINE_COM);
            message.setSender(name);
            message.setContent(text);
            message.setDate(new Date());
            message.setReceiver(to);
            ClientView.chatArea.append("\n" + DatePrase.Prase(message.getDate()));
            ClientView.chatArea.append("\n你对" + message.getReceiver() + "说：" + message.getContent());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String name, String to) {

        JFrame fileFrame = new JFrame("发送文件");
        JPanel srcPanel = new JPanel();
        JPanel destPanel = new JPanel();
        fileFrame.setVisible(true);
        fileFrame.setResizable(false);
        fileFrame.setSize(350, 230);
        JLabel srcLabel = new JLabel("要发送的文件:(格式：xxx/xxx/xxx或xxx\\xx\\xx\\xx)");
        srcLabel.setFont(new Font(null, Font.PLAIN, 14));
        JLabel destLabel = new JLabel("对方接受的地址:(格式：xxx/xxx/xxx或xxx\\xx\\xx\\xx)");
        destLabel.setFont(new Font(null, Font.PLAIN, 14));
        srcPanel.add(srcLabel);
        destPanel.add(destLabel);
        JTextArea srcArea = new JTextArea(1, 20);
        JTextArea destArea = new JTextArea(1, 20);
        srcPanel.add(srcArea);
        destPanel.add(destArea);
        JButton sendButton = new JButton("发送");
        JButton cancelButton = new JButton("取消");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);
        buttonPanel.add(cancelButton);
        Box vbox = Box.createVerticalBox();
        vbox.add(srcPanel);
        vbox.add(destPanel);
        vbox.add(buttonPanel);
        fileFrame.add(vbox);
        fileFrame.setLocationRelativeTo(null);
        cancelButton.addActionListener(e -> fileFrame.dispose());
        sendButton.addActionListener(e -> {
            try {
                String src = srcArea.getText();
                String dest = destArea.getText();
                Message message = new Message();
                message.setType(MessageType.SEND_SINGLE_ONLINE_FILE);
                message.setReceiver(to);
                message.setDest(dest);
                message.setSender(name);
                message.setDate(new Date());
                message.setSrc(src);
                System.out.println(src);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
                byte[] file = new byte[(int) new File(src).length()];
                bis.read(file);
                message.setFile(file);
                ObjectOutputStream oos = new ObjectOutputStream(ClientView.ALL_THREADS.get(name).getSocket().getOutputStream());
                oos.writeObject(message);
                bis.close();
                JOptionPane.showMessageDialog(fileFrame, "发送成功", "成功", JOptionPane.OK_OPTION);
                fileFrame.dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(fileFrame, "文件路径错误", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });


    }
}
