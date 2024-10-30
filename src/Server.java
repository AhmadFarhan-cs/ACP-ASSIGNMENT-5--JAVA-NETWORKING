import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

private ServerSocket serverSocket;
private List<ClientManager> clients;
private File usernames;
private File passwords;

 private static int numofclients;

    // Server constructor that sets up the server on a specific port
Server(int port){
    clients = new ArrayList<>();//initialize list of clients
    try {

    serverSocket = new ServerSocket(port);//create server socket
        try {
            //initialize the files
            usernames = new File("users.txt");
            passwords = new File("passwords.txt");
            //create the files if they don't exist
            if(usernames.createNewFile()){
                System.out.println("usernames file doesnt exist, one will be created.");

            }
            if (passwords.createNewFile()){
                System.out.println("passwords file doesnt exist, one will be created.");
            }
        }catch (IOException e){
            System.out.println("Error in creating file");
        }




}catch (IOException e){
    System.out.println("Error in creating server");
    }

}
//method that will wait for a client and add them to the list of connected clients
void waitforclient(){
    try {


    System.out.println("Waiting for a client ...");
    Socket clientsocket=new Socket();
    clientsocket= serverSocket.accept();
    ClientManager client= new ClientManager(this,clientsocket);
    new Thread(client).start();
    clients.add(client);
    numofclients++;
    System.out.println("Client accepted");
    System.out.println("Active clients: "+numofclients);


}catch (IOException e){
    System.out.println("Error in accepting client");
    }
}

//method that will send a msg to every client except the sender
    public synchronized void broadcast(String message,ClientManager sender){
        for (ClientManager client : clients){
            if(client !=sender)
                client.sendMessage(message);
        }
    }

//method to remove a client on disconnect
    public void removeClient(ClientManager client){
        clients.remove(client);
        numofclients--;
        System.out.println("Active clients: "+numofclients);
        updateactiveusers();

    }
//method that will check if a username exists or not
public boolean validUsername(String username){

    try {
       Scanner usernameescanner = new Scanner(usernames);
        String comparename;
        while (usernameescanner.hasNextLine()){
            comparename = usernameescanner.nextLine();
            if(username.equals( comparename))
                return false;
    }} catch (FileNotFoundException e) {
        throw new RuntimeException(e);

    }
    return true;


    }

//method that will check if the user exists
public boolean userExists(String username,String password){
    try {
        Scanner usernameescanner = new Scanner(usernames);
        Scanner passwordscanner = new Scanner(passwords);
        String comparename;
        String comparepass;
        while (usernameescanner.hasNextLine()){
            comparename = usernameescanner.nextLine();
            comparepass = passwordscanner.nextLine();
            if(username.equals( comparename) && password.equals(comparepass))
                return true;

        }
        return false;
}catch (IOException e){
        System.out.println("error retrieving username");
    return false;
    }
}
//method that will store new username and password to the files
public void addUser(String username,String password){
    try {
        FileWriter userWriter = new FileWriter(usernames,true);
        FileWriter passWriter = new FileWriter(passwords,true);
        userWriter.write(username+"\n");
        passWriter.write(password+"\n");
        userWriter.close();
        passWriter.close();
    } catch (IOException e) {
        System.out.println("Error writing to file");
    }

}
//method that will send a message of the username of every active user
public void updateactiveusers(){
    String users="";
    for(ClientManager client :clients){//get usernames from every client and append it to the string
        users+=client.getUsername()+"\n";
    }
    for(ClientManager client :clients){//send the string to every client
        client.sendMessage("1");
        client.sendMessage(users);
    }


}
    public static void main(String args[]){
        Server server = new Server(5000);



while (true) {

    server.waitforclient();
}




    }


}
