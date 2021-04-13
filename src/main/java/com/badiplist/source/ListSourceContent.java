package com.badiplist.source;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.IOException;
import java.util.*;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ListSourceContent {

    private String name;
    private String content;
    private final ArrayList<BadIp> finalips = new ArrayList<>();
    private ListSource source;

    public ListSource getSource(){
        return this.source;
    }
    public void setSource(ListSource source){
        this.source = source;
    }




    private void parseConfig(String content) throws IOException{

        /// new list to return as DO of BadIps, ready for batch;

        String[] processStr;
        processStr = content.split("#", -2);
        // create a ne List Source DO so we can parse and popuplate, this function
        //sets the list of ips and the source on the return, so the callback has a JPA
        //ready object to save for source_list and an array of IPSs
        ListSource source = new ListSource();

                for (int i =0; i < processStr.length; i++){

                    if(i==2){
                        String tester = processStr[i].replace("\n", "").trim();

                        source.setListName(tester);
                        setName(tester);
                    }
                    if(processStr[i].contains("Version")){


                      source.setVersion(processStr[i].split(":")[1].replace("\n", "").trim());
                    }
                    if(processStr[i].contains("Category")){


                        source.setCategory(processStr[i].split(":")[1].replace("\n", "").trim());
                    }
                    if(processStr[i].contains("Maintainer   ")){
                        source.setHostName(processStr[i].split(":")[1].replace("\n", "").trim());
                    }
                    if(processStr[i].contains("List source URL")){
                        if (processStr[i].split(":").length>=3) {
                            String str = processStr[i].split(":")[1].trim();
                            String str2 = processStr[i].split(":")[2];
                            source.setListUrl(str + ":" + str2.replace("\n", ""));
                        }else{
                            //malformed url
                            String str = processStr[i].split(":")[1].trim();
                            source.setListUrl(str);
                        }
                    }

                }
                setSource(source);



        String subStr = processStr[processStr.length-1];

        List iplist;
        iplist = Arrays.asList(subStr.split("\\r?\\n"));

        iplist.forEach( ip -> {

            if(ip!=""){
                String listname = source.getListName();
                String ipread = ip.toString();
                String category = source.getCategory();


                BadIp badIp = new BadIp(ipread, listname, category);

                finalips.add( badIp);
            }
        });

        System.out.println("Number of IPs "+finalips.size());
        System.out.println("By List Maker "+source.getHostName());



    }

    public String getName(){
        return this.name;

    }

    public ArrayList<BadIp> getFinalips(){
        return this.finalips;

    }
    public void setContent(String content){

        this.content = content;

        try {
            parseConfig(content);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void setName(String name){
        this.name = name;

    }
    public ListSourceContent(){}


    @Override
    public String toString() {
        return "ListSourceContent{" +
                "name='" + name + '\'' +
                ", content=" + content +
                ", finalips=" + finalips.toString() +
                '}';
    }




}
