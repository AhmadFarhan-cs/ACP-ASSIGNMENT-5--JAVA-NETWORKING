import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginFrame extends JFrame implements ActionListener{
    private Client client;
    private JTextField usernameField;
    private JLabel userLabel;
    private JTextField passwordField;
    private JLabel passwordLabel;
    private JButton loginButton;


    LoginFrame(Client client){
        this.client=client;
        // Set up the JFrame
        setTitle("Login");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        userLabel =new JLabel("Enter user name:");
        userLabel.setBounds(0,100,150,50);
        this.add(userLabel);
        usernameField = new JTextField();
        usernameField.setBounds(150,100,200,50);
        usernameField.setToolTipText("Enter username");
        usernameField.addActionListener(this);
        this.add(usernameField);
        passwordLabel =new JLabel("Enter password:");
        passwordLabel.setBounds(0,150,150,50);
        this.add(passwordLabel);
        passwordField = new JTextField();
        passwordField.setBounds(150,150,200,50);
        passwordField.setToolTipText("Enter password");
        passwordField.addActionListener(this);
        this.add(passwordField);
        loginButton = new JButton();
        loginButton.setBounds(0,300,150,50);
        loginButton.setText("Login");
        loginButton.addActionListener(this);
        this.add(loginButton);

        setFocusable(true);
        this.setVisible(true); // Make the frame visible


    }



    public void actionPerformed(ActionEvent e){
        if(e.getSource() ==loginButton ||e.getSource()==usernameField||e.getSource()==passwordField){
        client.sendmsg(usernameField.getText());
        client.sendmsg(passwordField.getText());
        }
    }

    public void handlelogin(){
        client.createchatframe();
        this.dispose();
    }

}
