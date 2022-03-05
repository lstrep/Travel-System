import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@Entity(name = "person")
public class Person {
    public enum PersonType {Customer, Employee, Guide}

    /**
     * guide specializations
     */
    public enum Specialization {
        Urban, Mountain, Museum, Field;
    }

    private PersonType personType;
    private long personId;
    private String name;
    private String surname;
    private Date dateOfBirth;
    private String address;
    private int phone;
    private String email;
    private int PESEL;

    // from customer
    private int age;
    private double loyaltyPoints;
    private List<Booking> bookings = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private static Set<Review> allReviews = new HashSet<>();

    // from employee and guide
    private Double salary;
    private List<Trip> trips = new ArrayList<>();

    // only for guide
    private Specialization specialization;

    public Person() {
    }

    // from customer
    public void addReview(Review review) throws Exception{
        if(!reviews.contains(review)) {

            if(allReviews.contains(review)) {
                throw new Exception("The part is already connected with a whole!");
            }
            reviews.add(review);
            allReviews.add(review);
        }
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    @Enumerated
    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    @Enumerated
    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPESEL() {
        return PESEL;
    }

    public void setPESEL(int PESEL) {
        this.PESEL = PESEL;
    }
    // from customer
    public void addBooking(Booking booking){
        bookings.add(booking);
    }
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public void calculateAge()
    {
        age = getDateOfBirth().getYear() - LocalDateTime.MAX.getYear();
    }

    public void setLoyaltyPoints(double loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    @Transient
    public int getAge() {
        return Period.between(dateOfBirth.toLocalDate(), LocalDate.now()).getYears();
    }

    public double getLoyaltyPoints() {
        return loyaltyPoints;
    }

    // from employee
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Trip> getTrips() {
    return trips;
}

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }
    public void addTrip(Trip trip){
        getTrips().add(trip);
    }

    public void removeTrip(Trip trip){
        getTrips().remove(trip);
        trip.setPerson(null);
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

   public String toString(){

        if(this.personType.equals(PersonType.Guide))
        {
            return "Name of the guide: " + name + ", Specialization in: " + specialization;
        }else
        return name + " person";
   }

}