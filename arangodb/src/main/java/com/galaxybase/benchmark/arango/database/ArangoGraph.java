package com.galaxybase.benchmark.arango.database;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.galaxybase.benchmark.common.database.DataBase;
import com.galaxybase.benchmark.common.util.TestConfiguration;

/**
 * @Author chenyanglin
 * @Date 2021/1/18 11:11
 * @Version 1.0
 */
public class ArangoGraph implements DataBase<ArangoDatabase> {

    private ArangoDatabase db;

    @Override
    public ArangoDatabase getDataBase() {
        return db;
    }

    @Override
    public void initDataBase(TestConfiguration conf) {
        String ip = "" + conf.get("ip");
        int port = Integer.parseInt("" + conf.get("port"));
        String username = "" + conf.get("username");
        String password = "" + conf.get("password");
        db = new ArangoDB.Builder()
                .host(ip, port)
                .user(username)
                .password(password)
                .build().db();
    }

    @Override
    public void close() {
    }

}
