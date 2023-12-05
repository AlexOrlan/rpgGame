package code;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Player extends Thing {
    private int freeStatPoints;
    private final int maxLevel = 10;
    private final int statPerLevel = 2;
    private int potions;
    private final int maxPotions = 5;
    private char gender = 'm';
    private boolean isDefeated = false;
    private LinkedHashMap<String, Boolean> perks = new LinkedHashMap<>();
    private final LinkedHashMap<Integer, Integer> expTable = new LinkedHashMap<>();

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getFreeStatPoints() {
        return freeStatPoints;
    }

    public void setFreeStatPoints(int freeStatPoints) {
        this.freeStatPoints = freeStatPoints;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getExpToLevel(int level) {
        return this.expTable.get(level);
    }

    public void usePotion() {
        if (this.getPotions() > 0 && this.getHp() != this.getMaxhp()) {
            this.setHp(this.getMaxhp());
            this.setPotions(this.getPotions() - 1);
        }
    }

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
        if (this.getRandomNumber(100, 200) <= this.getEvasion() + 100) {
            return new StringBuffer("evaded");
        }

        if (this.getRandomNumber(100, 200) <= this.getParry() + 100) {
            return new StringBuffer("parried");
        }

        this.setHp(this.getHp() - damage);
        if (this.getHp() < 0) {
            this.setHp(0);
        }

        return new StringBuffer(String.valueOf(this.getHp()));
    }

    public String combatResults(Thing monster) {
        String results = "";

        if (this.getHp() > 0) {
            results += "\r\nПолучено\r\nЗолото - " + monster.getGold();
            results += "\r\nОпыт - " + monster.getExp();

            this.setGold(this.getGold() + monster.getGold());
            this.setExp(this.getExp() + monster.getExp());

            if (this.getExp() >= this.getExpToLevel(this.getLevel()) && this.getLevel() != this.getMaxLevel()) {
                this.setExp(this.getExp() - this.getExpToLevel(this.getLevel()));
                this.setLevel(this.getLevel() + 1);

                if (this.getLevel() == this.getMaxLevel()) {
                    this.setExp(0);
                }

                this.setFreeStatPoints(this.getFreeStatPoints() + 2);
                this.setMaxhp(this.getMaxhp() + 10);
                this.setHp(this.getMaxhp());
            }
        }
        else {
            this.setDefeated(true);
        }

        return results;
    }

    public Player() {
        this.setHp(50);
        this.setMaxhp(50);
        this.setGold(100);
        this.setExp(0);
        this.setStr(10);
        this.setDex(10);
        this.setFreeStatPoints(5);
        this.setLevel(1);
        this.setCritRate(5);
        this.setParry(10);
        this.setEvasion(10);
        this.setGender('m');
        this.setPotions(3);

        this.expTable.put(1, 20);
        this.expTable.put(2, 50);
        this.expTable.put(3, 100);
        this.expTable.put(4, 210);
        this.expTable.put(5, 300);
        this.expTable.put(6, 390);
        this.expTable.put(7, 500);
        this.expTable.put(8, 650);
        this.expTable.put(9, 800);
        this.expTable.put(10, 0);

        recalcStats();
    }

    public void buyHealPotion(int cost) {
        if (this.getPotions() < this.getMaxPotions() && this.getGold() >= cost) {
            this.setPotions(this.getPotions() + 1);
            this.setGold(this.getGold() - cost);
        }
    }

    public void buyPerk(String perkName, int cost) {
        if (!this.isPerkOn(perkName) && this.getGold() >= cost) {
            this.addPerk(perkName);
            this.setGold(this.getGold() - cost);

            switch (perkName) {
                case "endur1_potion":
                    this.setMaxhp(this.getMaxhp() + 10);
                    this.setHp(this.getMaxhp());
                    break;
                case "endur2_potion":
                    this.setMaxhp(this.getMaxhp() + 15);
                    this.setHp(this.getMaxhp());
                    break;
                case "endur3_potion":
                    this.setMaxhp(this.getMaxhp() + 25);
                    this.setHp(this.getMaxhp());
                    break;

                case "free_attributes1":
                    this.setFreeStatPoints(this.getFreeStatPoints() + 1);
                    break;
                case "free_attributes2":
                    this.setFreeStatPoints(this.getFreeStatPoints() + 2);
                    break;
                case "free_attributes3":
                    this.setFreeStatPoints(this.getFreeStatPoints() + 3);
                    break;
                case "free_attributes4":
                    this.setFreeStatPoints(this.getFreeStatPoints() + 5);
                    break;

                case "critical_rate1":
                    this.setCritRate(this.getCritRate() + 3);
                    break;
                case "critical_rate2":
                    this.setCritRate(this.getCritRate() + 5);
                    break;
                case "critical_rate3":
                    this.setCritRate(this.getCritRate() + 7);
                    break;
                case "critical_rate4":
                    this.setCritRate(this.getCritRate() + 10);
                    break;

                case "parry1":
                    this.setParry(this.getParry() + 5);
                    break;
                case "parry2":
                    this.setParry(this.getParry() + 7);
                    break;
                case "parry3":
                    this.setParry(this.getParry() + 11);
                    break;
                case "parry4":
                    this.setParry(this.getParry() + 17);
                    break;

                case "evade1":
                    this.setEvasion(this.getEvasion() + 5);
                    break;
                case "evade2":
                    this.setEvasion(this.getEvasion() + 7);
                    break;
                case "evade3":
                    this.setEvasion(this.getEvasion() + 11);
                    break;
                case "evade4":
                    this.setEvasion(this.getEvasion() + 17);
                    break;
            }
        }
    }

    public void changeStat(String statName, int value) {
        switch (statName) {
            case "Сила":
                this.setStr(this.getStr() + value);
                break;
            case "Ловкость":
                this.setDex(this.getDex() + value);
                break;
        }

        recalcStats();
    }

    private void recalcStats() {
        this.setAttackStr((int) (this.getStr() * 1.5 + this.getDex() / 5 + this.getLevel()));
        this.setHitChance(this.getDex() * 5);
    }

    public char getGender() {
        return gender;
    }

    public int getPotions() {
        return potions;
    }

    public void setPotions(int potions) {
        this.potions = potions;
    }

    public int getMaxPotions() {
        return maxPotions;
    }

    public boolean isDefeated() {
        return this.isDefeated;
    }

    public void setDefeated(boolean defeated) {
        this.isDefeated = defeated;
    }

    public void addPerk(String perkName) {
        this.perks.put(perkName, true);
    }

    public boolean isPerkOn(String perkName) {
        return this.perks.get(perkName) == null ? false : this.perks.get(perkName);
    }
}