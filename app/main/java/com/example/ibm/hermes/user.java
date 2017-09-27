package com.example.ibm.hermes;

/**
 * Created by ibrahim on 9/22/2017.
 */
public class user {
    private String id;
    private String name;
    private String query;

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getName() {
        return this.name;
    }

    public String setName(String name) {
        this.name = name;
        return name;
    }

    public String getId() {
        return this.id;
    }

    public String setId(String id) {
        this.id = id;
        return id;
    }


}
