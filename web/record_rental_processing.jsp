<%-- Document : record_rental_processing Created on : 04 14, 23, 10:25:34 PM Author : ccslearner --%>

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
                        <br><br><br><br><br><br><br>
                        <jsp:useBean id="B" class="rentals.record" scope="session" />
                        <% B.officer_involved=request.getParameter("officer_info"); 
                            String v_asset_id=request.getParameter("asset_id"); //String
                            String v_ornum=request.getParameter("or_num"); 
                            if(request.getParameter("or_num").equals("0") || request.getParameter("or_num")==null || request.getParameter("or_num").trim().isEmpty()){
                            B.or_num="-1" ; } 
                            else B.or_num=(request.getParameter("or_num")); String
                            v_reserve_date=request.getParameter("reserve_date"); String
                            v_rental_date=request.getParameter("rental_date"); String
                            v_resident_id=request.getParameter("resident_id");
                            B.rent_price=request.getParameter("rental_amt");
                            B.discount=request.getParameter("discount"); // B.status=request.getParameter("status1");
                            B.resident_id=(v_resident_id); B.asset_id=(v_asset_id); B.res_date=v_reserve_date;
                            B.rental_date=v_rental_date; %>

                            <% int success=B.caller(); if (success==0){ %>

                                <h1 style="text-align: center">Registering Rental Successful</h1>
                                <% }if (success==-1){ %>

                                    <h1 style="text-align: center; color: red">Registering Rental Failed<br>Recheck your
                                        inputs</h1>
                                    <h3 style="text-align: center">The OR number may have been used,<br>or the asset has
                                        had a transaction already today.</h3>
                                    <% } %>
                                        <% if (success==-2){ %>

                                            <h1>Registering Rental Failed, try again</h1>
                                            <% } %>


                                                <% if (success==-10){ %>

                                                    <h1 style="text-align: center">Registering Rental Failed!</h1>
                                                    <h3 style="text-align: center">Reservation Date CANNOT be before
                                                        Transaction Date</h3>
                                                    <br>
                                                    <h3 style="text-align: center">Or Resident ID not in database</h3>
                                                    <% } %>
                                                        <br><br><br><br><br><br><br>
                                                        <form action="index.html">
                                                            <input type="submit" value="Return to Main Menu">
                                                        </form>
                    </div>
                </div>
            </body>

            </html>