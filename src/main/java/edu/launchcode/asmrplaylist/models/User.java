package edu.launchcode.asmrplaylist.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany
    @JoinColumn(name="user_id")
    private List<Video> playlist = new ArrayList<>();

    @NotNull
    @Size(min = 3, max = 15)
    private String name;

    @NotNull
    @Size(min = 3, max = 15)
    private String userName;

    @NotNull
    @Size(min = 3, max = 15)
    private String password;

    @NotNull
    @Size(min = 3, max = 15)

    @NotNull
    private String triggers;

    public User() { }

    public User(long id, String name, String userName, String password, String triggers) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.triggers = triggers;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTriggers() {
        return triggers;
    }

    public void setTriggers(String triggers) {
        this.triggers = triggers;
    }

    public List<Video> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<Video> playlist) {
        this.playlist = playlist;
    }
}