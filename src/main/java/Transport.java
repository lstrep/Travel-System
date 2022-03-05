import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "transport")
public class Transport {


    public enum TransportType {
        Bus, Plane, Ferry
    }

    private long transportId;
    private String departureFrom;
    private Date date;
    private String duration;
    private Double price;
    private int numberPeople;
    private TransportType transportType;
    private List<Booking> bookings = new ArrayList<>();

    public Transport() {
    }

    /**
     *
     * Identifier.
     */
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public long getTransportId() {
        return transportId;
    }

    public void setTransportId(long transportId) {
        this.transportId = transportId;
    }

    @OneToMany(mappedBy = "transport", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public String getDepartureFrom() {
        return departureFrom;
    }

    public void setDepartureFrom(String departureFrom) {
        this.departureFrom = departureFrom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getNumberPeople() {
        return numberPeople;
    }

    public void setNumberPeople(int numberPeople) {
        this.numberPeople = numberPeople;
    }

    @Enumerated
    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    @Transient
    public static void getTransportOptions(Date date, String destinantion){

    }
    @Transient
    public void addBooking(Booking booking){
        bookings.add(booking);
        booking.setTransport(this);
    }

    public String toString()
    {
        return "Date of transport: " + date + ", by " + transportType + ", duration: " + duration + ", cost: " + price;
    }
}
