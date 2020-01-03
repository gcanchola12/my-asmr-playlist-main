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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;



@Controller
public class MyAsmrPlaylistController {

    private Long userId;

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

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String processSignUpForm(@NotNull Model model, @ModelAttribute @Valid User newUser, Errors errors,
                                    @RequestParam String name, @RequestParam String triggers) {

        YoutubeVideoIDs youtubeVideoIDs = new YoutubeVideoIDs();
        List<Video> playlist = new ArrayList<>();
        List<String> videoIds = youtubeVideoIDs.getVideoIDs(triggers);

        if (errors.hasErrors()) {
            return "signUpPage";
        }

        for (String videoId : videoIds) {
            Video video = new Video();
            video.setVideoId(videoId);
            playlist.add(video);
            videoDao.save(video);
            model.addAttribute("videoId", videoId);
        }

        newUser.setPlaylist(playlist);
        userDao.save(newUser);
        userId = newUser.getId();
        model.addAttribute("user", "Welcome, " + name);
        return "playPage";
    }

    // User LogIn //

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String displayLogInForm(Model model) {
        model.addAttribute("userLogin", new UserLogin());
        return "loginPage";
    }

    @RequestMapping(value = "loggedIn", method = RequestMethod.POST)
    public String processLogInForm(Model model, @ModelAttribute UserLogin newUserLogin, @RequestParam String userName,
                                   @RequestParam String password) {

        for (User user : userDao.findAll()) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                List<Video>playlist = user.getPlaylist();
                String name = user.getName();
                userId = user.getId();

                for (Video video : playlist) {
                    model.addAttribute("videoId", video.getVideoId());
                    model.addAttribute("user", "Welcome back, " + name);
                    return "playPage";
                }
            }
        }

        model.addAttribute("errorMessage", "Invalid Username or Password");
        return "loginPage";
    }

    //TODO: create a homepage //

    // view playlist //

    @RequestMapping(value = "playlist")
    public String viewPlaylist(Model model) {

        List<String> videoIds = new ArrayList<>();
        List<Video> playlist = new ArrayList<>();

        for (User user : userDao.findAll()) {
            if (user.getId() == userId) {
                playlist = user.getPlaylist();
            }
        }

        for (Video video : playlist) {
            String videoId = video.getVideoId();
            videoIds.add(videoId);
        }

        model.addAttribute("playlist", videoIds);
        return "playlistPage";
    }

    @RequestMapping(value = "remove/{videoId}", method = RequestMethod.GET)
    public String displayRemoveVideoForm(Model model, @PathVariable String videoId) {

        for (User user : userDao.findAll()) {
            if (user.getId() == userId) {
                for (Video video : user.getPlaylist()) {
                    if (video.getVideoId().equals(videoId)) {
                        videoDao.delete(video);

                    }
                }
            }
        }

        return "redirect:/playlist";
    }

}













