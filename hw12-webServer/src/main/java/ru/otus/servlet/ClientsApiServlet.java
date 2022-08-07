package ru.otus.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;

import java.io.IOException;
import java.util.List;


public class ClientsApiServlet extends HttpServlet {

    private static final String PARAM_NAME = "name";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONE = "phone";

    DBServiceClient dbServiceClient;

    public ClientsApiServlet(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter(PARAM_NAME);
        String street = request.getParameter(PARAM_ADDRESS);
        String phoneNumber = request.getParameter(PARAM_PHONE);
        dbServiceClient.saveClient(new Client(null, name, new Address(null, street), List.of(new Phone(null, phoneNumber))));
        response.sendRedirect("/clients");
    }
}
