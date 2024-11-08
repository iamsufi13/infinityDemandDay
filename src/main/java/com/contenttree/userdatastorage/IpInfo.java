package com.contenttree.userdatastorage;


import lombok.Data;

@Data
public class IpInfo {
    private String city;
    private String country;
    private String region;
    private String org;
    private String postal;
    private String location;
    private String timeZone;
}
