package oit.is.z2395.kaizi.janken.model;

import java.util.Random;

public class Janken {
  /**
   * <概要> String型で表されたじゃんけんの手をshort型に変換する静的メンバ関数.
   *
   * @param hand String, String型で表されたじゃんけんの手
   * @return short, 変換表(switch-case文)に沿って変換されたshort型のじゃんけんの手を返す.
   */
  public static short hand_str_to_short(String hand) {
    short res;
    switch (hand) {
      case "Gu":
        res = 0;
        break;

      case "Choki":
        res = 1;
        break;

      case "Pa":
        res = 2;
        break;

      default:
        res = -1;
        break;
    }

    return res;
  }

  /**
   * <概要> short型で表されたじゃんけんの手をString型に変換する静的メンバ関数.
   *
   * @param hand short, short型で表されたじゃんけんの手
   * @return String, 変換表(switch-case文)に沿って変換されたString型のじゃんけんの手を返す.
   */
  public static String hand_short_to_str(short hand) {
    String res;
    switch (hand) {
      case 0:
        res = "Gu";
        break;

      case 1:
        res = "Choki";
        break;

      case 2:
        res = "Pa";
        break;

      default:
        res = "Error";
        break;
    }

    return res;
  }

  /**
   * <概要> short型で表された双方のじゃんけんの手から, 自分自身の勝敗を判定する.
   *
   * @param myHand   short, 自分自身のじゃんけんの手
   * @param yourHand short, 相手のじゃんけんの手
   * @return short, 自分自身がこのじゃんけんに勝ったか(1), 引き分けたか(0), 負けたか(-1)を返す.
   */
  public static short judge(short myHand, short yourHand) {
    if (myHand + 1 == yourHand || myHand + 1 == yourHand + 3)
      return 1;
    if (myHand == yourHand)
      return 0;
    else
      return -1;
  }

  /**
   * <概要> プレイヤーの手をもとに、CPUが出す手を決定する.
   * 勝敗率(プレイヤー視点)の偏りを設定できる.
   *
   * <備考> デフォルトの設定では, プレイヤーが大変負けやすくしている.
   *
   * @param myHand short, プレイヤーのじゃんけんの手
   * @return short, CPUが出すじゃんけんの手
   */
  public static short cpu_choice_hand(short myHand) {
    return cpu_choice_hand(myHand, 10, 20, 70);
  }

  public static short cpu_choice_hand(short myHand, int winRate, int drawRate, int loseRate) {
    Random rnd = new Random();
    int samp = rnd.nextInt(winRate + drawRate + loseRate);
    short playRes = 0;
    short cpuHand = 0;

    if (0 <= samp && samp < winRate) {
      playRes = 1;
      samp = -1;
    } else {
      samp -= winRate;
    }

    if (0 <= samp && samp < drawRate) {
      playRes = 0;
      samp = -1;
    } else {
      samp -= drawRate;
    }

    if (0 <= samp && samp < loseRate) {
      playRes = -1;
      samp = -1;
    } else {
      samp -= loseRate;
    }

    for (short hand = 0; hand < 3; hand++) {
      if (Janken.judge(myHand, hand) == playRes) {
        cpuHand = hand;
        break;
      }
    }

    return cpuHand;
  }
}
