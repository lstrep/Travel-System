import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "accommodation")
public class Accommodation {
    private long accommodationId;
    private Date startDate;
    private Date endDate;
    private Double totalPrice;
    private Room room;
    private Trip trip;

    private Accommodation(Trip trip,  Date startDate, Date endDate, Room room) {
        this.trip = trip;
        this.startDate = startDate;
        this.endDate = endDate;
        this.room = room;
    }

    /**
     * required by hibernate;
     *
     * */
    public Accommodation() {

    }

    public static Accommodation createAccommodation(Trip trip,  Date startDate, Date endDate, Room room) throws Exception{
        if(trip == null){
            throw new Exception("Given trip does not exist");
        }

        Accommodation accommodation = new Accommodation(trip, startDate, endDate, room);
        accommodation.setTotalPrice(room.getPricePerNight());
        trip.addAccommodation(accommodation);
        return accommodation;
    }

    @ManyToOne
    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public void addRoom(Room room){
        this.room = room;
        room.addAccommodation(this);
    }
    /**
     *
     * Identifier.
     */
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @ManyToOne
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String toString()
    {
        return room.getHotel() +" ("+ totalPrice +" PLN)";
    }
}
