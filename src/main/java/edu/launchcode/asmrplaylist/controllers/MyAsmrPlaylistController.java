package edu.launchcode.asmrplaylist.controllers;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MyAsmrPlaylistController {

    private YoutubeVideoIDs youtubeVideoIDs = new YoutubeVideoIDs();
    private List<String> videoIds;
    private long userId;

    // databases //

    @Autowired
    private UserDao userDao;

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private YoutubeClient youtubeClient;

    // user registration //

    @RequestMapping(value="register", method = RequestMethod.GET)
    public String displaySignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "signUpPage";
    }

    @RequestMapping(value="register", method = RequestMethod.POST)
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

    @RequestMapping(value="login", method = RequestMethod.POST)
    public String processLogInForm(Model model, @ModelAttribute UserLogin newUserLogin, @RequestParam String userName,
                                   @RequestParam String password) {
        YoutubeVideoIDs youtubeVideoIDs = new YoutubeVideoIDs();

        for (User user : userDao.findAll()) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                String triggers = user.getTriggers();
                videoIds = youtubeVideoIDs.getVideoIDs(triggers);
                String name = user.getName();

                for (Object videoId : videoIds) {
                    model.addAttribute("videoId", videoId);
                    model.addAttribute("user", "Welcome back, " + name);
                    return "playPage";
                }
            }
        }

        model.addAttribute("errorMessage", "Invalid Username or Password");
        return "loginPage";
    }

    // view playlist //

//    @RequestMapping(value = "playlist")
//    public String viewPlaylist(Model model) {
//
//        for (Video video : videoDao.findAll()) {
//            if ()
//
//        }
//
//        model.addAttribute("playlist", videoIds);
//        return "playlistPage";
//    }

//    // view playlist //
//
//    @RequestMapping(value = "playlist/{id}")
//    public String viewPlaylist(Model model, @PathVariable Long id) {
//
//        model.addAttribute("user", userDao.findById(id));
////        model.addAttribute("playlist", videoIds);
//        return "playlistPage";
//    }

    // delete from playlist //

//    @RequestMapping(value="remove/{videoId}", method = RequestMethod.GET)
//    public String displayRemovePage(Model model, @PathVariable String videoId) {
//        video.setVideoId(videoId);
//        videoDao.delete(video);
//        model.addAttribute("playlist", video);
//        return "redirect:/playlist";
//    }

//    @RequestMapping(value = "remove", method = RequestMethod.GET)
//    public String displayRemoveVideoForm(Model model) {
//        model.addAttribute("cheeses", .findAll());
//        model.addAttribute("title", "Remove Cheese");
//        return "cheese/remove";
//    }
//
//    @RequestMapping(value = "remove", method = RequestMethod.POST)
//    public String processRemoveVideoForm(@RequestParam int[] cheeseIds) {
//
//        for (int cheeseId : cheeseIds) {
//            cheeseDao.delete(cheeseId);
//        }
//
//        return "redirect:";
//    }

}













