package ro.siit.finalProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This is a controller for home
 */
@Controller
public class HomeController {
    @GetMapping("/")
    public String homePage(Model model) {
        return "home";
    }

}
