package env;

import jason.environment.grid.GridWorldView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.Random;

public class WorldView extends GridWorldView {

    Intersection env = null;

    public WorldView(WorldModel model) {
        super(model, "Intersection world", 600);
        setVisible(true);
        repaint();
    }

    public void setEnv(Intersection env) {
        this.env = env;
        Random rand = new Random();
        int selectedIndex = rand.nextInt(scenarios.getItemCount());  // Genera un indice casuale valido
        scenarios.setSelectedIndex(selectedIndex);
    }

    JComboBox scenarios;

    JSlider jSpeed;


    @Override
    public void initComponents(int width) {
        super.initComponents(width);
        scenarios = new JComboBox();
        for (int i = 1; i <= 3; i++) {
            scenarios.addItem(i);
        }
        JPanel args = new JPanel();
        args.setLayout(new BoxLayout(args, BoxLayout.Y_AXIS));

        JPanel sp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sp.setBorder(BorderFactory.createEtchedBorder());
        sp.add(new JLabel("Scenario:"));
        sp.add(scenarios);

        jSpeed = new JSlider();
        jSpeed.setMinimum(0);
        jSpeed.setMaximum(400);
        jSpeed.setValue(50);
        jSpeed.setPaintTicks(true);
        jSpeed.setPaintLabels(true);
        jSpeed.setMajorTickSpacing(100);
        jSpeed.setMinorTickSpacing(20);
        jSpeed.setInverted(true);


        Hashtable<Integer, Component> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("max"));
        labelTable.put(200, new JLabel("speed"));
        labelTable.put(400, new JLabel("min"));
        jSpeed.setLabelTable(labelTable);
        JPanel p = new JPanel(new FlowLayout());
        p.setBorder(BorderFactory.createEtchedBorder());
        p.add(jSpeed);

        args.add(sp);
        args.add(p);

    }

    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        Color idColor = Color.black;
        super.drawAgent(g, x, y, c, -1);
        idColor = Color.white;
        g.setColor(idColor);
        drawString(g, x, y, defaultFont, String.valueOf(id+1));
    }

    public static void main(String[] args) throws Exception {
        Intersection env = new Intersection();
        env.init(new String[] {"yes","10"});
    }

}
