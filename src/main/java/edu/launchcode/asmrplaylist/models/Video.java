package edu.launchcode.asmrplaylist.models;

import javax.persistence.*;

@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    @ManyToOne()
    private User user;

    private String videoId;

    public Video() {}

    public Video(Integer id, User user, String videoId) {
        this.Id = id;
        this.user = user;
        this.videoId = videoId;
    }

    public Integer getId() { return Id; }

    public void setId(Integer id) { this.Id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getVideoId() { return videoId; }

    public void setVideoId(String videoId) { this.videoId = videoId;}
}
