package MyQQ_Final.client;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Scanner;


public class ClientView {
    public static HashMap<String, ClientThread> ALL_THREADS = new HashMap<String, ClientThread>();
    public static JList<String> onlineList;//在线用户列表
    public static JFrame clientFrame = new JFrame();
    public static JPanel chatFramePanel;
    public static JFrame chatFrame;//聊天界面
    public static JTextArea chatArea;//消息显示框
    public static JTextArea inputArea;//输入框
    public static JScrollPane inputPanel;//
    public static JScrollPane messagePanel;//输入面板
    public static JComboBox<String> sendChoice;//发送给谁组件
    public static JScrollPane onlineScrollPane;//

    static {
        onlineList = new JList<String>();
        chatArea = new JTextArea();//聊天框
        inputArea = new JTextArea();//输入框
        sendChoice = new JComboBox<String>();
    }

    private Scanner sc = new Scanner(System.in);
    private UserViewFunction uf = new UserViewFunction();

    public ClientView() {
        JPanel welcomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 22));
        welcomeLabel.setText("==========欢迎进入客户端==========");
        welcomPanel.add(welcomeLabel);
        //账号框
        JPanel namePanel = new JPanel();
        JLabel nameLable = new JLabel();
        nameLable.setText("账号");
        JTextField nameField = new JTextField(16);
        namePanel.add(nameLable);
        namePanel.add(nameField);
        //密码框
        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel();
        passwordLabel.setText("密码");
        JTextField passwordField = new JTextField(16);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        //登录，注册，退出按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("登录");
        JButton signButton = new JButton("注册");
        JButton quitButton = new JButton("退出");
        buttonPanel.add(okButton);
        buttonPanel.add(signButton);
        buttonPanel.add(quitButton);
        //垂直显示
        Box vBox = Box.createVerticalBox();
        vBox.add(welcomPanel);
        vBox.add(namePanel);
        vBox.add(passwordPanel);
        vBox.add(buttonPanel);
        clientFrame.add(vBox);
        //分别给登录，注册，退出增加功能
        //退出
        quitButton.addActionListener(e -> {
            System.exit(0);
        });
        //注册
        signButton.addActionListener(e -> showSignview());
        //登录
        okButton.addActionListener(e -> {
            String name = nameField.getText();
            String password = passwordField.getText();
            if (uf.checkUser(name, password) == 0) {
                clientFrame.dispose();
                chatFrame = new JFrame(name + "的界面");
                chatFramePanel = new JPanel(null);
                chatFrame.setSize(800, 650);
                JButton sendBtn = new JButton("发送");
                JButton chatQuitButton = new JButton("退出");
                inputPanel = new JScrollPane(inputArea);
                //输入面板
                inputArea.setLineWrap(true);
                inputPanel.setBounds(225, 400, 500, 122);
                chatFramePanel.add(inputPanel);
                //聊天消息面板
                chatArea.setEditable(false);
                chatArea.setLineWrap(true);
                messagePanel = new JScrollPane(chatArea);
                messagePanel.setBounds(225, 20, 500, 332);
                chatFramePanel.add(messagePanel);
                //在线用户列表
                JLabel onlineLabel = new JLabel("当前在线用户:");
                onlineLabel.setFont(new Font(null, Font.PLAIN, 15));
                onlineLabel.setBounds(15, 10, 100, 50);
                onlineScrollPane = new JScrollPane(onlineList);
                onlineScrollPane.setBounds(15, 50, 200, 480);
                chatFramePanel.add(onlineLabel);
                chatFramePanel.add(onlineScrollPane);

                //发送消息按钮
                sendBtn.setBounds(605, 545, 100, 30);

                chatFramePanel.add(sendBtn);
                chatQuitButton.setBounds(20, 545, 100, 30);
                chatFramePanel.add(chatQuitButton);
                //发送文件按钮
                JButton fileButton = new JButton("发送文件");
                fileButton.setBounds(480, 545, 100, 30);
                chatFramePanel.add(fileButton);
                //选择发送给谁
                JLabel sendTo = new JLabel("Send to:");
                sendTo.setFont(new Font(null, Font.PLAIN, 18));
                sendTo.setBounds(520, 360, 100, 30);

                //按钮增加功能
                sendBtn.addActionListener(q -> {
                    String text = inputArea.getText();
                    inputArea.setText("");
                    String to = sendChoice.getSelectedItem().toString();
                    if (to.equals("ALL")) {
                        uf.sendAllMessage(name, text);
                    } else {
                        uf.sendSingleMessage(name, to, text);
                    }
                });//发消息
                chatQuitButton.addActionListener(q -> uf.quit(name));//退出
                JButton chargeButton = new JButton("充值");
                chargeButton.setBounds(350, 545, 100, 30);
                chatFramePanel.add(chargeButton);
                //充值
                chargeButton.addActionListener(q -> showChargeview());
                fileButton.addActionListener(q -> {
                    inputArea.setText("");
                    String to = sendChoice.getSelectedItem().toString();
                    uf.sendFile(name, to);
                });
                chatFramePanel.add(sendTo);
                sendChoice.setBounds(595, 362, 120, 25);
                chatFramePanel.add(sendChoice);
                chatFrame.setContentPane(chatFramePanel);
                chatFrame.setResizable(false);
                chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                chatFrame.setLocationRelativeTo(null);
                chatFrame.setVisible(true);
            } else if (uf.checkUser(name, password) == 2) {
                JOptionPane.showMessageDialog(null, "账号或密码错误或未被注册", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "用户已登录", "错误", JOptionPane.ERROR_MESSAGE);

            }
        });
        clientFrame.setSize(400, 300);
        clientFrame.setResizable(false);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientFrame.setLocationRelativeTo(null);
        clientFrame.setVisible(true);
    }

    private void showChargeview() {
        JDialog dialog = new JDialog();
        dialog.setTitle("充值");
        JLabel label = new JLabel(new ImageIcon("src/MyQQ_Final/data/receivemoney.jpg"));
        dialog.setSize(450, 600);
        dialog.add(label);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }

    private void showSignview() {
        JFrame dialog = new JFrame("注册");
        dialog.setSize(300, 200);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        JPanel panel11 = new JPanel();//姓名
        JPanel panel12 = new JPanel();//密码
        JPanel panel14 = new JPanel(new FlowLayout(FlowLayout.CENTER));//确认和取消按钮
        JButton button11 = new JButton("确认");
        JButton button12 = new JButton("取消");
        JLabel jl11 = new JLabel("姓         名");
        JLabel jl12 = new JLabel("密         码");
        JTextField jtf11 = new JTextField(16);
        JTextField jtf12 = new JTextField(16);
        Box b11 = Box.createVerticalBox();
        panel11.add(jl11);
        panel11.add(jtf11);
        panel12.add(jl12);
        panel12.add(jtf12);
        panel14.add(button11);
        panel14.add(button12);
        b11.add(panel11);
        b11.add(panel12);
        b11.add(panel14);
        button11.addActionListener(e -> {
            String name = jtf11.getText();
            String password = jtf12.getText();
            if (!password.trim().equals("") && !name.trim().equals("")) {
                uf.signUser(name, password);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "输入不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        button12.addActionListener(e -> dialog.dispose());
        dialog.add(b11);
        dialog.setVisible(true);
    }
}
