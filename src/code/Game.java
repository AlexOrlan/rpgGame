package code;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;

public class Game {
    private final JFrame window = new JFrame();
    HashMap componentMap;
    private static final String fontName = "San serif";

    private final ImageIcon startBgImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/start_screen_bg.png")));
    private final String gameName = "Простая РПГ";
    private final int width = 1024;
    private final int height = 1024;

    private final Player player = new Player();
    private final Font normalFont = new Font(fontName, Font.PLAIN, 20);
    private final Font statFont = new Font(fontName, Font.PLAIN, 16);
    private JTextField freeStatPointsField;
    private final JPanel charStatsPanel = new JPanel();
    private final JLabel playerStatInformer = new JLabel();
    private final JPanel inventory = new JPanel();
    private final JLabel monsterInformer = new JLabel();
    private final JPanel shopPanel = new JPanel();
    private boolean isActionPossible = true;
    private final JTextArea battleLogOutput = new JTextArea();
    private final JScrollPane battleLogScroll = new JScrollPane(battleLogOutput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);;
    Battlefield battlefield;


    private Thing monster;

    private final Map worldMap = new Map();

    public Game() {
    }

    public void init() {
        window.setTitle(gameName);
        window.setLayout(null);
        window.getContentPane().setPreferredSize(new Dimension(width, height));
        window.pack();
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        startScreen();
    }

    public void startScreen() {
        ToolTipManager.sharedInstance().setInitialDelay(10);

        OutlineLabel titleNameLabel = new OutlineLabel(gameName, JLabel.CENTER, 1);
        titleNameLabel.setFont(new Font(fontName, Font.BOLD, 48));
        titleNameLabel.setOutlineColor(Color.white);
        titleNameLabel.setForeground(Color.black);
        titleNameLabel.setOpaque(false);
        titleNameLabel.setBounds(0,480, width, 120);
        window.add(titleNameLabel, BorderLayout.CENTER);

        ActionListener buttonAction = e -> {
            String commandFromButton = e.getActionCommand();

            if (commandFromButton.equals("createCharacterScreen")) {
                createCharacter();
            }
        };

        JButton startButton = new JButton("Старт");
        startButton.setLayout(new BorderLayout());
        startButton.setBounds(width/2 - 150/2,600,150, 40);
        startButton.setFont(normalFont);
        startButton.setFocusPainted(false);
        startButton.addActionListener(buttonAction);
        startButton.setActionCommand("createCharacterScreen");
        window.add(startButton);

        JLabel startBgLabel = new JLabel(startBgImage);
        startBgLabel.setSize(width, height);
        window.add(startBgLabel);
        window.getContentPane().setComponentZOrder(startBgLabel, 2);
        window.getContentPane().setComponentZOrder(titleNameLabel, 1);
        window.getContentPane().setComponentZOrder(startButton, 1);
    }

    public void createCharacter() {
        showCharacter(true);
    }

