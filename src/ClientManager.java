import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientManager extends Thread {
    private String userName;
    private String passWord;
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    ClientManager(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;

        try {
            // Initialize data streams
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Errors creating connections");
        }
    }

    // Method to send message to this client
    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            boolean loggedin = false;
            String message;

            // Handle initial login or registration
            if (loggedin == false) {
                int choice = in.readInt();

                if (choice == JOptionPane.YES_OPTION) {
                    // New user registration
                    userName = in.readUTF();
                    passWord = in.readUTF();
                    System.out.println(userName + passWord);

                    while (!server.validUsername(userName)) {
                        out.writeUTF("Username taken.");
                        userName = in.readUTF();
                        passWord = in.readUTF();
                    }
                    out.writeUTF("Registration successful welcome " + userName);
                    server.addUser(userName, passWord); // Register user
                    out.writeUTF("1"); // Login success signal to client
                } else if(choice==JOptionPane.NO_OPTION) {
                    // Existing user login
                    userName = in.readUTF();
                    passWord = in.readUTF();

                    while (!server.userExists(userName, passWord)) {
                        out.writeUTF("Invalid username or password.");
                        userName = in.readUTF();
                        passWord = in.readUTF();
                    }
                    out.writeUTF("Login successful welcome " + userName);
                    out.writeUTF("1");
                }
                else {
                    in.close();
                }

                loggedin = true;
                out.writeUTF("You have joined the chat");
                server.broadcast(userName + " has joined the chat", this); // Notify others
                server.updateactiveusers(); // Update active users
            }

            // Main message-handling loop
            while ((message = in.readUTF()) != null) {
                System.out.println("Received: " + message);
                message = userName + " :" + message;
                server.broadcast(message, this); // Broadcast to all clients
            }
        } catch (IOException e) {
            System.out.println(userName + " has disconnected");

            if (userName != null) {
                server.broadcast(userName + " has left the chat", this); // Notify others
            }
        } finally {
            try {
                // Clean up resources when client disconnects
                server.removeClient(this);
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing connection");
            }
        }
    }

    public String getUsername() {
        return userName;
    }
}