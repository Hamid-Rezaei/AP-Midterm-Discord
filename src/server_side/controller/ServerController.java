package server_side.controller;


import client_side.model.User;

import static database.Database.retrieveFromDB;

public class ServerController {

    public static User getUser(){
        return  retrieveFromDB();
    }
}
