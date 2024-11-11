package housy.pass.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Arrays;

public class StackLayout implements LayoutManager {

    public int vGap = 0;
    public int hGap = 0;

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            if (parent.getComponentCount() == 0) {
                return parent.getPreferredSize();
            }
            int totalHeight = Arrays.stream(parent.getComponents()).mapToInt(x -> (int) x.getPreferredSize().getHeight()).sum() + vGap * (parent.getComponentCount() + 1);
            return new Dimension((int) parent.getPreferredSize().getWidth(), Math.max(totalHeight, (int) parent.getPreferredSize().getHeight()));
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            int y = vGap;
            Component[] comps = parent.getComponents();
            for (Component comp : comps) {
                Dimension size = comp.getPreferredSize();
                comp.setSize(parent.getWidth() - hGap * 2, (int) size.getHeight());
                comp.setLocation(hGap, y);
                y += comp.getHeight() + vGap;
            }
            parent.revalidate();
            parent.repaint();
        }

    }

}
