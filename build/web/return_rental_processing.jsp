<%-- Document : return_rental_processing Created on : 04 15, 23, 2:39:49 PM Author : ccslearner --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <%@page import="java.util.*, rentals.*" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>Return Rental Processing</title>
                <link rel="stylesheet" href="styles.css">
                <title>ARM: Assets and Rentals Management</title>
            </head>

            <body>
                <div class="container" style>
                    <div class="rectangle" style="padding: 5px">
                        <jsp:useBean id="C" class="rentals.returN" scope="session" />
                        <br><br><br><br><br><br><br><br><br>
                        <% C.officer_involved=request.getParameter("officer_info"); String
                            combinedAsset=request.getParameter("asset_id"); String hatdog[]=combinedAsset.split(",");
                            C.asset_id=hatdog[1]; C.rental_date=hatdog[0]; //to update (removed in main html)
                            C.assessed_value=request.getParameter("assessed_value");
                            C.inspection_details=request.getParameter("inspection_details");
                            C.return_date=request.getParameter("return_date"); //
                            C.status=request.getParameter("status1"); %>

                            <% int success=C.caller(); if (success==0){ %>
                                <% if (C.allEnclosing.size()==1) {%>
                                    <h1 style="text-align:center">Returning Rental Successful</h1>
                                    <% C.allEnclosing.clear();%>
                                        <% }else{ %>
                                            <form action="return_enclosed.jsp">

                                                <h1 style="text-align:center">Returning Rental Successful</h1>
                                                <input type="hidden" id="enclosed" name="enclosed"
                                                    value="<%for (int i=0;i<C.allEnclosing.size();i++){%><%=C.allEnclosing.get(i)%>,<% }%>">
                                                <br>
                                                <% C.allEnclosing.clear();%>
                                                    <input type="submit" value="Manual Input" id="Update">
                                            </form>
                                            <% }}if (success==-1){ %>

                                                <h1 style="text-align:center; color: red">Registering Rental
                                                    Failed<br>Recheck your inputs</h1>
                                                <h3 style="text-align:center">The OR number may have been used<br>or the
                                                    asset has had a transaction already today</h3>
                                                <% } %>
                                                    <% if (success==-2){ %>

                                                        <h1 style="text-align:center; color: red">Registering Rental
                                                            Failed, try again</h1>
                                                        <% } %>


                                                            <% if (success==-10){ %>

                                                                <h1 style="text-align:center; color: red">Registering
                                                                    Rental Failed!</h1>
                                                                <h3 style="text-align:center">Reservation Date CANNOT be
                                                                    before Transaction Date</h3>
                                                                <% } %>
                                                                    <br><br><br><br><br><br><br><br><br>
                                                                    <form action="index.html">
                                                                        <input type="submit"
                                                                            value="Return to Main Menu">
                                                                    </form>
                    </div>
                </div>
            </body>

            </html>