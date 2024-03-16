package org.example.dao;

import org.example.entity.Flight;
import org.example.entity.FlightStatus;
import org.example.entity.Ticket;
import org.example.exception.DaoException;
import org.example.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDao implements Dao<Long, Flight>{

    private static final FlightDao INSTANCE = new FlightDao();

    public static FlightDao getInstance() {
        return INSTANCE;
    }

    private final static String SQL_FIND_ALL = """
            SELECT id, flight_no, departure_date, departure_airport_code, arrival_date, arrival_airport_code, aircraft_id, status
            FROM flight
            """;
    private final static String SQL_FIND_BY_ID = SQL_FIND_ALL + """
            WHERE id = ?
            """;
    @Override
    public boolean update(Flight flight) {
        return false;
    }

    @Override
    public List<Flight> findAll(){
        try(var connection = ConnectionManager.get();
            var statement = connection.prepareStatement(SQL_FIND_ALL)) {
            List<Flight> flights = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next())
                flights.add(
                        buildFlight(result)
                );
            return flights;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Flight> findById(Long id) {
        try(Connection connection = ConnectionManager.get()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Flight buildFlight(ResultSet result) throws SQLException {
        return new Flight(result.getLong("id"),
                result.getString("flight_no"),
                result.getTimestamp("departure_date").toLocalDateTime(),
                result.getString("departure_airport_code"),
                result.getTimestamp("arrival_date").toLocalDateTime(),
                result.getString("arrival_airport_code"),
                result.getInt("aircraft_id"),
                FlightStatus.valueOf(result.getString("status"))
        );
    }

    public Optional<Flight> findById(Long id, Connection connection) {
        try(var statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();
            Flight flight = null;
            if (result.next())
                flight = buildFlight(result);
            return Optional.ofNullable(flight);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Flight save(Flight ticket) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
    private FlightDao(){
    }
}
