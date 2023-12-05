package code;

import java.util.HashMap;

public class Goblin extends Thing {
    @Override
    public HashMap<String, String> doAttack() {
        int aStr = this.getAttackStr();
        HashMap<String, String> resultAttack = new HashMap<>();
        resultAttack.put("type", "normal");

        if (this.getRandomNumber(1, 100) <= this.getHitChance()) {
            resultAttack.put("damage", String.valueOf(this.getRandomNumber(aStr - (int) Math.round(aStr * 0.2), aStr + (int) Math.round(aStr * 0.2))));
            return resultAttack;
        }
        else {
            resultAttack.put("type", "miss");
            resultAttack.put("damage", "0");
            return resultAttack;
        }
    }

    @Override
    public StringBuffer takeDamage(int damage) {
        this.setHp(this.getHp() - damage);
        if (this.getHp() < 0) {
            this.setHp(0);
        }

        return new StringBuffer(String.valueOf(this.getHp()));
    }

    public Goblin() {
        this.setName("Гоблин-мутант");
        this.initStats();
    }

    private void initStats() {
        int level = this.getRandomNumber(1, 3);

        this.setLevel(level);
        this.setHp(20 + 5 * level);
        this.setMaxhp(20 + 5 * level);
        this.setGold(10 + 1 * level + this.getRandomNumber(1, 10));
        this.setExp(5 + level);
        this.setStr(5 + level);
        this.setDex(5 + level);
        this.setCritRate(0);
        this.setParry(0);
        this.setEvasion(0);
        this.setAttackStr((int) (this.getStr() + this.getDex() / 5 + this.getLevel()));
        this.setHitChance(this.getDex() * 4);
    }
}
