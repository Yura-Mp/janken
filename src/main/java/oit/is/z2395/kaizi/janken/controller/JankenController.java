package oit.is.z2395.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z2395.kaizi.janken.model.*;

@Controller
public class JankenController {
  @Autowired
  private Entry entry = new Entry();
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private MatchMapper matchMapper;

  @GetMapping("/janken")
  public String join_game(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("userName", loginUser);

    entry.addUser(loginUser);
    model.addAttribute("entry", this.entry);

    ArrayList<User> users = userMapper.selectAllUser();
    model.addAttribute("users", users);

    ArrayList<Match> matches = matchMapper.selectAllMatch();
    model.addAttribute("matches", matches);

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

  @GetMapping("/match")
  public String match(@RequestParam int id, Principal prin, ModelMap model) {
    User opponentUser = userMapper.selectById(id);
    model.addAttribute("userName", prin.getName());
    model.addAttribute("opponentName", opponentUser.getName());

    return "match.html";
  }
}
