package com.badiplist.source;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class BadIp {

    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String ip;
    private String sources;
    private String catergory;

    public String getCatergory(){
        return this.catergory;
    }
    public void setCatergory(String catergory){
         this.catergory = catergory;
    }
    public String getIp(){
        return this.ip;
    }
    public String getSources(){
        return this.sources;
    }
    public String getId(){
        return this.id;
    }
    public void setIp(String ip){
        this.ip = ip;
    }
    public void setSources(String sources){
        this.sources = sources;
    }

    public BadIp(){};

    public BadIp(String ip, String sources, String catergory){

        this.ip = ip;
        this.sources = sources;
        this.catergory = catergory;

    }



}




