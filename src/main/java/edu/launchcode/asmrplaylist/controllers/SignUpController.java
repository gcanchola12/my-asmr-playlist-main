package edu.launchcode.asmrplaylist.controllers;

import edu.launchcode.asmrplaylist.backend.client.YoutubeClient;
import edu.launchcode.asmrplaylist.models.User;
import edu.launchcode.asmrplaylist.models.UserLogin;
import edu.launchcode.asmrplaylist.backend.client.YoutubeVideoIDs;
import edu.launchcode.asmrplaylist.repositories.UserDao;
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
import java.util.List;

@Controller
public class SignUpController {

    private String triggers;
    private Long userId;
    private YoutubeVideoIDs youtubeVideoIDs = new YoutubeVideoIDs();
    public List videoIds;
    private String name;

    // databases //

    @Autowired
    private UserDao userDao;

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


        if (errors.hasErrors()) {
            return "signUpPage";
        }

        videoIds = youtubeVideoIDs.getVideoIDs(triggers);

        for (Object videoId : videoIds) {
            model.addAttribute("videoId", videoId);
        }

        userDao.save(newUser);
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
        for (User user : userDao.findAll()) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                triggers = user.getTriggers();
                userId = user.getId();
                videoIds = youtubeVideoIDs.getVideoIDs(triggers);
                name = user.getName();

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

    @RequestMapping(value = "playlist")
    public String viewPlaylist(Model model) {
        model.addAttribute("playlist", videoIds);
        return "playlistPage";
    }

    // delete from playlist //

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













