/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.pserverclientmigrationtool.migration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.scify.pserverclientmigrationtool.communicator.PSClientRequest;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
public class Migration {

    private InetAddress FromPSAddress;
    private int FromPSPort;

    private InetAddress ToPSAddress;
    private int ToPSPort;

    private String ClientUsername;
    private String ClientPassword;
    private String clientCrendetials;

    private String APUsername;
    private String APPassword;

    private boolean post = true;
    private int timeout = 1000;

    private String requestPS;

    public Migration(String FromPSUrl, String FromPSPort, String ToPSUrl,
            String ToPSPort, String ClientUsername, String ClientPassword,
            String APUsername, String APPassword) {
        try {
            FromPSAddress = InetAddress.getByName(FromPSUrl);
            this.FromPSPort = Integer.parseInt(FromPSPort);

            ToPSAddress = InetAddress.getByName(ToPSUrl);
            this.ToPSPort = Integer.parseInt(ToPSPort);

            this.ClientUsername = ClientUsername;
            this.ClientPassword = ClientPassword;
            this.APUsername = APUsername;
            this.APPassword = APPassword;
            clientCrendetials = ClientUsername + "|" + ClientPassword;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Migration.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String MigrateClient() {

        //Add new client in ToPServer
        createClient();

        return "Done";
    }

    public String MigrateAttributes() {

        feedAttributes(getAttributes());

        return "Done";
    }

    public String MigrateFeatures() {
        feedFeatures(getFeatures());
        return "Done";
    }

    public String MigrateUsers() {
        ArrayList<String> Users = getUsers();
        //for each user on ToPServer we feed it on FromPServer
        for (String cUser : Users) {
            //feed first users Attributes
            feedUserAttributes(cUser);
            //feed second users Features
            feedUserFeatures(cUser);
        }
        return "Done";
    }

    /**
     * Create a new Client into the ToPServer
     */
    private void createClient() {
        //Add new client in ToPServer
        requestPS = "/admin?login_name=" + APUsername + "&login_password=" + APPassword + "&com=addClnt&name=" + ClientUsername + "&password=" + ClientPassword;
        PSClientRequest req = new PSClientRequest(ToPSAddress, ToPSPort, requestPS, post, timeout);
    }

    /**
     * Get attributes from FromPServer
     *
     * @return A HashMap with key--> attribute name and value --> default value
     */
    private ArrayList<String> getUsers() {
        //get attributes FromPServer
        ArrayList<String> Users = new ArrayList<String>();
        requestPS = "/1.0/personal/" + clientCrendetials + "/users.xml";
        PSClientRequest req = new PSClientRequest(FromPSAddress, FromPSPort, requestPS, post, timeout);
        for (int i = 0; i < req.getRows(); i++) {
            Users.add(req.getValue(i, 0));
        }
        return Users;
    }

    /**
     * Get attributes from FromPServer
     *
     * @return A HashMap with key--> attribute name and value --> default value
     */
    private HashMap<String, String> getAttributes() {
        //get attributes FromPServer

        HashMap<String, String> Attributes = new HashMap<String, String>();
        requestPS = "/1.0/personal/" + clientCrendetials + "/attributes.xml?attributesPattern=*";
        PSClientRequest req = new PSClientRequest(FromPSAddress, FromPSPort, requestPS, post, timeout);
        for (int i = 0; i < req.getRows(); i++) {
            Attributes.put(req.getValue(i, 0), req.getValue(i, 1));
        }

        return Attributes;
    }

    /**
     * Feed attribute list in ToPServer
     *
     * @param Attributes A HashMap with all attributes
     */
    private void feedAttributes(HashMap<String, String> Attributes) {
        //feedAttributes ToPSerer
        for (String tmpAttr : Attributes.keySet()) {
            requestPS = "/1.0/personal/" + clientCrendetials + "/add_attributes.xml?attributes={\"" + tmpAttr + "\":\"" + Attributes.get(tmpAttr) + "\"}";
            PSClientRequest req = new PSClientRequest(ToPSAddress, ToPSPort, requestPS, post, timeout);
        }
    }

    /**
     * Get features from FromPServer
     *
     * @return A HashMap with key--> feature name and value --> default value
     */
    private HashMap<String, String> getFeatures() {
        //get features FromPServer
        HashMap<String, String> Features = new HashMap<String, String>();
        requestPS = "/1.0/personal/" + clientCrendetials + "/features.xml?featuresPattern=*";
        PSClientRequest req = new PSClientRequest(FromPSAddress, FromPSPort, requestPS, post, timeout);
        for (int i = 0; i < req.getRows(); i++) {
            Features.put(req.getValue(i, 0), req.getValue(i, 1));
        }

        return Features;
    }

    /**
     * Feed features list in ToPServer
     *
     * @param Features A HashMap with all features
     */
    private void feedFeatures(HashMap<String, String> Features) {
        //feedFeatures ToPSerer
        String allfeatures = "";
        PSClientRequest req;
        int count = 0;
        //feed users Features to ToPSerer
        for (String cftr : Features.keySet()) {
            count++;

            if (allfeatures.isEmpty()) {
                allfeatures = "\"" + cftr + "\":\"" + Features.get(cftr) + "\"";
            } else {
                allfeatures = allfeatures + ",\"" + cftr + "\":\"" + Features.get(cftr) + "\"";
            }

            if (count % 20 == 0) {
                requestPS = "/1.0/personal/" + clientCrendetials + "/add_features.xml?features={" + allfeatures + "}";
                allfeatures = "";
                req = new PSClientRequest(ToPSAddress, ToPSPort, requestPS, post, timeout);
            }

        }
        if (!allfeatures.isEmpty()) {
            requestPS = "/1.0/personal/" + clientCrendetials + "/add_features.xml?features={" + allfeatures + "}";
            req = new PSClientRequest(ToPSAddress, ToPSPort, requestPS, post, timeout);
        }
    }

    /**
     * Feed users Attributes from FromPServer to ToPServer
     *
     * @param Username The current username
     */
    private void feedUserAttributes(String Username) {
        // get users Attributes from ToPServer
        requestPS = "/1.0/personal/" + clientCrendetials + "/users_attributes.xml?username=" + Username;
        PSClientRequest req = new PSClientRequest(FromPSAddress, FromPSPort, requestPS, post, timeout);
        String useratrributes = "";
        //feed users Attributes to ToPSerer
        for (int i = 0; i < req.getRows(); i++) {
            if (useratrributes.isEmpty()) {
                useratrributes = "\"" + req.getValue(i, 0) + "\":\"" + req.getValue(i, 1) + "\"";
            } else {
                useratrributes = useratrributes + ",\"" + req.getValue(i, 0) + "\":\"" + req.getValue(i, 1) + "\"";
            }
        }
        requestPS = "/1.0/personal/" + clientCrendetials + "/post_user.xml?username=" + Username + "&attr={" + useratrributes + "}";
        req = new PSClientRequest(ToPSAddress, ToPSPort, requestPS, post, timeout);

    }

    /**
     * Feed users Features from FromPServer to ToPServer
     *
     * @param Username The current username
     */
    private void feedUserFeatures(String Username) {
        // get users Features from ToPServer
        requestPS = "/1.0/personal/" + clientCrendetials + "/users_features.xml?username=" + Username;
        PSClientRequest req = new PSClientRequest(FromPSAddress, FromPSPort, requestPS, post, timeout);
        String userfeatures = "";
        //feed users Features to ToPSerer
        for (int i = 0; i < req.getRows(); i++) {
            if (Integer.parseInt(req.getValue(i, 1)) == 0) {
                break;
            }
            if (userfeatures.isEmpty()) {
                userfeatures = "\"" + req.getValue(i, 0) + "\":\"" + req.getValue(i, 1) + "\"";
            } else {
                userfeatures = userfeatures + ",\"" + req.getValue(i, 0) + "\":\"" + req.getValue(i, 1) + "\"";
            }

            if (i % 20 == 0) {
                requestPS = "/1.0/personal/" + clientCrendetials + "/increase_users_values.xml?username=" + Username + "&featurelist={" + userfeatures + "}";
                userfeatures = "";
                PSClientRequest ReqAddUsr = new PSClientRequest(ToPSAddress, ToPSPort, requestPS, post, timeout);
            }
        }
        if (!userfeatures.isEmpty()) {
            requestPS = "/1.0/personal/" + clientCrendetials + "/increase_users_values.xml?username=" + Username + "&featurelist={" + userfeatures + "}";
            req = new PSClientRequest(ToPSAddress, ToPSPort, requestPS, post, timeout);
        }
    }

}
