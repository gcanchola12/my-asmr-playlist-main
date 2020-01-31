package edu.launchcode.asmrplaylist.controllers;

import com.google.api.services.youtube.model.Playlist;
import edu.launchcode.asmrplaylist.backend.client.YoutubeClient;
import edu.launchcode.asmrplaylist.backend.client.YoutubeVideoIDs;
import edu.launchcode.asmrplaylist.models.User;
import edu.launchcode.asmrplaylist.models.UserLogin;
import edu.launchcode.asmrplaylist.models.Video;
import edu.launchcode.asmrplaylist.repositories.UserDao;
import edu.launchcode.asmrplaylist.repositories.VideoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;



@Controller
public class MyAsmrPlaylistController {

    //TODO need to work on getting rid of this variable and including it within each method.

    // keeping variable here because of issues with finding the user when clicking nav links. The id was not passing into variables
    // correctly.

    Long userId;

    // databases //

    @Autowired
    private UserDao userDao;

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private YoutubeClient youtubeClient;

    // user registration //

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String displaySignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "signUpPage";
    }

    // TODO find out if I still need to save only video objects in the playlist or if I can just store the strings pulled from getVideoIds()

    @RequestMapping(value = "registered", method = RequestMethod.POST)
    public String processSignUpForm(@NotNull Model model, @ModelAttribute @Valid User newUser, Errors errors,
                                    @RequestParam String name, @RequestParam String[] triggersList) {

        YoutubeVideoIDs youtubeVideoIDs = new YoutubeVideoIDs(); // Create the object that connects to YouTube API
        List<Video> playlist = new ArrayList<>();
        String triggers = StringUtils.join(triggersList, " "); // convert list of strings into a single string so that it can be used in the function
        List<String> videoIds = youtubeVideoIDs.getVideoIDs(triggers); // pulls the video Ids as strings

        if (errors.hasErrors()) {
            return "signUpPage";
        }

        // TODO add code to delete null videoIds //

        // set each video Id to a video object and save the video in a playlist that will be associated with the user.
        for (String videoId : videoIds) {
            Video video = new Video();
            video.setVideoId(videoId);
            playlist.add(video);
            videoDao.save(video);
            model.addAttribute("videoId", videoId);
        }

        newUser.setPlaylist(playlist);
        userDao.save(newUser);
        userId = newUser.getId(); // TODO make this local
        model.addAttribute("user", "Welcome, " + name);
        model.addAttribute("userId", userId); // TODO change to getUserId()
        return "playPage";
    }

    // TODO: create an additional trigger form so I can hard code in the search term ASMR //

    // User LogIn //

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String displayLogInForm(Model model) {
        model.addAttribute("userLogin", new UserLogin());
        return "loginPage";
    }

    @RequestMapping(value = "loggedIn", method = RequestMethod.POST)
    public String processLogInForm(Model model, @ModelAttribute UserLogin newUserLogin, @RequestParam String userName,
                                   @RequestParam String password) {

        // look into the DAO to find the matching user info

        for (User user : userDao.findAll()) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {

                List<Video> playlist = user.getPlaylist();
                userId = user.getId(); // TODO make this local

                // loop through the playlist and convert each video to a string again and display each video one at a time

                for (Video video : playlist) {
                    model.addAttribute("videoId", video.getVideoId());
                    model.addAttribute("user", "Welcome, " + user.getName());
                    model.addAttribute("userId", user.getId());
                    System.out.println(userId); // ignore
                    return "playPage";
                }
            }
        }


        model.addAttribute("errorMessage", "Invalid Username or Password");
        return "loginPage";
    }

    // Homepage //

    // when the user clicks the 'home' link in the nav, it will display here. The home link contains the userId which is pulled in
    // and used to verify the user is a user

    @RequestMapping(value = "home")
    public String displayHomepage(Model model, @RequestParam Long userId) {

        Object user1 = userDao.findById(userId); // this identifies the user in the userDao
        System.out.println(user1); // ignore
        System.out.println("it worked!"); // ignore

        // TODO remove loop as user already found above

        for (User user : userDao.findAll()) {
            if (user.getId() == userId) {
                List<Video> playlist = user.getPlaylist();
                model.addAttribute("userId", userId);
                model.addAttribute("user", "Hi, " + user.getName());

                for (Video video : playlist) {
                    model.addAttribute("videoId", video.getVideoId());
                }
            }
        }

        return "playPage";
    }

//     view playlist //

    // when user clicks on playlist, the userId is added to the url so it can be passed easily to the removed url

    @RequestMapping(value = "playlist/{userId}")
    public String viewPlaylist(Model model, @PathVariable Long userId) {

        Object user1 = userDao.findById(userId);
        System.out.println(user1);

        ArrayList<String> playlist = new ArrayList<>(); // create a playlist that can be looped in the view

        for (User user : userDao.findAll()) {

            if (user.getId() == userId) {
                System.out.println("sql found me");
                model.addAttribute("user", "Hi, " + user.getName());
                model.addAttribute("userId", userId);

                // I am looping through the user's playlist of videos and converting them into a string and storing them into a
                // new playlist again

                // TODO remove loop, create a youtubeVIdeoId object and just call in list of videoId strings

                for (Video video : user.getPlaylist()) {
                    String videoId = video.getVideoId();
                    playlist.add(videoId);
                }
            }
        }

        model.addAttribute("playlist", playlist);
        return "playlistPage";
    }

    // Here I grab the video Id so that I can find it in the DAO and delete it. -----> this is what is creating the null values in SQL.
    // TODO delete entire row and delete not just the videoId

    @RequestMapping(value = "playlist/{userId}/remove/{videoId}", method = RequestMethod.GET)
    public String displayRemoveVideoForm(Model model, @PathVariable Long userId, @PathVariable String videoId) {

        for (User user : userDao.findAll()) {
            if (user.getId() == userId) {

                for (Video video : user.getPlaylist()) {
                    if (video.getVideoId().equals(videoId)) {
                        videoDao.delete(video);
                    }
                }
            }
        }
        return "redirect:/playlist/" + userId;
    }

    //TODO: figure out how to add a video //
    // TODO: add functionality to create a new playlist //
    // TODO: refactor the template names
//
}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//