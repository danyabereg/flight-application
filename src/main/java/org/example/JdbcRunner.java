package org.example;

import org.example.dao.TicketDao;
import org.example.entity.Ticket;
import org.example.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        TicketDao ticketDao = TicketDao.getInstance();
        Ticket ticket = ticketDao.findById(5L).get();
        System.out.println(ticket);
    }
    public static List<Long> getTicketsByFlightId(Long flightId){
        List<Long> tickets = new ArrayList<>();
        String sql = """
                SELECT * FROM ticket
                WHERE flight_id = ?;
                """;
        try(Connection connection = ConnectionManager.get();
        var statement = connection.prepareStatement(sql)){
            statement.setFetchSize(2);
            statement.setMaxRows(2);
            statement.setQueryTimeout(1);
            statement.setLong(1, flightId);
            var result = statement.executeQuery();
            while (result.next())
                tickets.add(result.getLong("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
    public static List<Long> getFlightsBetween(LocalDateTime start, LocalDateTime end){
        List<Long> flights = new ArrayList<>();
        String sql = """
                SELECT * FROM flight
                WHERE departure_date BETWEEN ? AND ?;
                """;
        try(Connection connection = ConnectionManager.get();
            var statement = connection.prepareStatement(sql)){
            statement.setTimestamp(1, Timestamp.valueOf(start));
            statement.setTimestamp(2, Timestamp.valueOf(end));
            var result = statement.executeQuery();
            while (result.next())
                flights.add(result.getLong("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }
    public static void checkMetaData(){
        try(Connection connection = ConnectionManager.get()) {
            var metadata = connection.getMetaData();
            var catalogs = metadata.getCatalogs();
            while (catalogs.next())
                System.out.println(catalogs.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
