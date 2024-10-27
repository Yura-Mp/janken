package oit.is.z2395.kaizi.janken.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z2395.kaizi.janken.model.*;

@Service
public class AsyncKekka {
  private final Logger logger = LoggerFactory.getLogger(AsyncKekka.class);

  @Autowired
  private MatchMapper matchMapper;

  @Async
  public void waitResult(SseEmitter emitter, int userId) throws IOException {
    logger.info("AsyncKekka.waitResult");
    try {
      Match match;

      while (true) {
        match = matchMapper.selectActiveMatchByUser(userId);
        logger.info("Match Checking: " + (match != null));

        if (match != null) {
          String userHand = match.getUser1Hand();
          String opponentHand = match.getUser2Hand();
          if (match.getUser1() != userId) {
            String tmp = userHand;
            userHand = opponentHand;
            opponentHand = tmp;
          }

          emitter.send(
              String.format(
                  """
                        <h2>対戦結果</h2>
                        <div>あなたの手 %s</div>
                        <div>相手の手 %s</div>
                        <div>結果 %s</div>

                        <p><a href="/janken">もどる</a></p>
                      """,
                  userHand, opponentHand,
                  Janken.outputResult(userHand, opponentHand)));

          TimeUnit.MILLISECONDS.sleep(1000);
          matchMapper.deactivateMatchById(match.getId());

          break;
        }

        TimeUnit.MILLISECONDS.sleep(500);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
