import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "destination")
public class Destination {
    private long destinationId;
    private String country;
    private String city;

    private List<Trip> trips = new ArrayList<>();
    private List<Hotel> hotels = new ArrayList<>();

    /**
     * required by hibernate
     */
    public Destination() {

    }

    public void addTrip(Trip trip){
        getTrips().add(trip);
    }

    public void addHotel(Hotel hotel){
        getHotels().add(hotel);
        hotel.setDestination(this);
    }
    /**
     * Destination may have many trips, relation *-*
     */
    @ManyToMany(mappedBy = "destinations")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    /**
     *
     * Identifier.
     */
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
    }

    @OneToMany(mappedBy = "destination", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    @Basic
    public String getCountry() {
        return country;
    }

    @Basic
    public String getCity() {
        return city;
    }

    @Transient
    public String getDetails(){
        return city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String toString(){
        return city;
    }

}
