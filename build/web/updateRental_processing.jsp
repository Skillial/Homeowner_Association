<%-- Document : updateRental_processing Created on : 04 15, 23, 10:17:41 PM Author : ccslearner --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <%@page import="java.util.*, rentals.*" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <link rel="stylesheet" href="styles.css">
                <title>ARM: Assets and Rentals Management</title>
            </head>

            <body>
                <div class="container" style>
                    <div class="rectangle" style="padding: 5px">
                        <br><br><br><br><br><br><br><br><br>
                        <jsp:useBean id="U" class="rentals.update" scope="session" />
                        <% //if it is null, it should stay null
                            U.officer_involved_auth=request.getParameter("officer_involved_auth");
                            U.or_num=request.getParameter("or_num"); if(request.getParameter("or_num").equals("0") ||
                            request.getParameter("or_num")==null || request.getParameter("or_num").trim().isEmpty()){
                            U.or_num="-1" ; } else U.or_num=(request.getParameter("or_num"));
                            U.reservation_date=request.getParameter("reservation_date");
                            U.resident_id=request.getParameter("resident_id");
                            U.rental_amt=request.getParameter("rental_amt");
                            U.discount=request.getParameter("discount"); U.status=request.getParameter("status");
                            U.inspection_details=request.getParameter("inspection_details");
                            U.assessed_value=request.getParameter("assessed_value");
                            U.officer_involved_return=request.getParameter("officer_involved_return");
                            U.return_date=request.getParameter("return_date"); %>


                            <% int success=U.update_rental(); if (success==0){ %>

                                <h1 style="text-align: center">Update Rental Successful</h1>
                                <% }if (success==-1){ %>

                                    <h1 style="text-align: center; color: red">Update Failed<br>Recheck your inputs</h1>
                                    <h3 style="text-align: center">The OR number may have been used.</h3>
                                    <% } if (success==-2){ %>
                                        <h1 style="text-align: center; color: red">Update Failed<br>Recheck your inputs
                                        </h1>
                                        <h3 style="text-align: center">A value was inputted wrong</h3>
                                        <% } if (success==-3){ %>
                                            <h1 style="text-align: center; color: red">Updating Failed<br>Try again</h1>
                                            <% } %>


                                                <% if (success==-10){ %>

                                                    <h1 style="text-align: center; color: red">Registering Rental Failed
                                                    </h1>
                                                    <h3 style="text-align: center">Reservation Date CANNOT be before
                                                        Transaction Date</h3>
                                                    <% } %>
                                                        <br><br><br><br><br><br><br><br><br>
                                                        <form action="index.html">
                                                            <input type="submit" value="Return to Main Menu">
                                                        </form>
                    </div>
                </div>
            </body>

            </html>