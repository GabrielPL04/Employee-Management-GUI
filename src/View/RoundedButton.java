package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public
    class RoundedButton
    extends JButton {

    private final Color fillColor;
    private final Color borderColor;
    private final Color hoverBorderColor;
    private boolean hover = false;
    private final boolean isWhiteStyle;

    public RoundedButton(String text) {
        this(text, false);
    }

    public RoundedButton(String text, boolean whiteStyle) {
        super(text);
        this.isWhiteStyle = whiteStyle;

        this.fillColor = whiteStyle ? Color.WHITE : new Color(25, 118, 210);
        this.borderColor = whiteStyle ? new Color(100, 100, 100) : fillColor.darker();
        this.hoverBorderColor = whiteStyle ? new Color(60, 60, 60) : fillColor.brighter();

        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(whiteStyle ? Color.BLACK : Color.WHITE);
        setFont( new Font("SansSerif", Font.BOLD, 11));
        setCursor( new Cursor(Cursor.HAND_CURSOR));

        addMouseListener( new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(fillColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        g2.setColor(hover ? hoverBorderColor : borderColor);
        g2.setStroke( new BasicStroke(2));
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    public boolean isContentAreaFilled() {
        return false;
    }
}
