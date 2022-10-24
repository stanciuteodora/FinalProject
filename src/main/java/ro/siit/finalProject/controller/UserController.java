package ro.siit.finalProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.siit.finalProject.model.User;
import ro.siit.finalProject.repository.JpaUserRepository;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("users")
public class UserController {

    @Autowired
    public JpaUserRepository jpaUserRepository;

    @GetMapping("/{id}")
    public String getUserById(Model model, @PathVariable("id") UUID userId){
        Optional<User> user = jpaUserRepository.findById(userId);
        model.addAttribute("gugumumu", user.get());
        return "/user/user";
    }
}
