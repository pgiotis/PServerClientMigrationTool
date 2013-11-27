/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.pserverclientmigrationtool.service;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.scify.pserverclientmigrationtool.migration.Migration;

/**
 *
 * @author Panagiotis Giotis <giotis.p@gmail.com>
 */
@WebService(serviceName = "MigrationService")
public class MigrationService {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "MigrateClient")
    public String MigrateClient(@WebParam(name = "FromPSUrl") String FromPSUrl, @WebParam(name = "FromPSPort") String FromPSPort, @WebParam(name = "ToPSUrl") String ToPSUrl, @WebParam(name = "ToPSPort") String ToPSPort, @WebParam(name = "ClientUsername") String ClientUsername, @WebParam(name = "ClientPassword") String ClientPassword, @WebParam(name = "APUsername") String APUsername, @WebParam(name = "APPassword") String APPassword) {

        Migration ClientMigrate = new Migration(FromPSUrl, FromPSPort, ToPSUrl, ToPSPort, ClientUsername, ClientPassword, APUsername, APPassword);

        return ClientMigrate.MigrateClient();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "MigrateAttributes")
    public String MigrateAttributes(@WebParam(name = "FromPSUrl") String FromPSUrl, @WebParam(name = "FromPSPort") String FromPSPort, @WebParam(name = "ToPSUrl") String ToPSUrl, @WebParam(name = "ToPSPort") String ToPSPort, @WebParam(name = "ClientUsername") String ClientUsername, @WebParam(name = "ClientPassword") String ClientPassword, @WebParam(name = "APUsername") String APUsername, @WebParam(name = "APPassword") String APPassword) {

        Migration ClientMigrate = new Migration(FromPSUrl, FromPSPort, ToPSUrl, ToPSPort, ClientUsername, ClientPassword, APUsername, APPassword);

        return ClientMigrate.MigrateAttributes();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "MigrateFeatures")
    public String MigrateFeatures(@WebParam(name = "FromPSUrl") String FromPSUrl, @WebParam(name = "FromPSPort") String FromPSPort, @WebParam(name = "ToPSUrl") String ToPSUrl, @WebParam(name = "ToPSPort") String ToPSPort, @WebParam(name = "ClientUsername") String ClientUsername, @WebParam(name = "ClientPassword") String ClientPassword, @WebParam(name = "APUsername") String APUsername, @WebParam(name = "APPassword") String APPassword) {

        Migration ClientMigrate = new Migration(FromPSUrl, FromPSPort, ToPSUrl, ToPSPort, ClientUsername, ClientPassword, APUsername, APPassword);

        return ClientMigrate.MigrateFeatures();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "MigrateUsers")
    public String MigrateUsers(@WebParam(name = "FromPSUrl") String FromPSUrl, @WebParam(name = "FromPSPort") String FromPSPort, @WebParam(name = "ToPSUrl") String ToPSUrl, @WebParam(name = "ToPSPort") String ToPSPort, @WebParam(name = "ClientUsername") String ClientUsername, @WebParam(name = "ClientPassword") String ClientPassword, @WebParam(name = "APUsername") String APUsername, @WebParam(name = "APPassword") String APPassword) {

        Migration ClientMigrate = new Migration(FromPSUrl, FromPSPort, ToPSUrl, ToPSPort, ClientUsername, ClientPassword, APUsername, APPassword);

        return ClientMigrate.MigrateUsers();
    }
}
