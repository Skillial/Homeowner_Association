<%-- Document : record_rental Created on : 04 14, 23, 9:38:53 PM Author : ccslearner --%>

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
                    <form action="record_rental_processing.jsp">
                        <jsp:useBean id="B" class="rentals.record" scope="session" />
                        <br>
                        <h2>Record Rental Information</h2><br>
                        <h3>
                            <!renting 1 asset will rent ALL inclusion assets. Same with returning them,
                                but "some of the information upon returning needs to be encoded manually by the accepting officer.">
                                <!ilalagay sa transactions table>
                                    Asset ID
                                    <select style="float: right" name="asset_id" id="asset_id" required>
                                        <% B.get_asset(); for (int i=0;i<B.asset_idList.size();i++){ %>
                                            <option value="<%=B.asset_idList.get(i)%>">
                                                <%=B.asset_nameList.get(i)%>
                                                    <%=B.asset_idList.get(i)%>
                                            </option>
                                            <% } %>
                                    </select>
                        </h3>
                        <h3>
                            OR Number
                            <input style="float: right" type="number" id="or_num" name="or_num" step="1" min="3000000"
                                max="9999999">
                        </h3>
                        <h3>
                            Date for when Reserved
                            <input style="float: right" type="date" id="reserve_date" name="reserve_date" required>
                        </h3>
                        <h3>
                            Transaction Date
                            <input style="float: right" type="date" id="rental_date" name="rental_date" required>
                        </h3>
                        <h3>
                            Resident ID
                            <input style="float: right" type="number" id="resident_id" name="resident_id" step="1"
                                min="0" max="9999" required>
                        </h3>
                        <h3>
                            Authorizing Officer
                            <select style="float: right" name="officer_info" id="officer_info" required>
                                <% B.get_officer(); for (int i=0;i<B.officer_List.size();i++){ %>
                                    <option value="<%=B.officer_List.get(i).getInfo()%>">
                                        <%=B.officer_List.get(i).getId()%>
                                            <%=B.officer_List.get(i).getPosition()%>
                                                <%=B.officer_List.get(i).getElecdate()%>
                                    </option>
                                    <% } %>
                            </select>
                        </h3>
                        <h3>
                            Rental Amount
                            <input style="float: right" type="number" id="rental_amt" name="rental_amt"
                                placeholder="0.00" step="0.01" min="0" max="9999999.99" required>
                        </h3>
                        <h3>
                            Discount
                            <input style="float: right" type="number" id="discount" name="discount" placeholder="0.00"
                                step="0.01" min="0" max="9999999.99" required>

                            <!--        Status: <select id="status1" name="status1" required>
                        <option value="R"> Reserved</option>
                        <option value="C"> Canceled</option>
                        <option value="O"> On Rent</option>
                        <option value="N"> Returned</option>
                    </select> <br>-->
                        </h3><br>
                        <input type="submit" value="Record">
                    </form>
                </div>
            </div>
        </body>

        </html>