    public void showCharacter(boolean isCreateMode) {
        window.getContentPane().removeAll();
        window.repaint();

        ImageIcon avatarMale = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/knight_male31.png")));
        ImageIcon avatarFemale = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/knight_female31.png")));
        JLabel avatarFullImage = new JLabel();

        if (!isCreateMode && player.getGender() == 'm') {
            avatarFullImage.setIcon(avatarMale);
        } else if (!isCreateMode && player.getGender() == 'f') {
            avatarFullImage.setIcon(avatarFemale);
        } else {
            avatarFullImage.setIcon(avatarMale);
        }

        avatarFullImage.setBounds(341, 0, 683, 1024);
        window.add(avatarFullImage);

        JLabel textForInputName = new JLabel("Имя персонажа");
        textForInputName.setFont(normalFont);
        textForInputName.setBounds(50, 50, 200, 30);
        textForInputName.setForeground(Color.BLACK);
        window.add(textForInputName);

        JTextField textField = new JTextField(20);
        if (isCreateMode) {
            textField.setBounds(50, 80, 200, 30);
            textField.setFont(normalFont);
            textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            window.add(textField);
        }
        else {
            JLabel playerName = new JLabel(player.getName());
            playerName.setBounds(50, 80, 200, 30);
            playerName.setFont(normalFont);
            window.add(playerName);
        }

        ActionListener buttonAction = e -> {
            String commandFromButton = e.getActionCommand();

            if (!isActionPossible) {
                return;
            }

            switch (commandFromButton) {
                case "male":
                    avatarFullImage.setIcon(avatarMale);
                    player.setGender('m');
                    break;

                case "female":
                    avatarFullImage.setIcon(avatarFemale);
                    player.setGender('f');
                    break;

                case "start":
                    if (!textField.getText().isEmpty()) {
                        player.setName(textField.getText());
                        startGame();
                    }
                    else {
                        textField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                    }
                    break;

                case "close_char_screen":
                    changeLocation(worldMap.getCurrentLocation());
                    break;

                case "use_potion":
                    usePotion();
                    break;
            }
        };

        if (isCreateMode) {
            JLabel genderText = new JLabel("Пол");
            genderText.setFont(normalFont);
            genderText.setBounds(50, 130, 200, 30);
            window.add(genderText);

            JRadioButton maleGender = new JRadioButton("М");
            maleGender.setActionCommand("male");
            JRadioButton femaleGender = new JRadioButton("Ж");
            femaleGender.setActionCommand("female");
            ButtonGroup genderGroup = new ButtonGroup();
            genderGroup.add(maleGender);
            genderGroup.add(femaleGender);

            maleGender.addActionListener(buttonAction);
            femaleGender.addActionListener(buttonAction);
            maleGender.setBounds(100, 133, 50, 25);
            femaleGender.setBounds(150,133,50,25);
            maleGender.setFont(normalFont);
            femaleGender.setFont(normalFont);
            maleGender.setFocusPainted(false);
            femaleGender.setFocusPainted(false);
            maleGender.setSelected(true);
            window.add(maleGender);
            window.add(femaleGender);
        }


        JLabel statText = new JLabel("Атрибуты");
        statText.setFont(normalFont);
        statText.setBounds(50, 180, 200, 30);
        statText.setForeground(Color.BLACK);
        window.add(statText);

        JTextField strField = new JTextField(2);
        JPanel strP = drawStatRow("Сила", player.getStr(), 50, 220, strField);
        window.add(strP);
        JTextField dexField = new JTextField(2);
        JPanel dexP = drawStatRow("Ловкость", player.getDex(), 50, 240, dexField);
        window.add(dexP);

        JLabel freeStatPointsLabelText = new JLabel("Свободные", SwingConstants.RIGHT);
        freeStatPointsLabelText.setFont(statFont);
        freeStatPointsLabelText.setBounds(50, 260, 100, 20);
        window.add(freeStatPointsLabelText);

        freeStatPointsField = new JTextField(String.valueOf(player.getFreeStatPoints()), SwingConstants.CENTER);
        freeStatPointsField.setBounds(155, 261, 20, 20);
        freeStatPointsField.setFont(statFont);
        freeStatPointsField.setEditable(false);
        freeStatPointsField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        freeStatPointsField.setHorizontalAlignment(JTextField.CENTER);
        window.add(freeStatPointsField);

        printCharStats();

        if (!isCreateMode) {
            showCharInventory();
        }

        if (isCreateMode) {
            JButton createButton = new JButton("Начать игру");
            createButton.setBounds(50, 550, 300, 30);
            createButton.setFont(normalFont);
            createButton.setFocusPainted(false);
            createButton.setActionCommand("start");
            createButton.addActionListener(buttonAction);
            window.add(createButton);
        }
        else {
            JButton potionButton = new JButton("Использовать зелье");
            potionButton.setBounds(50, 924, 300, 30);
            potionButton.setFont(normalFont);
            potionButton.setFocusPainted(false);
            potionButton.setActionCommand("use_potion");
            potionButton.addActionListener(buttonAction);
            window.add(potionButton);

            JButton closeCharScreen = new JButton("Закрыть");
            closeCharScreen.setBounds(50, 964, 300, 30);
            closeCharScreen.setFont(normalFont);
            closeCharScreen.setFocusPainted(false);
            closeCharScreen.setActionCommand("close_char_screen");
            closeCharScreen.addActionListener(buttonAction);
            window.add(closeCharScreen);
        }
    }

