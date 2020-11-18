import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class App extends JFrame {
    // Настройки окна
    public static final int SIZE = 5;
    public static final int SIZE_MULTIPLIER = 151;
    public static Toolkit toolkit = Toolkit.getDefaultToolkit();
    public static Dimension dimension = toolkit.getScreenSize();
    public static final int screenCenterX = (dimension.width / 2) - (SIZE * (SIZE_MULTIPLIER / 2));
    public static final int screenCenterY = (dimension.height / 2) - (SIZE * (SIZE_MULTIPLIER / 2));
    public static final int windowWidth = 760;
    public static final int windowHeight = 755;
    public static final int BUTTON_SIZE = 110;
    public static float infoPanelOpaque = 255F;
    public static float INFO_PANEL_OPAQUE_SPEED = 0.35F;
    public static float INFO_PANEL_MAX_OPAQUE = 255F;

    private final Random random = new Random();

    // Настройки игры
    public static final int RESOURCE_RARITY = 30; // чем выше значение, тем реже встречаются ресурсы
    public static final int RESOURCE_MULTIPLIER = 10;
    private static final int GEO_RESEARCH_MULTIPLIER = 100;
    private static final int MINE_COST_MULTIPLIER = 30;
    private static final int MINE_MAX_LEVEL = 5;
    private static final int COAL_EARN_BASE = 5;
    private static final int IRON_EARN_BASE = 7;
    private static final int GOLD_EARN_BASE = 15;
    private static final int PLANTS_MAX = 10;
    private static final int MINI_OBJECTS_MAX = 20;

    private static int money = 100000;
    private static int earning = 0;
    private static String msg = "Добро пожаловать в Mine Tycoon!";
    private static int gameTime = 0;
    private static int currentGeoResearchCost = 100;
    private static int currentAction = 0;


    // Массивы
    JButton[][] buttons = new JButton[SIZE][SIZE];
    private static final char[][] resourceType = new char[SIZE][SIZE];
    private static final int[][] resourceAmount = new int[SIZE][SIZE];
    private static final int[][] mineLevel = new int[SIZE][SIZE];
    private static final int[][] earnings = new int[SIZE][SIZE];
    private static final boolean[][] isResearched = new boolean[SIZE][SIZE];


    public App() {
        //--------Window Settings---------//
        setTitle("Mine Tycoon");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(screenCenterX, screenCenterY, windowWidth, windowHeight);

        //------ Генерим ресурсы-----//
        resourcesGen();

        //--------Панель меню---------//
        JPanel menu = new JPanel();
        menu.setBounds(0, 7, windowWidth, 32);
        add(menu);
        JLabel moneyCaption = new JLabel("Деньги: " + money + " $  ");
        JLabel earningCaption = new JLabel("|    Доход в секунду: " + earning + " $  ");
        moneyCaption.setForeground(new Color(230, 230, 200));
        moneyCaption.setFont(moneyCaption.getFont().deriveFont(15F));
        menu.add(moneyCaption);
        menu.add(earningCaption);
        JButton buy = new JButton("Построить шахту");
        JButton find = new JButton("Георазведка");
        find.addActionListener(actionEvent -> {
            if (currentAction == 1) {
                find.setText("Георазведка");
                currentAction = 0;
                buy.setEnabled(true);
                return;
            }
            currentAction = 1;
            msg = "Выберите участок для георазведки...";
            find.setText("Отмена");
            buy.setEnabled(false);
        });
        buy.addActionListener(actionEvent -> {
            if (currentAction == 2) {
                buy.setText("Купить/улучшить шахту");
                currentAction = 0;
                find.setEnabled(true);
                return;
            }
            currentAction = 2;
            msg = "Выберите рудник для строительства (улучшения) шахты...";
            buy.setText("Отмена");
            find.setEnabled(false);
        });

        menu.add(find);
        menu.add(buy);
        JPanel msgPanel = new JPanel();
        msgPanel.setBounds(0, 37, windowWidth, 25);
        JLabel msgCaption = new JLabel("Добро пожаловать в Mine Tycoon!");
        msgCaption.setForeground(moneyCaption.getForeground());
        earningCaption.setForeground(moneyCaption.getForeground());
        msgPanel.add(msgCaption);

        menu.setOpaque(false);
        msgPanel.setOpaque(false);

        //--------BackGround---------//
        JLabel background = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/back.jpg"))));
        Container container = this.getContentPane();
        //--------------//-------------//

        container.add(menu);
        container.add(msgPanel);


        //--------ПАНЕЛЬ ИНФЫ!---------//
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setBounds(10, 85, 250, 90);
        info.setLayout(new GridLayout(5, 1));

        JLabel researchCostCaption = new JLabel("Стоимость разработки:");
        JLabel mineralsAmountCaption = new JLabel("Количество руды:");
        JLabel mineCostCaption = new JLabel("");
        JLabel mineLevelCaption = new JLabel("");
        JLabel mineEarningCaption = new JLabel("");

        researchCostCaption.setForeground(new Color(255, 255, 220, 50));
        mineLevelCaption.setForeground(moneyCaption.getForeground());
        mineCostCaption.setForeground(moneyCaption.getForeground());
        mineralsAmountCaption.setForeground(moneyCaption.getForeground());
        info.add(researchCostCaption);
        info.add(mineralsAmountCaption);
        info.add(mineCostCaption);
        info.add(mineLevelCaption);
        info.add(mineEarningCaption);
        info.setVisible(false);
        container.add(info);
        // генерируем красоту))
        treeGen(container);
        Human human = new Human(container, 0, 100);



        // тест анимации
        human.setBounds(100,100,human.getWidth(),human.getHeight());
        human.move('r');





        //-----Генерим массив с кнопками-----//
        // это отступы для них
        int currentX = -80;
        int currentY = -50;

        for (int i = 0; i < SIZE; i++) {
            currentY += 130;
            currentX = 50;
            for (int j = 0; j < SIZE; j++) {
                ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/empty.png")));
                Image img = icon.getImage();
                Image newImg = img.getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH);
                icon = new ImageIcon(newImg);

                JButton button = new JButton(icon);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setBackground(Color.LIGHT_GRAY);
                button.setOpaque(false);
                button.setBounds(currentX, currentY, BUTTON_SIZE, BUTTON_SIZE);
                currentX += 130;

                buttons[i][j] = button;
                button.setText("");

                int ii = i;
                int jj = j;

                button.addActionListener(actionEvent -> {
                    if (currentAction == 0) {
                        info.setVisible(true);
                        infoPanelOpaque = INFO_PANEL_MAX_OPAQUE;

                        if (!isResearched[ii][jj]) {
                            researchCostCaption.setText("Стоимость разработки: " + currentGeoResearchCost + " $");
                            mineEarningCaption.setText("");
                            mineLevelCaption.setText("");
                            mineCostCaption.setText("");
                        } else researchCostCaption.setText("Ресурс: " + getResName(resourceType[ii][jj], 0));

                        if (resourceAmount[ii][jj] > 0 && isResearched[ii][jj] && mineLevel[ii][jj] < 1) {
                            mineCostCaption.setText("Построить шахту: " + ((mineLevel[ii][jj] + 1) * MINE_COST_MULTIPLIER) * getResourceCost(resourceType[ii][jj]) + " $");
                            mineralsAmountCaption.setText("Количество руды: " + resourceAmount[ii][jj] + " т.");
                            mineEarningCaption.setText("");
                            mineLevelCaption.setText("");

                        }
                        if (resourceAmount[ii][jj] > 0 && isResearched[ii][jj] && mineLevel[ii][jj] > 0) {
                            mineCostCaption.setText("Улучшить шахту: " + ((mineLevel[ii][jj] + 1) * MINE_COST_MULTIPLIER) * getResourceCost(resourceType[ii][jj]) + " $");
                            if (mineLevel[ii][jj] == MINE_MAX_LEVEL) mineCostCaption.setText("");
                            mineralsAmountCaption.setText("Количество руды: " + resourceAmount[ii][jj] + " т.");
                            mineLevelCaption.setText("Уровень шахты: " + mineLevel[ii][jj]);
                            mineEarningCaption.setText("Доход с шахты: " + earnings[ii][jj] + " $ в сек.");
                        }

                        if (!isResearched[ii][jj]) {
                            mineralsAmountCaption.setText("Количество руды: НЕИЗВЕСТНО");
                            mineEarningCaption.setText("");
                            mineLevelCaption.setText("");
                            mineCostCaption.setText("");
                        }
                        if (resourceAmount[ii][jj] == 0 && isResearched[ii][jj]) {
                            mineralsAmountCaption.setText("Количество руды: НЕТ");
                            mineEarningCaption.setText("");
                            mineLevelCaption.setText("");
                            mineCostCaption.setText("");
                        }
                        if (resourceAmount[ii][jj] == 0 && isResearched[ii][jj] && mineLevel[ii][jj] > 0) {

                        }


                    }
                    if (currentAction != 0) {
                        if (currentAction == 1) {
                            find.setText("Георазведка");
                            buy.setEnabled(true);
                            find.setEnabled(true);
                            if (money < currentGeoResearchCost) {
                                msg = "Недостаточно денег...";
                                currentAction = 0;
                                return;
                            }
                            if (isResearched[ii][jj]) {
                                msg = "Эта территория уже разведана";
                                currentAction = 0;
                                return;
                            }
                            if (!isResearched[ii][jj]) {
                                money -= currentGeoResearchCost;
                                currentGeoResearchCost += GEO_RESEARCH_MULTIPLIER;
                                buttons[ii][jj].setIcon(getResourceIco(resourceType, ii, jj));
                                currentAction = 0;
                                isResearched[ii][jj] = true;
                                if (resourceType[ii][jj] != '0') {
                                    msg = "Отлично! Вы нашли залежи " + getResName(resourceType[ii][jj], 1) + " в количестве: " + resourceAmount[ii][jj] + " т. Можно строить шахту...";
                                } else msg = "К сожалению, здесь руда не обнаружена... ";
                            }
                        }
                        if (currentAction == 2) {
                            buy.setText("Купить/улучшить шахту");
                            buy.setEnabled(true);
                            find.setEnabled(true);
                            if (money < ((mineLevel[ii][jj] + 1) * MINE_COST_MULTIPLIER) * getResourceCost(resourceType[ii][jj])) {
                                msg = "Недостаточно денег...";
                                currentAction = 0;
                                return;
                            }
                            if (mineLevel[ii][jj] == MINE_MAX_LEVEL) {
                                msg = "Достигнут максимальный уровень шахты";
                                currentAction = 0;
                                return;
                            }
                            if (!isResearched[ii][jj]) {
                                msg = "Сначала нужно провести геологоразведку...";
                                currentAction = 0;
                                return;
                            }
                            if (isResearched[ii][jj] && resourceAmount[ii][jj] == 0) {
                                msg = "Здесь руды нет...";
                                currentAction = 0;
                                return;
                            }
                            if (isResearched[ii][jj] && resourceAmount[ii][jj] > 0) {
                                money -= ((mineLevel[ii][jj] + 1) * MINE_COST_MULTIPLIER * getResourceCost(resourceType[ii][jj]));
                                mineLevel[ii][jj]++;
                                button.setIcon(getMineIco(mineLevel[ii][jj]));
                                earnings[ii][jj] = getResourceCost(resourceType[ii][jj]) * mineLevel[ii][jj];
                                msg = "Вы улучшили шахту до уровня " + mineLevel[ii][jj];

                                currentAction = 0;

                            }
                        }
                    }

                });
                container.add(button);
            }
        }
        container.add(background);
        this.setVisible(true);

        //--------Считаем прибыль раз в секунду + интра---------//
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        gameTime++;

                        {   // Суммируем доходы/сек со всех шахт
                            int earn = 0;
                            for (int k = 0; k < SIZE; k++) {
                                for (int l = 0; l < SIZE; l++) {
                                    if (earnings[k][l] > 0) {
                                        earn += earnings[k][l];
                                    }
                                }

                            }
                            earning = earn;
                        }
                        money += earning;


                        for (int k = 0; k < SIZE; k++) {
                            for (int l = 0; l < SIZE; l++) {
                                if (resourceAmount[k][l] > 0 && mineLevel[k][l] > 0) {
                                    resourceAmount[k][l] -= mineLevel[k][l];
                                }
                                if (resourceAmount[k][l] < mineLevel[k][l] && mineLevel[k][l] > 0) {
                                    msg = "В " + getResName(resourceType[k][l], 2) + " шахте по адресу " + k + 1 + "x" + l + 1 + " закончилась руда!";
                                    money += resourceAmount[k][l] * getResourceCost(resourceType[k][l]);
                                    resourceAmount[k][l] = 0;
                                    earnings[k][l] = 0;
                                    mineLevel[k][l] = 0;
                                    buttons[k][l].setIcon(getMineIco(0));
                                }
                            }
                        }

//                        msgIntro();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //--------Обновляем лейблы в соответствии с полями---------//
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10);
                        moneyCaption.setText("Деньги: " + money + " $  ");
                        msgCaption.setText(msg);
                        earningCaption.setText("|    Доход в секунду: " + earning + " $  ");


                        // а так же делаем инфо плавно исчезающей

                        researchCostCaption.setForeground(new Color(255, 255, 220, (int) infoPanelOpaque));
                        mineCostCaption.setForeground(new Color(255, 255, 220, (int) infoPanelOpaque));
                        mineLevelCaption.setForeground(new Color(255, 255, 220, (int) infoPanelOpaque));
                        mineralsAmountCaption.setForeground(new Color(255, 255, 220, (int) infoPanelOpaque));
                        mineEarningCaption.setForeground(new Color(255, 255, 220, (int) infoPanelOpaque));

                        if (infoPanelOpaque > 0) infoPanelOpaque -= INFO_PANEL_OPAQUE_SPEED;

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        //--------------//-------------//
    }

    public void msgIntro() {
        if (gameTime == 5) {
            msg = "Для начала вам следует провести геологическую разведку местности. Каждая георазведка стоит денег...";
        }
        if (gameTime == 10) {
            msg = "Чтобы узнать стоимость, нажмите на свободный участок земли...";
        }
        if (gameTime == 15) {
            msg = "...затем кнопку 'Георазведка', чтобы разведать территорию ";
        }
    }

    public ImageIcon getResourceIco(char[][] mineralsType, int i, int j) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/empty.png")));
        if (mineralsType[i][j] == '0') {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/pitfall.png")));
        }
        if (mineralsType[i][j] == 'c') {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/coal.png")));
        }
        if (mineralsType[i][j] == 'i') {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/iron.png")));
        }
        if (mineralsType[i][j] == 'g') {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/gold.png")));
        }
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);
        return icon;
    } //mineralsType, i ,j

    public ImageIcon getMineIco(int mineLev) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/pitfall.png")));
        if (mineLev == 1) {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/mine1.png")));
        }
        if (mineLev == 2) {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/mine2.png")));
        }
        if (mineLev == 3) {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/mine3.png")));
        }
        if (mineLev == 4) {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/mine4.png")));
        }
        if (mineLev == 5) {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/mine5.png")));
        }
        if (mineLev == 0) {
            icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/mine0.png")));
        }
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);
        return icon;
    }

    public void resourcesGen() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int x = random.nextInt(100) - random.nextInt(RESOURCE_RARITY);
                resourceType[i][j] = '0';
                if (x < 20) {
                    resourceType[i][j] = '0';
                }
                if (x >= 20 && x < 40) {
                    resourceType[i][j] = 'c';
                }
                if (x >= 40 && x < 60) {
                    resourceType[i][j] = 'i';
                }
                if (x >= 70) {
                    resourceType[i][j] = 'g';
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (resourceType[i][j] != '0') {
                    if (resourceType[i][j] == 'c') {
                        resourceAmount[i][j] = random.nextInt((1000) + 1) * RESOURCE_MULTIPLIER;
                    }
                    if (resourceType[i][j] == 'i') {
                        resourceAmount[i][j] = random.nextInt((1000) + 1) * RESOURCE_MULTIPLIER / 2;
                    }
                    if (resourceType[i][j] == 'g') {
                        resourceAmount[i][j] = random.nextInt((1000) + 1) * RESOURCE_MULTIPLIER / 3;
                    }
                }
            }
        }
    }

    public String getResName(char index, int inCase) {
        if (inCase == 0) {
            if (index == 'c') {
                return "УГОЛЬ";
            }
            if (index == 'i') {
                return "ЖЕЛЕЗО";
            }
            if (index == 'g') {
                return "ЗОЛОТО";
            }
        }
        if (inCase == 1) {
            if (index == 'c') {
                return "УГЛЯ";
            }
            if (index == 'i') {
                return "ЖЕЛЕЗА";
            }
            if (index == 'g') {
                return "ЗОЛОТА";
            }
        }
        if (inCase == 2) {
            if (index == 'c') {
                return "УГОЛЬНОЙ";
            }
            if (index == 'i') {
                return "ЖЕЛЕЗОЙ";
            }
            if (index == 'g') {
                return "ЗОЛОТОЙ";
            }
        }
        return "ПУСТО";
    }

    public int getResourceCost(char index) {
        if (index == 'c') {
            return COAL_EARN_BASE;
        }
        if (index == 'i') {
            return IRON_EARN_BASE;
        }
        if (index == 'g') {
            return GOLD_EARN_BASE;
        }
        return 0;
    }

    public void treeGen(Container container) {

        JLabel[] plants = new JLabel[PLANTS_MAX];
        JLabel[] miniObjects = new JLabel[MINI_OBJECTS_MAX];


        for (int i = 0; i < PLANTS_MAX; i++) {
            int plantType = random.nextInt(4);
            int plantX = random.nextInt(windowWidth);
            int plantY = random.nextInt(windowHeight);
            if (plantY > 0 && plantY < 160) plantY = 140 + random.nextInt(15);
            if (plantY > 160 && plantY < 290) plantY = 270 + random.nextInt(15);
            if (plantY > 290 && plantY < 420) plantY = 400 + random.nextInt(15);
            if (plantY > 420 && plantY < 550) plantY = 530 + random.nextInt(15);
            if (plantY > 550 && plantY < 680) plantY = 660 + random.nextInt(15);

            plants[i] = new JLabel();

            if (plantType == 0) {
                plants[i].setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/tree1.png"))));
            }
            if (plantType == 1) {
                plants[i].setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/tree2.png"))));
            }
            if (plantType == 2) {
                plants[i].setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/deadTree.png"))));
            }
            if (plantType == 3) {
                plants[i].setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/tree3.png"))));
            }
            plants[i].setBounds(plantX, plantY - plants[i].getIcon().getIconHeight(), plants[i].getIcon().getIconWidth(), plants[i].getIcon().getIconHeight() + 100);
            container.add(plants[i]);

        }
        for (int i = 0; i < MINI_OBJECTS_MAX; i++) {
            int plantType = random.nextInt(4);
            int plantX = random.nextInt(windowWidth);
            int plantY = random.nextInt(windowHeight);
            if (plantY < 160) plantY = 140 + random.nextInt(15);
            if (plantY > 160 && plantY < 290) plantY = 270 + random.nextInt(15);
            if (plantY > 290 && plantY < 420) plantY = 400 + random.nextInt(15);
            if (plantY > 420 && plantY < 550) plantY = 530 + random.nextInt(15);
            if (plantY > 550 && plantY < 680) plantY = 660 + random.nextInt(15);

            miniObjects[i] = new JLabel();

            if (plantType == 0) {
                miniObjects[i].setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/leaves.png"))));
            }
            if (plantType == 1) {
                miniObjects[i].setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/mushrooms.png"))));
            }
            if (plantType == 2) {
                miniObjects[i].setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/stones.png"))));
            }
            if (plantType == 3) {
                miniObjects[i].setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/flowers.png"))));
            }
            miniObjects[i].setBounds(plantX, plantY - miniObjects[i].getIcon().getIconHeight(), miniObjects[i].getIcon().getIconWidth(), miniObjects[i].getIcon().getIconHeight() + 100);
            container.add(miniObjects[i]);

        }

    }
}
