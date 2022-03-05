import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "hotel")
public class Hotel {
    private long hotelId;
    private String name;
    private int noStars;
    private List<Room> rooms = new ArrayList<>();
    private static Set<Room> allRooms =  new HashSet<>();
    private Destination destination;
    private String address;

    public Hotel() {

    }
    public void addRoom(Room room) throws Exception {
        if(!rooms.contains(room)) {
            if(allRooms.contains(room)){
                throw new Exception("Already exists");
            }
            rooms.add(room);
            allRooms.add(room);
        }
    }

    /**
     *
     * Identifier.
     */
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public long getHotelId() {
        return hotelId;
    }
    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    @OneToMany(mappedBy = "hotel",fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Room> getRooms() {
        return rooms;
    }

    @ManyToOne
    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoStars() {
        return noStars;
    }

    public void setNoStars(int noStars) {
        this.noStars = noStars;
    }

    public String toString(){
        return name;
    }
}
