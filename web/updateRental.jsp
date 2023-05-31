<%-- Document : updateRental Created on : 04 15, 23, 6:23:37 PM Author : ccslearner --%>

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
                        <form action="updateRental_selected.jsp">
                            <jsp:useBean id="U" class="rentals.update" scope="session" />
                            <br>
                            <h2>Update Asset Information</h2><br>
                            <h3>
                                Asset Info
                                <select style="float: right" name="asset_info" id="asset_info" required>
                                    <% U.get_asset(); for (int i=0;i<U.asset_idList.size();i++){ %>
                                        <option value="<%=U.asset_dateList.get(i)%>,<%=U.asset_idList.get(i)%>">
                                            <%=U.asset_nameList.get(i)%>
                                                <%=U.asset_idList.get(i)%>
                                                    <%=U.asset_dateList.get(i)%>
                                        </option>
                                        <% } %>
                                </select>
                            </h3><br>
                            <input type="submit" value="Update">
                        </form>
                    </div>
                </div>
            </body>

            </html>