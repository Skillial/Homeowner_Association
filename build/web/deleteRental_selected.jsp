<%-- Document : deleteRental_processing.jsp Created on : 04 15, 23, 11:04:28 PM Author : ccslearner --%>

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
                        <form action="deleteRental_processing.jsp">
                            <jsp:useBean id="B" class="rentals.record" scope="session" />
                            <% String v_Asset_ID=request.getParameter("Asset_ID"); ArrayList<String> dates =
                                B.get_deletable_rental_dates(v_Asset_ID);
                                %>
                                <br>
                                <h2>Delete Rental Information</h2><br>
                                <input type="hidden" name="Asset_ID" id="Asset_ID"
                                    value=<%=request.getParameter("Asset_ID")%>>
                                <h3>
                                    Asset ID:
                                    <%=v_Asset_ID%>
                                </h3>
                                <h3>
                                    Transaction Date
                                    <select style="float:right" name="Rental_Date" id="Rental_Date" required>
                                        <% for (int i=0; i < dates.size(); i++){ %>
                                            <option value="<%=dates.get(i)%>">
                                                <%=dates.get(i)%>
                                            </option>
                                            <% } %>
                                    </select> <br>
                                </h3>
                                <h3>
                                    <input style="text-align:center" type="checkbox" id="Approved" name="Approved"
                                        required>
                                    President's Approval
                                </h3><br>
                                <input type="submit" value="Delete">
                        </form>
                    </div>
                </div>
            </body>

            </html>