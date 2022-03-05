import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Travel_system {

    private JPanel mainPane;
    private JPanel firstPanel;
    private JButton continueAsClientButton;
    private JLabel travelSystemField;
    private JPanel tripsPanel;
    private JPanel detailsPanel;
    private JPanel specificDetailsPanel;
    private JTextField destinationDetailsField;
    private JButton bookWithTransportButton;
    private JButton bookButton;
    private JPanel bookingTransportPanel;
    private JPanel transportToPanel;
    private JPanel transportsPanel;
    private JTextField transportToTextField;
    private JButton bookTransportButton;
    private JPanel endPanel;
    private JButton backButton;
    private JButton backToDetailsButton;
    private JLabel descriptionLabel;
    private JLabel datesLabel;
    private JLabel accommodationLabel;
    private JLabel priceLabel;
    private JLabel guideLabel;
    private JButton continuteAsEmployeeButton;
    private JPanel createPanel;
    private JPanel tabelsPanel;
    private JTable destinationsJTable;
    private JTable hotelsJTable;
    private JPanel dataPanel;
    private JTextField descriptionTextField;
    private JPanel buttonsPanel;
    private JButton createTripButton;
    private JButton backButton1;
    private JPanel buttonsPanel02;
    private JButton backButton2;
    private JTextField startDateTextField;
    private JTextField endDateTextField;
    private JTextField priceTextField;
    private JTextField maxNumTextField;
    private JTable tripsJTable;
    private JTable transportsJTable;
    private JPanel upperPanel;
    private JPanel buttonsTransportPanel;

    private DestinationsTableModel destinationsTableModel;
    private HotelsTableModel hotelsTableModel;
    private TripsTableModel tripsTableModel;
    private TransportsTableModel  transportsTableModel;

    private int indexOfChosenTrip;
    private int indexOfChosenTransport;

    private int selectedRowDestination;
    private int[] selectedRowsHotels;


    public Travel_system() {
        try {
            populateDb();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        /**
         * populating tripsJTable and jumping to trips panel
         */
        continueAsClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                populateTripsJTable();
                jumpToTripsPanel();
            }
        });

        /**
         * jumping to details panel with info displayed based on picked trip
         */
        tripsJTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                if(e.getClickCount() == 2 )
                {
                    indexOfChosenTrip = tripsJTable.getSelectedRow();
                    jumpToDetailsPanel();
                }
            }
        });

        /**
         * loading from db transport options and displaying on panel
         */
        bookWithTransportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                populateTransportJTable();
             jumpToBookingTransportPanel();
            }
        });

        /**
         * going back to trips panel
         */
        backButton.addActionListener(e -> jumpToTripsPanel());

        /**
         * return to details panel
         */
        backToDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jumpToDetailsPanel();
            }
        });

        /**
         * making a booking and jumping to end panel
         */
        bookTransportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createBooking(true);
                jumpToEndPanel();
            }
        });

        /**
         * making a booking with transport and jumping to end panel
         */
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                createBooking(false);
                jumpToEndPanel();
            }
        });

        /**
         * going to employee's creation panel
         */
        continuteAsEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                populateDestinationsJTable();
                jumpToCreatePanel();
            }
        });

        /**
         * back to first panel buttons
         */
        backButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jumpToFirstPanel();
            }
        });

        backButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jumpToFirstPanel();
            }
        });

        /**
         *
         * creating a trip
         */
        createTripButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createTrip();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        /**
         * remembering chosen index of destinations and populating hotelsJTable
         */
        destinationsJTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                selectedRowDestination = destinationsJTable.getSelectedRow();
                populateHotelsJTable();
            }
        });


        /**
         * remembering which hotels were chosen in JList
         */
        hotelsJTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                selectedRowsHotels = hotelsJTable.getSelectedRows();
                System.out.println("Hotel is:" + hotelsTableModel.getSelectedHotels(selectedRowsHotels));
            }
        });

        /**
         * remembering which transport was chosen in JList
         */
        transportsJTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                indexOfChosenTransport = transportsJTable.getSelectedRow();
                System.out.println(indexOfChosenTransport);
            }
        });
    }

    /**
     * setting specific data in detailsPanel based on chosen trip from trips list
     */
    public void setDetailsPanelData(){

        Trip trip = tripsTableModel.getSelectedTrip(indexOfChosenTrip);

        // destinations in the upper corner
        String destinations = trip.getDestinations().toString();
        destinationDetailsField.setText(destinations.substring(1,destinations.length() -1));

        descriptionLabel.setText(trip.getDescription());

        datesLabel.setText("From " + trip.getStartDate().toString() + " To " + trip.getEndDate());

        StringBuilder accommodationText = new StringBuilder();
        List<Accommodation> accommodationsFromATrip = trip.getAccommodations();

        for(Accommodation acc : accommodationsFromATrip){
            accommodationText.append(acc).append(", ");
        }
        accommodationLabel.setText(accommodationText.toString());

        priceLabel.setText(trip.getPrice().toString());

        guideLabel.setText(trip.getPerson().toString());
    }

    /**
     * loading data to destinations JTable
     */
    public void populateDestinationsJTable(){

        List<Destination> destinations = loadDestinationsFromDb();

        destinationsTableModel = new DestinationsTableModel();
        destinationsTableModel.setDestinations(destinations);
        destinationsJTable.setModel(destinationsTableModel);
        destinationsJTable.getTableHeader().setEnabled(false);
    }

    /**
     * populating hotels JTable
     */
    public void populateHotelsJTable()
    {
        Destination destination = destinationsTableModel.getSelectedDestination(selectedRowDestination);
        List<Hotel> hotels = destination.getHotels();

        hotelsTableModel = new HotelsTableModel();
        hotelsTableModel.setHotels(hotels);
        hotelsJTable.setModel(hotelsTableModel);

        hotelsJTable.getTableHeader().setEnabled(false);
    }


    /**
     * populating trips JTable
     */
    public void populateTripsJTable() {

        List<Trip> tripsFromDb = loadTripsFromDb();

        tripsTableModel = new TripsTableModel();
        tripsTableModel.setTrips(tripsFromDb);
        tripsJTable.setModel(tripsTableModel);
        hotelsJTable.getTableHeader().setEnabled(false);
    }



    /**
     * finding transport to the given city
     */
    public void populateTransportJTable()
    {
        Trip trip = tripsTableModel.getSelectedTrip(indexOfChosenTrip);
        // always getting trip with index 0
        String destination = trip.getDestinations().get(0).toString();
        transportToTextField.setText(destination);

        List<Transport> transportOptions = new ArrayList<>();
        for(Transport transport : loadTransportOptionsFromDb() )
        {
            if(transport.getDepartureFrom().equals(destination))
            {
                transportOptions.add(transport);
            }
        }

        transportsTableModel = new TransportsTableModel();
        transportsTableModel.setTransports(transportOptions);
        transportsJTable.setModel(transportsTableModel);

        hotelsJTable.getTableHeader().setEnabled(false);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Travel_system");

        frame.setContentPane(new Travel_system().mainPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setBounds(0, 0, 840,500);
        frame.setMinimumSize(new Dimension(1000,500));
        frame.setMaximumSize(new Dimension(1000,500));
        frame.setPreferredSize(new Dimension(1000,500));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * creating a trip
     * @throws Exception
     */
    public void createTrip() throws Exception {

        String description = descriptionTextField.getText();
        Double price = Double.parseDouble(priceTextField.getText());
        int maxNumOfPpl = Integer.parseInt(maxNumTextField.getText());
        Date startDate = Date.valueOf(LocalDate.now());
        Date endDate = Date.valueOf(LocalDate.now());

        Trip trip = createTrip(description, price, maxNumOfPpl, startDate, endDate);

        List<Destination> destinations = new ArrayList<>();
        destinations.add(destinationsTableModel.getSelectedDestination(selectedRowDestination));
        trip.setDestinations(destinations);

        List<Hotel> chosenHotels = hotelsTableModel.getSelectedHotels(selectedRowsHotels);

        for(Hotel hotel : chosenHotels){
            // get(0) because there is no functionality of choosing a room by client
            // (I assume it will be assigned to him after arrival)
            Accommodation.createAccommodation(trip, startDate, endDate, hotel.getRooms().get(0));
        }

        // guide created here because functionality of choosing guide does not exist
        Person guide1 = createPerson(Person.PersonType.Guide, "Mike", "Jerry",
                Date.valueOf(LocalDate.now()), "address", 123456789, "email" );
        guide1.setSpecialization(Person.Specialization.Museum);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.save(guide1);
            session.save(trip);
            trip.setPerson(guide1);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * creating a booking
     * @param transport
     */
    public void createBooking(Boolean transport)
    {
        Trip trip = tripsTableModel.getSelectedTrip(indexOfChosenTrip);
        Booking booking = createBooking(trip.getStartDate(), trip.getEndDate());

        booking.setTrip(trip);
        // number people one because there is no functionality of creating a trip for more than 1 person
        booking.setNumberPeople(1);

        if(trip.getEndDate().after(Date.valueOf(LocalDate.now()))){
            booking.setState(true);
        }else{
            booking.setState(false);
        }

        booking.setTax(trip.getPrice());
        // Person null because there no functionality of logged employee
        booking.setPerson(null);

        if(!transport) {
            booking.setTransport(null);
            booking.setTotalPrice(calculatePrice(trip));
        }else{
            Transport chosenTransport = transportsTableModel.getSelectedTransport(indexOfChosenTransport);
            booking.setTransport(chosenTransport);
            booking.setTotalPrice(calculatePrice(trip, chosenTransport));
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(booking);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }

    /**
     * calculate price of a trip without transport
     * @param trip
     * @return
     */
    public Double calculatePrice(Trip trip){
        Double totalPrice = trip.getPrice();
        List<Accommodation> accommodationsFromATrip = trip.getAccommodations();
        for(Accommodation acc : accommodationsFromATrip)
        {
            totalPrice += acc.getTotalPrice();
        }
        return totalPrice;
    }

    /**
     * overloaded method with transport to calculate price of a booking with transport
     * @param trip
     * @param transport
     * @return
     */
    public Double calculatePrice(Trip trip, Transport transport){
        Double totalPrice = trip.getPrice();
        List<Accommodation> accommodationsFromATrip = trip.getAccommodations();
        for(Accommodation acc : accommodationsFromATrip)
        {
            totalPrice += acc.getTotalPrice();
        }
        totalPrice+= transport.getPrice();
        return totalPrice;
    }


    /**
     * loading trips from database which are later displayed on first panel
     * @return
     */
    public List<Trip> loadTripsFromDb() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Trip> tripsFromDb = session.createQuery("from Trip").list();
            return tripsFromDb;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    /**
     * loading destination from db
     * @return
     */
    public List<Destination> loadDestinationsFromDb(){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String query = "from Destination";
            List<Destination> destinations = session.createQuery(query).list();
            return  destinations;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
       return null;
    }


    /**
     * loading transport options from db which are later displayed in transport panel
     * @return
     */
    public List<Transport> loadTransportOptionsFromDb() {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String query = "from Transport";
            List<Transport> transportOptionsFromDb = session.createQuery(query).list();
            return transportOptionsFromDb;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }


    /**
     * populating db with start data
     * @throws Exception
     */
    public void populateDb() throws Exception {
        Person employee1 = createPerson(Person.PersonType.Employee, "John", "Smith",
                Date.valueOf(LocalDate.now()), "address", 123456789, "email");

        Transport transport1 = createTransport("London", Date.valueOf(LocalDate.now()),
                "3 hours", 25.0, 25, Transport.TransportType.Bus);
        Transport transport2 = createTransport("London", Date.valueOf(LocalDate.now()),
                "3 hours", 55.0, 15, Transport.TransportType.Plane);
        Transport transport3 = createTransport( "Moscow", Date.valueOf(LocalDate.now()),
                "3 hours", 150.0, 5, Transport.TransportType.Ferry);
        Transport transport4 = createTransport( "Madrid", Date.valueOf(LocalDate.now()),
                "3 hours", 200.0, 12, Transport.TransportType.Plane);

        Destination destination1 = createDestination("England", "London");
        Destination destination2 = createDestination("Spain", "Madrid");
        Destination destination3 = createDestination("Russia", "Moscow");
        Destination destination4 = createDestination("Poland", "Warsaw");

        Hotel hotel1 =  createHotel("Great hotel", 3, "address");
        Hotel hotel2 =  createHotel("Mansion", 2, "address");
        Hotel hotel3 =  createHotel("Strawberry", 4, "address");
        Hotel hotel4 =  createHotel("Super hotel", 5, "address");
        Hotel hotel5 = createHotel("Mariott", 3, "address");
        Hotel hotel6 = createHotel("Hilton", 4, "address");

        Room.createRoom(hotel1, 2, 100.0 );
        Room.createRoom(hotel2, 3, 200.0);
        Room.createRoom(hotel3, 1, 50.0);
        Room.createRoom(hotel4, 2, 150.0);
        Room.createRoom(hotel5, 3,200.0);
        Room.createRoom(hotel6, 4, 300.0);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.save(employee1);

            session.save(destination1);
            session.save(destination2);
            session.save(destination3);
            session.save(destination4);

            session.save(hotel1);
            session.save(hotel2);
            session.save(hotel3);
            session.save(hotel4);
            session.save(hotel5);
            session.save(hotel6);

            session.save(transport1);
            session.save(transport2);
            session.save(transport3);
            session.save(transport4);

            destination1.addHotel(hotel1);
            destination2.addHotel(hotel2);
            destination3.addHotel(hotel3);
            destination1.addHotel(hotel4);
            destination1.addHotel(hotel5);
            destination2.addHotel(hotel6);

            transaction.commit();
            session.close();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Methods to create objects
     * @param personType
     * @param name
     * @param surname
     * @param dateOfBirth
     * @param address
     * @param phone
     * @param email
     * @return
     */
    public Person createPerson(Person.PersonType personType, String name, String surname, Date dateOfBirth, String address, int phone, String email){
        Person person = new Person();
        person.setPersonType(personType);
        person.setName(name);
        person.setSurname(surname);
        person.setDateOfBirth(dateOfBirth);
        person.setAddress(address);
        person.setPhone(phone);
        person.setEmail(email);
        return person;
    }

    public Hotel createHotel(String name, int stars, String address){
        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setNoStars(stars);
        hotel.setAddress(address);
        return hotel;
    }

    public Destination createDestination(String country, String city){
        Destination destination = new Destination();
        destination.setCountry(country);
        destination.setCity(city);
        return  destination;
    }

    public Transport createTransport(String departureFrom, Date date, String duration, Double price, int numberPeople, Transport.TransportType transportType)
    {
        Transport transport = new Transport();
        transport.setTransportType(transportType);
        transport.setPrice(price);
        transport.setNumberPeople(numberPeople);
        transport.setDuration(duration);
        transport.setDate(date);
        transport.setDepartureFrom(departureFrom);
        return transport;
    }

    public Trip createTrip(String description, Double price, int maxNumOfPpl, Date startDate, Date endDate ){
        Trip trip = new Trip();
        trip.setDescription(description);
        trip.setPrice(price);
        trip.setMaxNumOfPeople(maxNumOfPpl);
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);
        return trip;
    }
    public Booking createBooking(Date startDate, Date endDate){
        Booking booking = new Booking();
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        return booking;
    }

    /**
     * methods to change panels
     */

    public void jumpToFirstPanel(){
        mainPane.removeAll();
        mainPane.add(firstPanel);
        mainPane.revalidate();
        mainPane.repaint();
    }
    public void jumpToCreatePanel(){

        mainPane.removeAll();
        mainPane.add(createPanel);
        mainPane.revalidate();
        mainPane.repaint();
    }
    public void jumpToTripsPanel()
    {
        mainPane.removeAll();
        mainPane.add(tripsPanel);
        mainPane.revalidate();
        mainPane.repaint();
    }


    public void jumpToDetailsPanel()
    {
        setDetailsPanelData();
        mainPane.removeAll();
        mainPane.add(detailsPanel);
        mainPane.revalidate();
        mainPane.repaint();
    }

    public void jumpToBookingTransportPanel()
    {
        mainPane.removeAll();
        mainPane.add(bookingTransportPanel);
        mainPane.revalidate();
        mainPane.repaint();
    }

    public void jumpToEndPanel()
    {
        HibernateUtil.shutdown();
        mainPane.removeAll();
        mainPane.add(endPanel);
        mainPane.revalidate();
        mainPane.repaint();
    }

    /**
     * abstract table models
     */
    private static class DestinationsTableModel extends AbstractTableModel{

        private final String[] header ={"Country", "City"};
        private List<Destination> destinations;

        @Override
        public int getRowCount() {
            return destinations.size();
        }

        @Override
        public int getColumnCount() {
            return header.length;
        }


        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return switch (columnIndex) {
                case 0 -> destinations.get(rowIndex).getCountry();
                case 1 -> destinations.get(rowIndex).getCity();
                default -> "--";
            };
        }

        @Override
        public String getColumnName(int column) {
            return header[column];
        }
        public void setDestinations(List<Destination> destinations){
            this.destinations = destinations;
        }
        public Destination getSelectedDestination(int rowIndex){
            return destinations.get(rowIndex);
        }
    }
    private static class HotelsTableModel extends AbstractTableModel{

        private final String header[] = {"Hotel name", "Stars"};
        private List<Hotel> hotels;

        @Override
        public int getRowCount() {
            return hotels.size();
        }

        @Override
        public int getColumnCount() {
            return header.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return switch (columnIndex) {
                case 0 -> hotels.get(rowIndex).getName();
                case 1 -> hotels.get(rowIndex).getNoStars();
                default -> "--";
            };
        }
        @Override
        public String getColumnName(int column) {
            return header[column];
        }

        public void setHotels(List<Hotel> hotels){
            this.hotels = hotels;
        }

        public List<Hotel> getSelectedHotels(int[] rowsIndexes){
            List<Hotel> selectedHotels = new ArrayList();

            for(int i=0; i<rowsIndexes.length; i++)
            {
                selectedHotels.add(hotels.get(rowsIndexes[i]));
            }

            return selectedHotels;
        }
    }


    private static class TripsTableModel extends AbstractTableModel{

        private final String[] header ={"Destination", "Description", "Price", "Max number of people"};
        private List<Trip> trips;

        @Override
        public int getRowCount() {
            return trips.size();
        }

        @Override
        public int getColumnCount() {
            return header.length;
        }


        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return switch (columnIndex) {
                case 0 -> trips.get(rowIndex).getDestinations();
                case 1 -> trips.get(rowIndex).getDescription();
                case 2 -> trips.get(rowIndex).getPrice();
                case 3 -> trips.get(rowIndex).getMaxNumOfPeople();
                default -> "--";
            };
        }

        @Override
        public String getColumnName(int column) {
            return header[column];
        }
        public void setTrips(List<Trip> trips){
            this.trips = trips;
        }
        public Trip getSelectedTrip(int rowIndex){
            return trips.get(rowIndex);
        }
    }

    private static class TransportsTableModel extends AbstractTableModel{

        private final String[] header ={"Date of departure", "Duration", "Price"};
        private List<Transport> transports;

        @Override
        public int getRowCount() {
            return transports.size();
        }

        @Override
        public int getColumnCount() {
            return header.length;
        }


        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return switch (columnIndex) {
                case 0 -> transports.get(rowIndex).getDate();
                case 1 -> transports.get(rowIndex).getDuration();
                case 2 -> transports.get(rowIndex).getPrice();
                default -> "--";
            };
        }

        @Override
        public String getColumnName(int column) {
            return header[column];
        }
        public void setTransports (List<Transport> transports){
            this.transports = transports;
        }
        public Transport getSelectedTransport(int rowIndex){
            return transports.get(rowIndex);
        }
    }
}
