package ufrgs.network.manager.data;

/**
 * Created by lucas on 12/4/16.
 */
public class NetworkInterface {

    private String description;
    private Integer speed;
    private Integer inOctets;
    private Integer outOctets;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getInOctets() {
        return inOctets;
    }

    public void setInOctets(Integer inOctets) {
        this.inOctets = inOctets;
    }

    public Integer getOutOctets() {
        return outOctets;
    }

    public void setOutOctets(Integer outOctets) {
        this.outOctets = outOctets;
    }

}
