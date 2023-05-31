<%-- Document : deleteRental_processing Created on : 04 16, 23, 1:59:08 AM Author : ccslearner --%>

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
                        <jsp:useBean id="B" class="rentals.record" scope="session" />
                        <br><br><br><br><br><br><br><br><br>
                        <% B.asset_id=request.getParameter("Asset_ID");
                            B.rental_date=request.getParameter("Rental_Date"); boolean status=B.delete_rental(); if
                            (status){ %>
                            <h1 style="text-align: center">Asset Rental Information Deleted</h1>
                            <% }else{ %>

                                <h1 style="text-align: center; color: red">Asset Rental Information Deletion Failed</h1>
                                <% } %>
                                    <br><br><br><br><br><br><br><br><br>
                                    <form action="index.html">
                                        <input type="submit" value="Return to Main Menu">
                                    </form>
                    </div>
                </div>
            </body>

            </html>