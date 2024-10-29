import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client {
    // Initialize socket and input/output streams
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;
    private DataInputStream in= null;
    private LoginFrame loginFrame;
    private ChatFrame chatFrame;

    // Constructor to establish a connection to the server
    public Client(String address, int port) {
        try {
            // Attempt to connect to the server
            socket = new Socket(address, port);
            System.out.println("Connected");

            input = new DataInputStream(System.in);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (UnknownHostException u) {
            System.out.println(u);
            return;
        } catch (IOException i) {
            System.out.println(i);
            return;
        }

        try {
            // Start a new thread to continuously read incoming messages
            new Thread(new ReadMessages()).start();
            int choice = JOptionPane.showConfirmDialog(null, "Are you a new user?");
            out.writeInt(choice); // Send choice to server (new or existing user)
            if(choice != JOptionPane.CANCEL_OPTION)
                loginFrame = new LoginFrame(this); // Open login frame
        } catch (IOException e) {
            System.out.println("Error in logging in ");
        }
    }

    // Inner class to handle reading messages in a separate thread
    private class ReadMessages implements Runnable {
        @Override
        public void run() {
            try {
                boolean loggedin = false;
                boolean updatingusers = false;
                String message;

                // Continuously read messages from server
                while ((message = in.readUTF()) != null) {
                    if (!loggedin) {
                        if (message.equals("1")) {
                            loginFrame.handlelogin(); // Successful login
                            loggedin = true;
                        } else {
                            JOptionPane.showMessageDialog(null, message); // Display error
                        }
                    } else {
                        // Handle updating users and receiving messages
                        if (message.equals("1")) {
                            updatingusers = true;
                        } else if (updatingusers == true) {
                            chatFrame.updateActiveUsersText(message);
                            updatingusers = false;
                        } else if (updatingusers == false) {
                            chatFrame.addText(message); // Add message to chat window
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection closed: " + e.getMessage());
            }
        }
    }

    // Method to send a message from input stream to server
    public void sendmsg() {
        try {
            String message = input.readLine();
            out.writeUTF(message);
        } catch (IOException e) {
            chatFrame.addText("Error message could not be sent");
        }
    }

    // Overloaded method to send a specific message to server
    public void sendmsg(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            System.out.println("Error sending msg to server");
        }
    }

    // Method to create the chat frame once user has logged in
    public void createchatframe() {
        chatFrame = new ChatFrame(this);
    }

    public static void main(String args[]) {
        Client client = new Client("127.0.0.1", 5000);
    }
}