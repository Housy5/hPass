package housy.pass.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class CenteredLayout implements LayoutManager {

    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return parent.getPreferredSize();
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            if (parent.getComponentCount() > 1) {
                throw new IllegalStateException("JPanel can only contain one component, but multiple components were added.");
            }

            if (parent.getComponentCount() == 0) {
                return;
            }

            Component comp = parent.getComponent(0);
            comp.setSize(comp.getPreferredSize());
            comp.setLocation((parent.getWidth() - comp.getWidth()) / 2, (parent.getHeight() - comp.getHeight()) / 2);
        }

        parent.repaint();
    }

}
