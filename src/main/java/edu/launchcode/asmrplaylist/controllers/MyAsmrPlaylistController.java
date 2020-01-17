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
                                    @RequestParam String name, @RequestParam String[] triggersList) {


        YoutubeVideoIDs youtubeVideoIDs = new YoutubeVideoIDs();
        List<Video> playlist = new ArrayList<>();
        String triggers = StringUtils.join(triggersList, " ");
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
        Long userId = newUser.getId();
        model.addAttribute("user", "Welcome, " + name);
        return "redirect:home/" + userId; // change this to a redirect: home + userId //
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

        for (User user : userDao.findAll()) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                List<Video> playlist = user.getPlaylist();
                String name = user.getName();
                Long userId = user.getId();

                for (Video video : playlist) {
                    model.addAttribute("videoId", video.getVideoId());
                    model.addAttribute("user", "Welcome, " + name);
                    System.out.println(userId);
                    return "redirect:home/" + userId; // change this to a redirect: home + userId //
                }
            }
        }


        model.addAttribute("errorMessage", "Invalid Username or Password");
        return "loginPage";
    }

    // Homepage //

    @RequestMapping(value = "home/{userId}") // add a path variable with userId//
    public String displayHomepage(Model model, @PathVariable Long userId) {

        for (User user : userDao.findAll()) {
            if (user.getId() == userId) {
                List<Video> playlist = user.getPlaylist();
                String name = user.getName();
                model.addAttribute("userId", userId);

                for (Video video : playlist) {
                    model.addAttribute("videoId", video.getVideoId());
                    model.addAttribute("user", "Hi, " + name);
                }
            }
        }

        return "playPage"; // change this to a redirect: home + userId //
    }

//     view playlist //

    @RequestMapping(value = "playlist/{userId}")
    public String viewPlaylist(Model model, @PathVariable Long userId) {

        System.out.println(userId);

        List<String> videoIds = new ArrayList<>();
        List<Video> playlist = new ArrayList<>();
        String name = new String();

        // adjust this to grab the userId from the url or from query

        for (User user : userDao.findAll()) {
            if (user.getId() == userId) {
                System.out.println("something");
                playlist = user.getPlaylist();
                name = user.getName();
            }
        }

        for (Video video : playlist) {
            String videoId = video.getVideoId();
            videoIds.add(videoId);
        }

        model.addAttribute("user", "Hi, " + name);
        model.addAttribute("playlist", videoIds);
        model.addAttribute("userId", userId);
        return "playlistPage";
    }

    // create a query for videoId
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