    private void showCharInventory() {
        window.getContentPane().remove(inventory);
        inventory.removeAll();
        inventory.revalidate();
        inventory.repaint();
        inventory.setBounds(50, 550, 200, 300);

        JLabel inventoryTitle = new JLabel("Инвентарь");
        inventoryTitle.setFont(normalFont);
        inventoryTitle.setBounds(0, 0, 200, 30);
        inventoryTitle.setForeground(Color.BLACK);
        inventory.add(inventoryTitle);

        JLabel potionsLabel = new JLabel("Зелья исцеления | " + player.getPotions() + " / " + player.getMaxPotions(), SwingConstants.RIGHT);
        potionsLabel.setFont(statFont);
        potionsLabel.setBounds(0, 30, 200, 30);
        potionsLabel.setForeground(Color.BLACK);
        inventory.add(potionsLabel);

        window.add(inventory);
        window.repaint();
    }

    private void usePotion() {
        player.usePotion();
        showCharInventory();
        printCharStats();
    }

    private void printCharStats() {
        int startY = 300;
        int statPanelHeight = 0;
        window.remove(charStatsPanel);
        charStatsPanel.removeAll();

        JLabel strText = new JLabel("Характеристики");
        strText.setFont(normalFont);
        strText.setBounds(0, 0, 200, 30);
        strText.setForeground(Color.BLACK);
        charStatsPanel.add(strText);
        statPanelHeight += 30;

        LinkedHashMap<String, String> charStats = new LinkedHashMap<>();
        charStats.put("Уровень", String.valueOf(player.getLevel()));
        charStats.put("Опыт", (player.getLevel() == player.getMaxLevel() ? "предел" : player.getExp() + " / " + player.getExpToLevel(player.getLevel())));
        charStats.put("Очки здоровья", player.getHp() + " / " + player.getMaxhp());
        charStats.put("Сила атаки", String.valueOf(player.getAttackStr()));
        charStats.put("Шанс попадания", String.valueOf(player.getHitChance()));
        charStats.put("Крит шанс", String.valueOf(player.getCritRate()));
        charStats.put("Парирование", String.valueOf(player.getParry()));
        charStats.put("Уклонение", String.valueOf(player.getEvasion()));

        Set<String> keys = charStats.keySet();
        int startYstatStr = 30;
        for (String statName : keys) {
            JLabel statTextStr = new JLabel(statName, SwingConstants.RIGHT);
            statTextStr.setFont(statFont);
            statTextStr.setBounds(0, startYstatStr, 150, 20);
            statTextStr.setForeground(Color.BLACK);
            charStatsPanel.add(statTextStr);

            JTextField inputField = new JTextField(1);
            if (statName.equals("Опыт") || statName.equals("Очки здоровья")) {
                inputField.setBounds(155, startYstatStr + 1, 100, 20);
            }
            else {
                inputField.setBounds(155, startYstatStr + 1, 40, 20);
            }
            inputField.setFont(statFont);
            inputField.setText(charStats.get(statName));
            inputField.setEditable(false);
            inputField.setBorder(BorderFactory.createCompoundBorder(
                  BorderFactory.createMatteBorder(0,1,0,1, Color.BLACK),
                  BorderFactory.createEmptyBorder(0, 5, 0, 5)));
            inputField.setHorizontalAlignment(JTextField.CENTER);
            charStatsPanel.add(inputField);

            startYstatStr += 25;
            statPanelHeight += 25;
        }

        charStatsPanel.setBounds(50, startY, 300, statPanelHeight);
        window.add(charStatsPanel);
    }

