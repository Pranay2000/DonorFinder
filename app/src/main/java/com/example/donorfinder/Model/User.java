package com.example.donorfinder.Model;

public class User {
    String name,bloodgroup,Id,email,idnumber,phonenumber,search,type;

    public User() {
    }

    public User(String name, String bloodgroup, String id, String email, String idnumber, String phonenumber, String search, String type) {
        this.name = name;
        this.bloodgroup = bloodgroup;
        Id = id;
        this.email = email;
        this.idnumber = idnumber;
        this.phonenumber = phonenumber;
        this.search = search;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
