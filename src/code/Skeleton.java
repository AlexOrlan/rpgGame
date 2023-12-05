package code;

import java.util.HashMap;

public class Skeleton extends Thing {
    @Override
    public HashMap<String, String> doAttack() {
        int aStr = this.getAttackStr();
        HashMap<String, String> resultAttack = new HashMap<>();

        if (this.getRandomNumber(1, 100) <= this.getCritRate()) {
            aStr = (int) Math.round(aStr * 1.5);
            resultAttack.put("type", "critical");
        }
        else {
            resultAttack.put("type", "normal");
        }

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
        if (this.getRandomNumber(1, 100) <= this.getEvasion()) {
            return new StringBuffer("evaded");
        }

        if (this.getRandomNumber(1, 100) <= this.getParry()) {
            return new StringBuffer("parried");
        }

        this.setHp(this.getHp() - damage);
        if (this.getHp() < 0) {
            this.setHp(0);
        }

        return new StringBuffer(String.valueOf(this.getHp()));
    }

    public Skeleton() {
        this.setName("Скелет-гигант");
        this.initStats();
    }

    private void initStats() {
        int level = this.getRandomNumber(4, 6);

        this.setLevel(level);
        this.setHp(50 + 7 * level);
        this.setMaxhp(50 + 7 * level);
        this.setGold(35 + 1 * level + this.getRandomNumber(1, 20));
        this.setExp(10 + level);
        this.setStr(10 + level);
        this.setDex(10 + level);
        this.setCritRate(5);
        this.setParry(5);
        this.setEvasion(5);
        this.setAttackStr((int) (this.getStr() + this.getDex() / 5));
        this.setHitChance(this.getDex() * 4);
    }
}
