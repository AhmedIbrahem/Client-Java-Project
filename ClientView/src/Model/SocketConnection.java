/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.dao.implementation.ComponentDBOperations;
import Model.dao.implementation.TodoListDBOperations;
import Model.dao.implementation.UserDBOperations;
import Model.entities.ComponentEntity;
import Model.entities.TodoEntity;
import clientview.NotificationGUI;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dell
 */
public class SocketConnection extends Thread {

    public static boolean checkFirstTime = false;
    private static SocketConnection instance = null;
    private Socket socket;
    private PrintStream printStream;
    private boolean serverClosed = false;
    DataInputStream dataInputStream;
    Thread th;

    private SocketConnection() {
        createConnection();
    }

    public void createConnection() {

        try {
            if (socket!=null && socket.isConnected()) {
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        socket = null;
        try {
            if (socket == null) {
                socket = new Socket("192.168.43.154", 5005);
                dataInputStream = new DataInputStream(socket.getInputStream());
                printStream = new PrintStream(socket.getOutputStream());
                checkFirstTime = true;
                serverClosed = false;
                th = new Thread(this);
                th.start();
            }

        } catch (IOException e) {
            // e.printStackTrace();
            serverClosed = true;
            checkFirstTime = false;
        }
    }

    public boolean isServerClosed() {
        return serverClosed;
    }

    public static SocketConnection getInstance() {
        if (instance == null) {
            synchronized (SocketConnection.class) {
                if (instance == null) {
                    instance = new SocketConnection();
                }
            }
        }
        return instance;
    }

    public void closeSocketConnection() throws IOException {
        if (printStream != null && dataInputStream != null && socket != null) {
            printStream.println("clientClosed");
            printStream.close();
            dataInputStream.close();
            socket.close();
        }
    }

    public PrintStream getPrintStreamInstance() {
        return printStream;
    }

    @Override
    public void run() {
        System.out.println("in run");
        try {
            while (checkFirstTime) {
                String replyMsg = dataInputStream.readLine();
                if (replyMsg != null) {

                    if (replyMsg.equals("notification received")) {
                        NotificationGUI.receiveNotificationTray();
                    } else if (replyMsg.equals("Update Notification")) {
                        System.out.println("Update your ui");
                        UserDBOperations.getAllTodos(clientview.ClientView.currentUser);
                    }else if(replyMsg.contains("Item Notification")){
                        System.out.println("Item added!");
                        String todoId = replyMsg.split("\\+")[1];
                        if(clientview.ClientView.whichScreen.equals(todoId)){
                            TodoEntity todo = new TodoEntity();
                            todo.setId(Integer.parseInt(todoId));
                            TodoListDBOperations.getAllItems(todo);
                        }
                    }else if(replyMsg.contains("Task Notification")){
                        System.out.println("Task Notification");
                        String itemId = replyMsg.split("\\+")[1];
                        if(clientview.ClientView.whichScreen.equals(itemId)){
                            ComponentEntity component = new ComponentEntity();
                            component.setItemId(Integer.parseInt(itemId));
                            ComponentDBOperations.retrieveAllComponent(component);
                        }
                    }else if (replyMsg.equals("closed")) {
                        System.out.println(replyMsg);
                        serverClosed = true;
                    } else if (replyMsg.equals("opened")) {
                        serverClosed = false;
                        System.out.println("opened");
                    } else/* if (!replyMsg.equals("opened") && !replyMsg.equals("closed") && !replyMsg.equals("notification received"))*/ {
                        Handler.handle(replyMsg);
                    }
                }

            }
        } catch (IOException e) {
            serverClosed = true;

        }
        System.out.println("test Stream");
    }

}