    private JPanel drawStatRow(String statName, int initValue, int x, int y, JTextField inputField) {
        JPanel statRow = new JPanel();
        statRow.setBounds(x, y, 250, 22);

        JLabel statTextStr = new JLabel(statName, SwingConstants.RIGHT);
        statTextStr.setFont(statFont);
        statTextStr.setBounds(0, 0, 100, 20);
        statTextStr.setForeground(Color.BLACK);
        statRow.add(statTextStr);

        inputField.setBounds(105, 1,20,20);
        inputField.setFont(statFont);
        inputField.setText(String.valueOf(initValue));
        inputField.setEditable(false);
        inputField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        statRow.add(inputField);

        ActionListener statButtons = e -> {
            String commandFromButton = e.getActionCommand();
            int currentValue = Integer.parseInt(inputField.getText());

            switch (commandFromButton) {
                case "minus": {
                    if (currentValue != initValue) {
                        inputField.setText(String.valueOf(currentValue - 1));
                        player.setFreeStatPoints(player.getFreeStatPoints() + 1);
                        freeStatPointsField.setText(String.valueOf(player.getFreeStatPoints()));
                        player.changeStat(statName, -1);
                        printCharStats();
                    }
                }
                break;
                case "plus": {
                    if (player.getFreeStatPoints() > 0) {
                        inputField.setText(String.valueOf(currentValue + 1));
                        player.setFreeStatPoints(player.getFreeStatPoints() - 1);
                        freeStatPointsField.setText(String.valueOf(player.getFreeStatPoints()));
                        player.changeStat(statName, 1);
                        printCharStats();
                    }
                }
                break;
            }
        };

        JButton minusButton = new JButton("-");
        minusButton.setBounds(132,3,18, 18);
        minusButton.setFocusPainted(false);
        minusButton.setFont(statFont);
        minusButton.setMargin(new Insets(0, 0, 0, 0));
        minusButton.addActionListener(statButtons);
        minusButton.setActionCommand("minus");
        minusButton.setRolloverEnabled(false);
        statRow.add(minusButton);

        JButton plusButton = new JButton("+");
        plusButton.setBounds(155,3,18, 18);
        plusButton.setFocusPainted(false);
        plusButton.setFont(statFont);
        plusButton.setMargin(new Insets(0, 0, 0, 0));
        plusButton.addActionListener(statButtons);
        plusButton.setActionCommand("plus");
        plusButton.setRolloverEnabled(false);
        statRow.add(plusButton);

        return statRow;
    }

    public void startGame() {
        changeLocation(worldMap.getCurrentLocation());
    }

    public void changeLocation(String locName) {
        worldMap.setCurrentLocation(locName);
        window.getContentPane().removeAll();
        window.repaint();
        ImageIcon locationBg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/" + worldMap.getCurrentLocation() +".png")));
        JLabel locationBgLabel = new JLabel(locationBg);
        locationBgLabel.setSize(width, height);

        printLocationButtons();
        shopPanel.setVisible(false);
        window.add(shopPanel);

        if (locName.contains("game_ending")) {
            OutlineLabel titleNameLabel = new OutlineLabel("Конец игры", JLabel.CENTER, 1);
            titleNameLabel.setFont(new Font(fontName, Font.BOLD, 48));
            titleNameLabel.setOutlineColor(Color.white);
            titleNameLabel.setForeground(Color.black);
            titleNameLabel.setOpaque(false);
            titleNameLabel.setBounds(0,480, width, 120);
            window.add(titleNameLabel, BorderLayout.CENTER);
        }

        if (locName.contains("search")) {
            showMonsterInformer();
            showBattleLogLabel();

            if (monster.getHp() == 0) {
                createComponentMap();
                Component attackButton = getComponentByName("attack_monster");
                if (attackButton != null) {
                    attackButton.setEnabled(false);
                }
            }
        }

        window.add(locationBgLabel);
    }

