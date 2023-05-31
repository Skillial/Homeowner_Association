<%-- Document : record_rental Created on : 04 14, 23, 9:38:53 PM Author : ccslearner --%>

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
                        <form action="return_rental_processing.jsp">
                            <jsp:useBean id="C" class="rentals.returN" scope="session" />
                            <br>
                            <h2>Return Rental</h2><br>
                            <!renting 1 asset will rent ALL inclusion assets. Same with returning them,
                                but "some of the information upon returning needs to be encoded manually by the accepting officer.">
                                <h3>
                                    Asset ID
                                    <select style="float:right" name="asset_id" id="asset_id" required>
                                        <% C.get_asset(); for (int i=0;i<C.asset_idList.size();i++){ %>
                                            <option value="<%=C.asset_dateList.get(i)%>,<%=C.asset_idList.get(i)%>">
                                                <%=C.asset_nameList.get(i)%>
                                                    <%=C.asset_idList.get(i)%>
                                                        <%=C.asset_dateList.get(i)%>
                                            </option>
                                            <% } %>
                                    </select>
                                </h3>
                                <!Original Transaction Date: <input type="date" id="transac_date" name="transac_date"
                                    required>

                                    <!-- Status: <select id="status1" name="status1" required>
                            <option value="R"> Reserved</option>
                            <option value="C"> Canceled</option>
                            <option value="O"> On Rent</option>
                            <option value="N"> Returned</option>
                        </select> <br>-->
                                    <h3>
                                        Inspection Details
                                        <textarea style="float:right" rows=5 name="inspection_details"
                                            id="inspection_details" required> </textarea> <br>
                                    </h3>
                                    <h3>
                                        Date Returned
                                        <input style="float:right" type="date" id="return_date" name="return_date"
                                            required><br>
                                    </h3>
                                    <h3>
                                        Receiving Officer
                                        <select style="float:right" name="officer_info" id="officer_info" required>
                                            <% C.get_officer(); for (int i=0;i<C.officer_List.size();i++){ %>
                                                <option value="<%=C.officer_List.get(i).getInfo()%>">
                                                    <%=C.officer_List.get(i).getId()%>
                                                        <%=C.officer_List.get(i).getPosition()%>
                                                            <%=C.officer_List.get(i).getElecdate()%>
                                                </option>
                                                <% } %>
                                        </select>
                                    </h3>
                                    <h3>
                                        Assessed Value for Damages
                                        <input type="number" id="assessed_value" name="assessed_value"
                                            placeholder="0.00" step="0.01" min="0" max="9999999.99" required>
                                    </h3><br>
                                    <input type="submit" value="Return">
                        </form>
                    </div>
                </div>
            </body>

            </html>