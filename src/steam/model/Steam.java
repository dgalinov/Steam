package steam.model;

public class Steam implements Comparable<Steam> {
    private String fileRoute;
    private int id;
    private boolean updatable;
    private String name;
    private int price;
    private String web;
    private String image;
    private String description;

    Steam(String fileRoute, int id, boolean updatable, String name,
          int price, String web, String image, String description){
        this.fileRoute = fileRoute;
        this.id = id;
        this.updatable = updatable;
        this.name = name;
        this.price = price;
        this.web = web;
        this.image = image;
        this.description = description;
    }

    String getFileRoute() {
        return fileRoute;
    }

    public int getId() {
        return id;
    }

    boolean isUpdatable() {
        return updatable;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    void setPrice(int price) {
        this.price = price;
    }

    public String getWeb() {
        return web;
    }

    void setWeb(String web) {
        this.web = web;
    }

    public String getImage() {
        return image;
    }

    void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(Steam o) {
        return Integer.compare(this.id, o.id);
    }
}
