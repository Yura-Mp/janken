package oit.is.z2395.kaizi.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z2395.kaizi.janken.model.Janken;

@Controller
public class JankenController {
  @GetMapping("/janken")
  public String join_game_directly() {
    return "janken.html";
  }

  @PostMapping("/janken")
  public String join_game(@RequestParam String userName, ModelMap model) {
    model.addAttribute("userName", userName);

    return "janken.html";
  }

  @GetMapping("/play_janken/{userHand}")
  public String play_game(@PathVariable String userHand, ModelMap model) {
    String result;
    String cpuHand;

    cpuHand = Janken.hand_short_to_str(Janken.cpu_choice_hand(Janken.hand_str_to_short(userHand)));

    switch (Janken.judge(Janken.hand_str_to_short(userHand), Janken.hand_str_to_short(cpuHand))) {
      case 1:
        result = "You Win!";
        break;

      case 0:
        result = "Draw.";
        break;

      case -1:
        result = "You Lose.";
        break;

      default:
        result = "Occurred Undefined Error.";
        break;
    }

    model.addAttribute("userHand", userHand);
    model.addAttribute("opponentHand", cpuHand);
    model.addAttribute("jankenResult", result);

    return "janken.html";
  }
}
