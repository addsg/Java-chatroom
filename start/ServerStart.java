package MyQQ_Final.start;

import MyQQ_Final.server.ServerView;

import javax.swing.*;
import java.awt.*;


public class ServerStart {
    public static void main(String[] args) {
        JFrame serverFrame = new JFrame();
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton serverStart = new JButton("启动");
        JButton serverEnd = new JButton("关闭");
        serverStart.addActionListener(e -> {
            serverFrame.dispose();
            new ServerView();
        });
        serverEnd.addActionListener(e -> serverFrame.dispose());
        JPanel serverPanel = new JPanel();
        serverPanel.add(serverStart);
        serverPanel.add(serverEnd);
        serverFrame.add(serverPanel, BorderLayout.CENTER);
        serverFrame.setSize(400, 300);
        serverFrame.setLocationRelativeTo(null);
        serverFrame.setVisible(true);
    }
}
