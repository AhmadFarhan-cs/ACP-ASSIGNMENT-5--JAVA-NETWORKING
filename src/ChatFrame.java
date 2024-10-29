import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatFrame extends JFrame implements ActionListener {
    private Client client;
    private JTextArea textarea;
    private JTextField sendMsgfield;
    private JButton sendButton;
    private JTextArea activeusers;
    ChatFrame(Client client) {
        this.client=client;
        // Set up the JFrame
        setTitle("Chat");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        textarea =new JTextArea();
         textarea.setBounds(0,0,450,450);
         textarea.setEditable(false);
         this.add(textarea);
        activeusers=new JTextArea();
        activeusers.setBounds(450,0,150,450);
        activeusers.setEditable(false);
        activeusers.setText("Active Users:");
        this.add(activeusers);
         sendMsgfield=new JTextField();
         sendMsgfield.setBounds(0,475,450,50);
         sendMsgfield.addActionListener(this);
         this.add(sendMsgfield);
         sendButton= new JButton();
         sendButton.setText("Send");
         sendButton.setBounds(450,475,150,50);
         sendButton.addActionListener(this);
         this.add(sendButton);
        JScrollPane scrollPane = new JScrollPane(textarea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0,0,450,450);

        // Add the JScrollPane to the JFrame
        this.add(scrollPane);

        this.setVisible(true);

}
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==sendButton || e.getSource()==sendMsgfield){
            textarea.setText(textarea.getText()+sendMsgfield.getText()+"\n");
            client.sendmsg(sendMsgfield.getText());
            sendMsgfield.setText("");

        }

    }

    public void addText(String message){

        textarea.append(message+"\n");

    }
    public void updateActiveUsersText(String users){
        activeusers.setText("Active Users:\n"+users);
    }
}
