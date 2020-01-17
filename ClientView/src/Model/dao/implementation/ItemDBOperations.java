/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.dao.implementation;

import Model.GsonParser;
import Model.Handler;
import Model.RequestEntity;
import Model.entities.ItemEntity;
import java.util.ArrayList;
import Model.entities.AssignFriendTodoEntity;
import Model.entities.UserEntity;
import clientview.ClientView;
import clientview.ItemCollaboratorsXMLController;
import clientview.TodoFormXMLController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author dell
 */
public class ItemDBOperations {

    public static void addItem(ItemEntity item) {
        ArrayList<ItemEntity> list = new ArrayList<>();
        list.add(item);
        RequestEntity<ItemEntity> addRequest = new RequestEntity("ItemDBOperations", "addItem", list);
        System.out.println(GsonParser.parseToJson(addRequest));
        Handler.sendRequestToServer(addRequest);
    }
       
    public void addItemResponse(ArrayList<Object> arrayObjects) {
        if (arrayObjects != null) {
            //to connect by Controller of ui
            System.out.println("Item Added  successfully");
        } else {
            System.out.println("Item not added successfully");
        }
    }

    public static void updateItem(ItemEntity itemToUpdate) {
        ArrayList<ItemEntity> list = new ArrayList<>();
        list.add(itemToUpdate);
        RequestEntity<ItemEntity> updateRequest = new RequestEntity("ItemDBOperations", "updateItem", list);
        Handler.sendRequestToServer(updateRequest);
    }

    public void updateItemResponse(ArrayList<Object> arrayObjects) {
        if (arrayObjects != null) {
            //to connect by Controller of ui
            
            System.out.println("Item Updated  successfully");
        } else {
            System.out.println("Item not updated successfully");
        }
    }
    
    public static void assignItem(ArrayList<AssignFriendTodoEntity> assignedFriendList){
        RequestEntity<AssignFriendTodoEntity> request = new RequestEntity("ItemDBOperations", "assignItem", assignedFriendList);
        Handler.sendRequestToServer(request);
    }
    
    public void assignItemResponse(ArrayList<Object>isAssigned){
        if (isAssigned != null) {
            //to connect by Controller of ui
            System.out.println("Item assigned  successfully");
        } else {
            System.out.println("Item not assigned successfully");
        }
    }
    
    public static void deleteItem(ItemEntity itemToUpdate) {
        ArrayList<ItemEntity> list = new ArrayList<>();
        list.add(itemToUpdate);
        RequestEntity<ItemEntity> updateRequest = new RequestEntity("ItemDBOperations", "deleteItem", list);
        Handler.sendRequestToServer(updateRequest);
    }

    public void deleteItemResponse(ArrayList<Object> arrayObjects) {
        if (arrayObjects != null) {
                TodoListDBOperations.getAllItems(TodoFormXMLController.todo);
                System.out.println("Item deleted  successfully");
        } else {
            System.out.println("Item not deleted successfully");
        }
    }
    
     public static void getItemCollaborators(ItemEntity itemToUpdate) {
        ArrayList<ItemEntity> list = new ArrayList<>();
        list.add(itemToUpdate);
        RequestEntity<ItemEntity> updateRequest = new RequestEntity("ItemDBOperations", "getItemCollaborators", list);
        Handler.sendRequestToServer(updateRequest);
    }

    public void getItemCollaboratorsResonse(ArrayList<UserEntity> collabotarors) {
        //clear
        // TodoFormXMLController.clearTest();
        if (collabotarors == null || collabotarors.size() == 0) {
            System.out.println("no user in this item");
        } else {
            System.out.println("\nCollaborators Size: " + collabotarors.size());
            for (int i = 0; i < collabotarors.size(); i++) {
                System.out.println("Colllaborators for this items are\n" + collabotarors.get(i).getUsername());
            }
            ItemCollaboratorsXMLController.setCollaboratorsList(collabotarors);
            
            Platform.runLater(new Runnable() {

            @Override
            public void run() {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/clientview/ItemCollaboratorsXML.fxml"));
                Stage stage = new Stage(StageStyle.DECORATED);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(UserDBOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            });
        }
    }
}
