<%-- Document : deleteRental Created on : 04 15, 23, 11:03:43 PM Author : ccslearner --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
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
                    <form action="deleteRental_selected.jsp">
                        <jsp:useBean id="B" class="rentals.record" scope="session" />
                        <br>
                        <h2>Delete Rental Information</h2><br>
                        <h3>
                            Asset
                            <select style="float: right" name="Asset_ID" id="Asset_ID" required method="post">
                                <% B.deletable_rentals(); for (int i=0;i<B.rental_idList.size();i++){ %>
                                    <option value="<%=B.rental_idList.get(i)%>">
                                        <%=B.rental_idList.get(i)%>
                                            <%=B.rental_nameList.get(i)%>
                                    </option>
                                    <% } %>
                            </select>
                        </h3><br>
                        <input type="submit" value="Delete">
                    </form>
        </body>

        </html>