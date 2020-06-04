package steam.model;

import javafx.scene.image.Image;
/*
 * He creado ésta clase para poder tomar la url de un objeto Image, ya que en Java8 no existe un método específico
 * getUrl() y el método que existe impl_getUrl() está deprecated.
 */
public class LocatedImage extends Image {
    private String url;

    public LocatedImage(String url, double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth) {
        super(url, requestedWidth, requestedHeight, preserveRatio, smooth);
        this.url = url.replace("\\", "/"); //cambio \ por / para luego sacar el nombre de la imagen
    }

    public String getURL() {
        return url;
    }
}
