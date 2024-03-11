package ayonglay.web.todo.viewcontroller;

import ayonglay.web.todo.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {


    @GetMapping(path = "/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping(path = {"/home", "/"})
    public ModelAndView home() {
        return new ModelAndView("home");
    }

    @GetMapping(path = "/profile")
    public ModelAndView profile() {
        return new ModelAndView("profile");
    }
}
