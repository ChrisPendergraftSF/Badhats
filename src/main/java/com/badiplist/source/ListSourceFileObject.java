package com.badiplist.source;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ListSourceFileObject {

    private String name;
    private String url;


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public ListSourceFileObject() {
    }
    public void ListSourceFileObject(String name, String url) {
        this.name = name;
        this.url = url;
    }
    @Override
    public String toString() {
        return "ListSourceFileObject{" +
                "name='" + name + '\'' +
                ", url=" + url +
                '}';
    }

}