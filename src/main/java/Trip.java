import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.*;

@Entity(name = "trip")
public class Trip {
    private long tripId;
    private String description;
    private Double price;
    private int maxNumOfPeople;
    private Date startDate;
    private Date endDate;
    private Person person;
    private List<Destination> destinations = new ArrayList<>();
    private List<Review> reviews;
    private List<Accommodation> accommodations = new ArrayList<>();
    private static Set<Accommodation> allAccommodations = new HashSet<>();
    private List<Booking> bookings = new ArrayList<>();

    /**
     * Required by Hibernate
     */
    public Trip() {

    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setTrip(this);
    }

    public void setAccommodations(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    public static Set<Accommodation> getAllAccommodations() {
        return allAccommodations;
    }

    /**
     * Trip may have many bookings. Relation 1-*. (aggregation)
     */
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    /**
     * Trip may have many accommodations. Relation 1-*. (composition)
     */
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public List<Accommodation> getAccommodations() {
        return accommodations;
    }

    public void addAccommodation(Accommodation accommodation) throws Exception {
        if (!accommodations.contains(accommodation)) {
            if (allAccommodations.contains(accommodation)) {
                throw new Exception("Already exists");
            }
            accommodations.add(accommodation);
            allAccommodations.add(accommodation);
        }
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setTrip(this);
    }

    /**
     * Identifier.
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }
    /**
     * Trip may have many reviews. Relation 1-*.
     */
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    public void addDestination(Destination destination) {
        destinations.add(destination);
        destination.addTrip(this);
    }

    /**
     * Trip may have many destinations. Relation *-*.
     */
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "Trip_Destination",
            joinColumns = {@JoinColumn(name = "tripId")},
            inverseJoinColumns = {@JoinColumn(name = "destinationId")}
    )
    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    /**
     * Trip has one employee. relation *-1.
     */
    @ManyToOne
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }


    /**
     * defining attributes to be mapped. simple types.
     */
    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * defining attributes to be mapped. simple types.
     */
    @Basic
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * defining attributes to be mapped. simple types.
     */
    @Basic
    public int getMaxNumOfPeople() {
        return maxNumOfPeople;
    }

    public void setMaxNumOfPeople(int maxNumOfPeople) {
        this.maxNumOfPeople = maxNumOfPeople;
    }

    /**
     * defining attributes to be mapped. simple types.
     */
    @Basic
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * defining attributes to be mapped. simple types.
     */
    @Basic
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Transient
    public String toString() {
        return "Destination: " + getDestinations() + ", price: " + price;
    }
}
