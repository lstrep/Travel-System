import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity(name = "review")
public class Review {
    private Date date;
    private String reviewNote;
    private Trip trip;
    private Person person;
    private Long idReview;

    private Review(Person person, String reviewNote) {
        this.person = person;
        date = Date.valueOf(LocalDate.now());
        this.reviewNote = reviewNote;
    }

    /**
     * required by hibernate
     */
    public Review() {

    }

    private static Review createReview(Person person, String reviewNote) throws Exception{
        if(person == null){
            throw new Exception("Customer does not exist");
        }

        Review review = new Review(person, reviewNote);

        person.addReview(review);
        return review;
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getIdReview() {
        return idReview;
    }

    public void setIdReview(Long idReview) {
        this.idReview = idReview;
    }

    @ManyToOne
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person customer) {
        this.person = customer;
    }

    @ManyToOne
    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @Basic
    public Date getDate() {
        return date;
    }

    @Basic
    public String getReviewNote() {
        return reviewNote;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setReviewNote(String reviewNote) {
        this.reviewNote = reviewNote;
    }



}
