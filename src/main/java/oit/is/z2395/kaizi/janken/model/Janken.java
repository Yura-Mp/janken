package oit.is.z2395.kaizi.janken.model;

public class Janken {
  /**
   * <概要> String型で表されたじゃんけんの手をshort型に変換する静的メンバ関数.
   *
   * @param str String, 文字列型で表されたじゃんけんの手
   * @return short, 変換表(switch-case文)に沿って変換されたshort型のじゃんけんの手を返す.
   */
  public static short str_to_short(String str) {
    short res;
    switch (str) {
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
}
