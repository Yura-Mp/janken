package oit.is.z2395.kaizi.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JankenController {
  @GetMapping("/janken")
  public String join_game_directly() {
    return "janken.html";
  }

  @PostMapping("/janken")
  public String join_game(@RequestParam String user_name, ModelMap model) {
    model.addAttribute("user_name", user_name);

    return "janken.html";
  }
}
