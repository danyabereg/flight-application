package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.UserDto;

import java.io.IOException;

@WebServlet("/session")
public class SessionServlet extends HttpServlet {
    private final static String USER = "user";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession();
        var user = session.getAttribute(USER);
        if(user == null)
            user = UserDto.builder()
                .id(5)
                .email("aaa@aa.com")
                .build();

        session.setAttribute(USER, user);
    }
}
