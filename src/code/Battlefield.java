package code;

import java.nio.charset.Charset;
import java.util.HashMap;

public class Battlefield {
    private Thing monster;
    private Player player;
    private volatile boolean proceed = true;
    private StringBuffer battleLog;
    private String lastString;

    public Battlefield(Thing monster, Player player) {
        this.monster = monster;
        this.player = player;
    }

    public void doFight() {
        this.setProceed(true);
        this.setBattleLog(new StringBuffer());

        int attackOrder = monster.getRandomNumber(1 , 2);
        HashMap<String, String> attackPerformed;
        int attackValue = 0;
        StringBuffer takeDamage;

        battleLog.append("Сражение начинается!\r\n");
        this.lastString = "Сражение начинается!";
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        while (this.isProceed()) {
            if (attackOrder == 1) {
                attackPerformed = monster.doAttack();
                attackValue = Integer.parseInt(attackPerformed.get("damage"));

                battleLog.append("[Монстр] ");
                battleLog.append(monster.getName());
                if (attackValue > 0) {
                    takeDamage = player.takeDamage(attackValue);
                    battleLog.append(" атакует. ").append(player.getName());

                    if (takeDamage.toString().equals("evaded")) {
                        battleLog.append(" уворачивается.");
                    } else if (takeDamage.toString().equals("parried")) {
                        battleLog.append(" парирует.");
                    } else {
                        battleLog.append(" получает урон ").append(attackValue).append(".");

                        if (attackPerformed.get("type").equals("critical")) {
                            battleLog.append(" Критический урон!");
                        }

                        if (takeDamage.toString().equals("0")) {
                            setProceed(false);
                        }
                    }
                }
                else {
                    battleLog.append(" промахивается.");
                }
            }

            if (attackOrder == 2) {
                attackPerformed = player.doAttack();
                attackValue = Integer.parseInt(attackPerformed.get("damage"));

                battleLog.append("[Герой] ");
                battleLog.append(player.getName());
                if (attackValue > 0) {
                    takeDamage = monster.takeDamage(attackValue);
                    battleLog.append(" атакует. ").append(monster.getName());

                    if (takeDamage.toString().equals("evaded")) {
                        battleLog.append(" уворачивается.");
                    } else if (takeDamage.toString().equals("parried")) {
                        battleLog.append(" парирует.");
                    } else {
                        battleLog.append(" получает урон ").append(attackValue).append(".");

                        if (attackPerformed.get("type").equals("critical")) {
                            battleLog.append(" Критический урон!");
                        }

                        if (takeDamage.toString().equals("0")) {
                            setProceed(false);
                        }
                    }
                }
                else {
                    battleLog.append(" промахивается.");
                }

            }

            attackOrder = attackOrder == 1 ? 2 : 1;
            String[] logByString = battleLog.toString().split("\r\n");
            this.lastString += logByString[logByString.length - 1];
            battleLog.append("\r\n");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (monster.getHp() == 0) {
            battleLog.append("Победа!\r\n");
        }
        if (player.getHp() == 0) {
            battleLog.append("Поражение!\r\n\r\nКонец игры...");
        }
    }

    public boolean isProceed() {
        return proceed;
    }

    public void setProceed(boolean proceed) {
        this.proceed = proceed;
    }

    public StringBuffer getBattleLog() {
        return battleLog;
    }

    public void setBattleLog(StringBuffer battleLog) {
        this.battleLog = battleLog;
    }

    public String getLastString() {
        return lastString;
    }

    public void setLastString(String lastString) {
        this.lastString = lastString;
    }
}