    private void showBattleLogLabel() {
        battleLogScroll.setBounds(200, 300, 624, 424);
        battleLogOutput.setBounds(0, 0, 624, 424);
        battleLogScroll.setVisible(monster.getHp() == 0);
        battleLogOutput.setFont(new Font("Calibri", Font.PLAIN, 16));
        battleLogOutput.setBackground(Color.WHITE);
        battleLogOutput.setForeground(Color.BLACK);
        battleLogOutput.setEditable(false);
        battleLogOutput.setAutoscrolls(true);
        DefaultCaret caret = (DefaultCaret)battleLogOutput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        battleLogOutput.setBorder(BorderFactory.createCompoundBorder(
              BorderFactory.createMatteBorder(1,1,1,1, Color.BLACK),
              BorderFactory.createEmptyBorder(5,8,5,8)));
        battleLogScroll.setBorder(BorderFactory.createEmptyBorder());
        window.add(battleLogScroll);
    }

    public void showMonsterInformer() {
        monsterInformer.setHorizontalAlignment(SwingConstants.CENTER);
        monsterInformer.setBounds(550, 924, 200, 70);
        monsterInformer.setFont(new Font("Calibri", Font.PLAIN, 12));
        monsterInformer.setBorder(BorderFactory.createCompoundBorder(
              BorderFactory.createMatteBorder(1,1,1,1, Color.BLACK),
              BorderFactory.createEmptyBorder(3,3,3,3)));
        monsterInformer.setOpaque(true);
        monsterInformer.setBackground(Color.white);
        monsterInformer.setForeground(Color.black);
        window.add(monsterInformer);
        updateMonsterInformer();
    }

    public void printLocationButtons() {
        ActionListener locationButtonsListener = e -> {
            String commandFromButton = e.getActionCommand();

            if (!isActionPossible) {
                return;
            }

            switch (commandFromButton) {
                case "alchemy_buy":
                    showAlchemyShop();
                    break;

                case "mercenary_train":
                    showMercenaryTrain();
                    break;

                case "char_screen":
                    showCharacter(false);
                    break;

                default:
                    if (commandFromButton.contains("forest_search")) {
                        monster = new Goblin();
                    }
                    if (commandFromButton.contains("graveyard_search")) {
                        monster = new Skeleton();
                    }
                    if (commandFromButton.contains("plains_search")) {
                        monster = new HellCreature();
                    }

                    if (commandFromButton.contains("attack")) {
                        createComponentMap();
                        Component attackButton = getComponentByName("attack_monster");
                        if (attackButton != null) {
                            attackButton.setEnabled(false);
                        }
                        startFight();
                        break;
                    }

                    changeLocation(commandFromButton);
                    break;
            }
        };

        playerStatInformer.setHorizontalAlignment(SwingConstants.CENTER);
        playerStatInformer.setBounds(50, 924, 200, 30);
        playerStatInformer.setFont(new Font("Calibri", Font.PLAIN, 12));
        playerStatInformer.setBorder(BorderFactory.createCompoundBorder(
              BorderFactory.createMatteBorder(1,1,1,1, Color.BLACK),
              BorderFactory.createEmptyBorder(3,3,3,3)));
        playerStatInformer.setOpaque(true);
        playerStatInformer.setBackground(Color.white);
        playerStatInformer.setForeground(Color.black);
        window.add(playerStatInformer);
        updatePlayerInformer();

        JButton charScreenButton = new JButton("Просмотр персонажа");
        charScreenButton.setActionCommand("char_screen");
        charScreenButton.addActionListener(locationButtonsListener);
        charScreenButton.setBounds(50, 964, 200, 30);
        charScreenButton.setFocusPainted(false);
        window.add(charScreenButton);

        int buttonsContainerYStart = 924;
        int buttonsContainerX = 300;
        int buttonsContainerY = buttonsContainerYStart;
        int buttonWidth = 200;
        int buttonHeight = 30;
        int buttonStepX = 250;
        int buttonStepY = 40;
        int buttonNumber = 1;

        ArrayList<LocationButton> buttonList = worldMap.getLocationButtons(worldMap.getCurrentLocation());
        for (LocationButton button : buttonList) {
            JButton buttonJ = new JButton(button.getButtonText());
            if (player.getLevel() < button.getLevelReq()) {
                buttonJ.setText(button.getButtonText());
                buttonJ.setName(button.getName());
                buttonJ.setToolTipText("Минимальный уровень " + button.getLevelReq());
                buttonJ.setEnabled(false);
            }
            else {
                buttonJ.setText(button.getButtonText());
                buttonJ.setName(button.getName());
            }
            buttonJ.setActionCommand(button.getName());
            buttonJ.addActionListener(locationButtonsListener);
            buttonJ.setBounds(buttonsContainerX, buttonsContainerY, buttonWidth, buttonHeight);
            buttonJ.setFocusPainted(false);
            window.add(buttonJ);
            buttonsContainerY += buttonStepY;

            if (buttonNumber % 2 == 0) {
                buttonsContainerX += buttonStepX;
                buttonsContainerY = buttonsContainerYStart;
            }

            buttonNumber++;
        }
    }

