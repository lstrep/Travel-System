import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity(name = "room")
public class Room {
    private long roomNo;
    private int numberOfBeds;
    private Double pricePerNight;
    private List<Accommodation> accommodations;
    private Hotel hotel;

    private Room(Hotel hotel, int numberOfBeds, Double pricePerNight) {
        this.hotel = hotel;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    public Room() {

    }

    public static Room createRoom(Hotel hotel, int numberOfBeds, Double pricePerNight ) throws  Exception{
        if(hotel == null) {
            throw new Exception("The given hotel does not exist!");
        }
        Room room = new Room(hotel,numberOfBeds,pricePerNight);
        hotel.addRoom(room);
        return room;
    }

    /**
     *
     * Identifier.
     */
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public long getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(long roomNo) {
        this.roomNo = roomNo;
    }

    @ManyToOne
    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public void addAccommodation(Accommodation accommodation){
        accommodations.add(accommodation);
    }

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Accommodation> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
}
