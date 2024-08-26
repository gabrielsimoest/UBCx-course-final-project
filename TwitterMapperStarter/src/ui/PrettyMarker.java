package ui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import twitter4j.Status;
import twitter4j.User;
import util.ImageCache;
import util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PrettyMarker extends MapMarkerSimple {

    private String imageURL;
    private User user;
    private BufferedImage image;
    private String text;
    private Status status;
    private Color color;

    public PrettyMarker(Layer layer, String name, Coordinate coord, Color color, Status status) {
        super(layer, name, coord);
        this.color = color;
        setBackColor(color);
        this.status = status;
        this.text = status.getText();
        this.user = status.getUser();
        this.imageURL = status.getUser().getProfileImageURL();
        this.image = ImageCache.getInstance().getImage(status.getUser().getProfileImageURL());
    }

    public User getUser() {
        return user;
    }
    public String getText() {
        return text;
    }
    public Status getStatus() {
        return status;
    }
    public BufferedImage getImage() { return image; }
    public String getImageURL() { return imageURL; }

    @Override
    public Color getColor(){
        return color;
    }

    private final Object lock = new Object();

    @Override
    public void paint(Graphics g, Point position, int radius) {
        synchronized (lock) {
            int size = radius * 2;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillOval(position.x - radius + 2, position.y - radius + 2, size, size);

            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 200));
            g2.fillOval(position.x - radius, position.y - radius, size, size);

            g2.setColor(color.darker());
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(position.x - radius, position.y - radius, size, size);

            if (image != null) {
                BufferedImage circleImage = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
                Graphics2D gCircle = circleImage.createGraphics();
                gCircle.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 30, 30));
                gCircle.drawImage(image, 0, 0, 30, 30, null);
                gCircle.dispose();

                g2.drawImage(circleImage, position.x - 15, position.y - 15, null);
                g2.setColor(color.darker()); // Usar a mesma cor para a borda da imagem
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(position.x - 15, position.y - 15, 30, 30);
            }

            if (this.getLayer() == null || this.getLayer().isVisibleTexts()) {
                this.paintText(g, position);
            }
        }
    }
    
}