    private void showAlchemyShop() {
        LocationButton[][] alchemyShop = new LocationButton[4][4];
        alchemyShop[0][0] = new LocationButton("heal_potion", "<html>Зелье исцеления<br>Восполняет ОЗ до максимума<br>Цена: 50 золота</html>", 1);
        alchemyShop[1][0] = new LocationButton("endur1_potion", "<html>Элексир выносливости<br>+10 к максимальным ОЗ<br>Цена: 100 золота</html>", 1);
        alchemyShop[1][1] = new LocationButton("endur2_potion", "<html>Элексир стойкости<br>+15 к максимальным ОЗ<br>Цена: 300 золота</html>", 4);
        alchemyShop[1][2] = new LocationButton("endur3_potion", "<html>Элексир здоровья<br>+25 к максимальным ОЗ<br>Цена: 700 золота</html>", 7);
        printShopButtons(alchemyShop);
    }

    private void showMercenaryTrain() {
        LocationButton[][] mercShop = new LocationButton[4][4];
        mercShop[0][0] = new LocationButton("free_attributes1", "<html>Очки атрибутов урок №1<br>+1 к свободным очкам<br>Цена: 100 золота</html>", 1);
        mercShop[0][1] = new LocationButton("free_attributes2", "<html>Очки атрибутов урок №2<br>+2 к свободным очкам<br>Цена: 200 золота</html>", 1);
        mercShop[0][2] = new LocationButton("free_attributes3", "<html>Очки атрибутов урок №3<br>+3 к свободным очкам<br>Цена: 400 золота</html>", 3);
        mercShop[0][3] = new LocationButton("free_attributes4", "<html>Очки атрибутов урок №4<br>+5 к свободным очкам<br>Цена: 1000 золота</html>", 5);
        mercShop[1][0] = new LocationButton("critical_rate1", "<html>Крит шанс урок №1<br>+3 к характеристике<br>Цена: 100 золота</html>", 1);
        mercShop[1][1] = new LocationButton("critical_rate2", "<html>Крит шанс урок №2<br>+5 к характеристике<br>Цена: 200 золота</html>", 1);
        mercShop[1][2] = new LocationButton("critical_rate3", "<html>Крит шанс урок №3<br>+7 к характеристике<br>Цена: 400 золота</html>", 3);
        mercShop[1][3] = new LocationButton("critical_rate4", "<html>Крит шанс урок №4<br>+10 к характеристике<br>Цена: 1000 золота</html>", 5);
        mercShop[2][0] = new LocationButton("parry1", "<html>Парирование урок №1<br>+5 к характеристике<br>Цена: 100 золота</html>", 1);
        mercShop[2][1] = new LocationButton("parry2", "<html>Парирование урок №2<br>+7 к характеристике<br>Цена: 200 золота</html>", 1);
        mercShop[2][2] = new LocationButton("parry3", "<html>Парирование урок №3<br>+11 к характеристике<br>Цена: 400 золота</html>", 3);
        mercShop[2][3] = new LocationButton("parry4", "<html>Парирование урок №4<br>+17 к характеристике<br>Цена: 1000 золота</html>", 5);
        mercShop[3][0] = new LocationButton("evade1", "<html>Уклонение урок №1<br>+5 к характеристике<br>Цена: 100 золота</html>", 1);
        mercShop[3][1] = new LocationButton("evade2", "<html>Уклонение урок №2<br>+7 к характеристике<br>Цена: 200 золота</html>", 1);
        mercShop[3][2] = new LocationButton("evade3", "<html>Уклонение урок №3<br>+11 к характеристике<br>Цена: 400 золота</html>", 3);
        mercShop[3][3] = new LocationButton("evade4", "<html>Уклонение урок №4<br>+17 к характеристике<br>Цена: 1000 золота</html>", 5);
        printShopButtons(mercShop);
    }

