package oit.is.z2395.kaizi.janken.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z2395.kaizi.janken.model.*;

import oit.is.z2395.kaizi.janken.service.AsyncKekka;

@Controller
public class JankenController {
  @Autowired
  private Entry entry = new Entry();

  @Autowired
  private UserMapper userMapper;
  @Autowired
  private MatchMapper matchMapper;
  @Autowired
  private MatchInfoMapper matchInfoMapper;

  @Autowired
  private AsyncKekka resultDisplayer;

  @GetMapping("/janken")
  public String joinGame(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("userName", loginUser);

    entry.addUser(loginUser);
    model.addAttribute("entry", this.entry);

    ArrayList<User> users = userMapper.selectAllUser();
    model.addAttribute("users", users);

    ArrayList<Match> matches = matchMapper.selectAllMatch();
    model.addAttribute("matches", matches);

    ArrayList<MatchInfo> matchinfos = matchInfoMapper.selectAllActiveMatch();
    model.addAttribute("matchinfos", matchinfos);

    return "janken.html";
  }

  @GetMapping("/play_janken/{userHand}")
  public String playGame(@PathVariable String userHand, ModelMap model) {
    String result;
    String cpuHand;

    cpuHand = Janken.handShort2Str(Janken.cpuChoiceHand(Janken.handStr2Short(userHand)));

    result = Janken.outputResult(userHand, cpuHand);

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
    model.addAttribute("opponentId", id);

    return "match.html";
  }

  @GetMapping("/fight/{userHand}")
  public String fight(@PathVariable String userHand, @RequestParam int id, Principal prin, ModelMap model) {
    int userId = userMapper.selectByName(prin.getName()).getId();
    int opponentId = id;
    User opponentUser = userMapper.selectById(opponentId);
    model.addAttribute("userName", prin.getName());
    model.addAttribute("opponentName", opponentUser.getName());
    model.addAttribute("opponentId", opponentId);

    MatchInfo activeMatch = matchInfoMapper.selectActiveMatchByUsers(opponentId, userId);
    if (activeMatch != null) {
      // アクティブな試合への対戦(受動的)
      Match match = new Match();
      match.setUser1(opponentId);
      match.setUser2(userId);
      match.setUser1Hand(activeMatch.getUser1Hand());
      match.setUser2Hand(userHand);
      match.setIsActive(true);
      matchMapper.insertMatch(match);

      matchInfoMapper.deactivateMatchById(activeMatch.getId());

    } else {
      // 新規対戦(能動的)
      MatchInfo matchinfo = new MatchInfo();
      matchinfo.setUser1(userId);
      matchinfo.setUser2(opponentId);
      matchinfo.setUser1Hand(userHand);
      matchinfo.setIsActive(true);
      matchInfoMapper.startMatch(matchinfo);
    }

    return "wait.html";
  }

  @GetMapping("/fight/waiting")
  public SseEmitter fightWaiting(Principal prin) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    int userId = userMapper.selectByName(prin.getName()).getId();

    try {
      this.resultDisplayer.waitResult(emitter, userId);
    } catch (IOException e) {

    }

    return emitter;
  }
}
