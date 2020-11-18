import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Apple extends JFrame {

    // Настройки окна
    public static Toolkit toolkit = Toolkit.getDefaultToolkit();
    public static Dimension dimension = toolkit.getScreenSize();
    public static final int windowWidth = 800;
    public static final int windowHeight = 800;
    public static final int screenCenterX = (dimension.width / 2) - windowWidth / 2;
    public static final int screenCenterY = (dimension.height / 2) - windowHeight / 2;

    private Random random = new Random();

    // Настройки игры

    public static final int SIZE = 100;
    public static final int BIOM_SIZE = 15;
    public static final int BIOM_TYPES_NUM = 2;


    private static int[][] tiles = new int[BIOM_SIZE][BIOM_SIZE];
//    private static int[][] terrainType = new int[BIOM_SIZE][BIOM_SIZE];

    public Apple() {

        //--------Window Settings---------//
        setTitle("Mine Tycoon");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(screenCenterX, screenCenterY, windowWidth, windowHeight);

        Container objects = this.getContentPane();
        Container map = this.getContentPane();


        Human human = new Human(objects, (windowWidth / 2), windowHeight / 2);


        grassBiomesGen(map);


//        JPanel test = new JPanel();
//        test.setOpaque(false);
//        test.setBounds(100, 85, 250, 90);
//
//        JLabel testLbl = new JLabel("gaedgaedgaedgaedr");
//
//        test.add(testLbl);
//        map.add(test);
//        JComponent tst = new JComponent() {
//        };


//        human.move('r');

        setVisible(true);
    }

    public void grassBiomesGen(Container container) {
        JLabel tileSize = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/earth/grass1.png"))));
        int x;
        int y = - tileSize.getIcon().getIconWidth();
        for (int i = 0; i < BIOM_SIZE; i++) {
            y += tileSize.getIcon().getIconHeight() - 10;
            x = 0;
            for (int j = 0; j < BIOM_SIZE; j++) {
                int texture = random.nextInt(3);
                JLabel tile = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/earth/grass0.png"))));
                if (texture == 0) {
                    tile = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/earth/grass0.png"))));
                }
                if (texture == 1) {
                    tile = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/earth/grass1.png"))));
                }
                if (texture == 2) {
                    tile = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/earth/grass2.png"))));
                }
                tile.setBounds(x,y,tile.getIcon().getIconWidth(),tile.getIcon().getIconHeight());
//                terrain[]

                container.add(tile);

                x += tileSize.getIcon().getIconWidth() - 10;

            }
        }

    }
}