    private void buyShopButton(JButton button, int cost) {
        player.buyPerk(button.getActionCommand(), cost);
        button.setEnabled(false);
        button.setToolTipText("Уже в наличии ");
        updatePlayerInformer();
    }

    private void printShopButtons(LocationButton[][] buttons) {
        int buttonsContainerX = 0;
        int buttonsContainerY = 0;
        int buttonWidth = 223;
        int buttonHeight = 60;
        int buttonStepX = 233;
        int buttonStepY = 70;
        LinkedHashMap<String, JButton> shopButtonsCollection = new LinkedHashMap<>();

        ActionListener shopButtonsListener = e -> {
            String commandFromButton = e.getActionCommand();

            if (!isActionPossible) {
                return;
            }

            switch (commandFromButton) {
                case "heal_potion":
                    player.buyHealPotion(50);
                    updatePlayerInformer();
                    break;

                case "endur1_potion":
                    buyShopButton(shopButtonsCollection.get("endur1_potion"), 100);
                    break;
                case "endur2_potion":
                    buyShopButton(shopButtonsCollection.get("endur2_potion"), 300);
                    break;
                case "endur3_potion":
                    buyShopButton(shopButtonsCollection.get("endur3_potion"), 700);
                    break;

                case "free_attributes1":
                    buyShopButton(shopButtonsCollection.get("free_attributes1"), 100);
                    break;
                case "free_attributes2":
                    buyShopButton(shopButtonsCollection.get("free_attributes2"), 200);
                    break;
                case "free_attributes3":
                    buyShopButton(shopButtonsCollection.get("free_attributes3"), 400);
                    break;
                case "free_attributes4":
                    buyShopButton(shopButtonsCollection.get("free_attributes4"), 1000);
                    break;

                case "critical_rate1":
                    buyShopButton(shopButtonsCollection.get("critical_rate1"), 100);
                    break;
                case "critical_rate2":
                    buyShopButton(shopButtonsCollection.get("critical_rate2"), 200);
                    break;
                case "critical_rate3":
                    buyShopButton(shopButtonsCollection.get("critical_rate3"), 400);
                    break;
                case "critical_rate4":
                    buyShopButton(shopButtonsCollection.get("critical_rate4"), 1000);
                    break;

                case "parry1":
                    buyShopButton(shopButtonsCollection.get("parry1"), 100);
                    break;
                case "parry2":
                    buyShopButton(shopButtonsCollection.get("parry2"), 200);
                    break;
                case "parry3":
                    buyShopButton(shopButtonsCollection.get("parry3"), 400);
                    break;
                case "parry4":
                    buyShopButton(shopButtonsCollection.get("parry4"), 1000);
                    break;

                case "evade1":
                    buyShopButton(shopButtonsCollection.get("evade1"), 100);
                    break;
                case "evade2":
                    buyShopButton(shopButtonsCollection.get("evade2"), 200);
                    break;
                case "evade3":
                    buyShopButton(shopButtonsCollection.get("evade3"), 400);
                    break;
                case "evade4":
                    buyShopButton(shopButtonsCollection.get("evade4"), 1000);
                    break;
            }
        };

        shopPanel.removeAll();
        shopPanel.repaint();
        shopPanel.setBounds(50, 300, 924, 500);
        shopPanel.setLayout(null);

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                LocationButton button = buttons[i][j];
                if (button == null) {
                    continue;
                }
                JButton buttonJ = new JButton(button.getButtonText());
                shopButtonsCollection.put(button.getName(), buttonJ);
                if (player.getLevel() < button.getLevelReq()) {
                    buttonJ.setText(button.getButtonText());
                    buttonJ.setName(button.getName());
                    buttonJ.setToolTipText("Минимальный уровень " + button.getLevelReq());
                    buttonJ.setEnabled(false);
                } else if (player.isPerkOn(button.getName())) {
                    buttonJ.setText(button.getButtonText());
                    buttonJ.setName(button.getName());
                    buttonJ.setToolTipText("Уже в наличии ");
                    buttonJ.setEnabled(false);
                } else {
                    buttonJ.setText(button.getButtonText());
                    buttonJ.setName(button.getName());
                }
                buttonJ.setFont(new Font("Calibri", Font.PLAIN, 14));
                buttonJ.setActionCommand(button.getName());
                buttonJ.addActionListener(shopButtonsListener);
                buttonJ.setBounds(buttonsContainerX + i * buttonStepX, buttonsContainerY + j * buttonStepY, buttonWidth, buttonHeight);
                buttonJ.setFocusPainted(false);
                shopPanel.add(buttonJ);
            }
        }

        shopPanel.setOpaque(false);
        shopPanel.setVisible(true);
    }

    private void updatePlayerInformer() {
        String informerText = "";
        informerText += "ОЗ: " + player.getHp() + "/" + player.getMaxhp();
        informerText += " | Золото: " + player.getGold() + "<br>";
        informerText += "Уровень: " + player.getLevel();
        informerText += " | Опыт: " + player.getExp() + "/" + player.getExpToLevel(player.getLevel());
        playerStatInformer.setText("<html>" + informerText + "</html>");
    }

    private void updateMonsterInformer() {
        String informerText = "";
        informerText += monster.getName() + "<br>";
        informerText += "Уровень: " + monster.getLevel() + "<br>";
        informerText += "ОЗ: " + monster.getHp() + "/" + monster.getMaxhp() + "<br>";
        informerText += "Опыт: " + monster.getExp() + "<br>";
        monsterInformer.setText("<html>" + informerText + "</html>");
    }

    private void startFight() {
        this.battlefield = new Battlefield(monster, player);

        Runnable task = battlefield::doFight;
        Thread thread = new Thread(task);
        thread.start();

        battleLogScroll.setVisible(true);
        BattlefieldSwingWorker battlefieldSwingWorker = new BattlefieldSwingWorker();
        battlefieldSwingWorker.execute();
    }

    public class BattlefieldSwingWorker extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() {
            isActionPossible = false;

            battleLogOutput.setText("");
            while(battlefield.isProceed()) {
                if (!battlefield.getLastString().isEmpty()) {
                    battleLogOutput.setText(battleLogOutput.getText() + battlefield.getLastString() + "\r\n");
                    battlefield.setLastString("");
                    updateMonsterInformer();
                    updatePlayerInformer();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (!battlefield.getLastString().isEmpty()) {
                battleLogOutput.setText(battleLogOutput.getText() + battlefield.getLastString() + "\r\n");
                battlefield.setLastString("");
            }
            if (monster.getHp() == 0) {
                battleLogOutput.setText(battleLogOutput.getText() + "Победа!\r\n");
            }
            if (player.getHp() == 0) {
                battleLogOutput.setText(battleLogOutput.getText() + "Поражение!\r\n\r\nКонец игры...");
            }

            battleLogOutput.setText(battleLogOutput.getText() + player.combatResults(monster));
            updateMonsterInformer();
            updatePlayerInformer();

            if (!player.isDefeated()) {
                isActionPossible = true;
            }

            return null;
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.init();
    }

    private void createComponentMap() {
        componentMap = new HashMap<String,Component>();
        Component[] components = window.getContentPane().getComponents();
        for (int i=0; i < components.length; i++) {
            componentMap.put(components[i].getName(), components[i]);
        }
    }

    public Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
            return (Component) componentMap.get(name);
        }
        else return null;
    }

}
