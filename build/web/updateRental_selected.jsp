<%-- Document : updateRental_selected Created on : 04 15, 23, 9:49:08 PM Author : ccslearner --%>

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
                        <form action="updateRental_processing.jsp">
                            <jsp:useBean id="U" class="rentals.update" scope="session" />
                            <br>
                            <h2>Update Asset Information</h2><br>
                            <% U.get_info(request.getParameter("asset_info")); %>
                                <h3>
                                    Asset ID: <%=U.asset_id%>
                                </h3>
                                <h3>
                                    Transaction/Rental Date: <%=U.rental_date%>
                                </h3>
                                <h3>
                                    Authorizing Officer
                                    <select style="float: right" name="officer_involved_auth" id="officer_involved_auth"
                                        required>
                                        <%U.get_officer();for (int i=0;i<U.officer_List.size();i++){
                                            if(U.officer_List.get(i).getInfo().equals(U.officer_involved_auth)) { %>
                                            <option value="<%=U.officer_List.get(i).getInfo()%>" selected>
                                                <%=U.officer_List.get(i).getId()%>
                                                    <%=U.officer_List.get(i).getPosition()%>
                                                        <%=U.officer_List.get(i).getElecdate()%>
                                            </option>
                                            <% }else{ %>
                                                <option value="<%=U.officer_List.get(i).getInfo()%>">
                                                    <%=U.officer_List.get(i).getId()%>
                                                        <%=U.officer_List.get(i).getPosition()%>
                                                            <%=U.officer_List.get(i).getElecdate()%>
                                                </option>
                                                <% }} %>
                                    </select>
                                </h3>
                                <h3>
                                    OR Number
                                    <input style="float: right" type="number" id="or_num" name="or_num"
                                        placeholder="<%=U.or_num%>" step="1" value="<%=U.or_num%>"> <br>
                                </h3>
                                <h3>
                                    Reservation Date
                                    <input style="float: right" type="date" id="reservation_date"
                                        name="reservation_date" required value="<%=U.reservation_date%>"> <br>
                                </h3>
                                <h3>
                                    Resident ID
                                    <input style="float: right" type="number" id="resident_id" name="resident_id"
                                        placeholder="<%=U.resident_id%>" step="1" required value="<%=U.resident_id%>">
                                    <br>
                                </h3>
                                <h3>
                                    Rental Amount
                                    <input style="float: right" type="number" id="rental_amt" name="rental_amt"
                                        placeholder="<%=U.rental_amt%>" step="0.01" value="<%=U.rental_amt%>"> <br>
                                </h3>
                                <h3>
                                    Discount
                                    <input style="float: right" type="number" id="discount" name="discount"
                                        placeholder="<%=U.discount%>" step="1" value="<%=U.discount%>"> <br>
                                </h3>
                                <h3>
                                    Status
                                    <select style="float: right" id="status" name="status" required>
                                        <% if (U.status.equals("R")) {%>
                                            <option value="R" selected> Reserved</option>
                                            <option value="C"> Canceled</option>
                                            <option value="O"> On Rent</option>
                                            <option value="N"> Returned</option>
                                            <% } if(U.status.equals("C")){%>
                                                <option value="R"> Reserved</option>
                                                <option value="C" selected> Canceled</option>
                                                <option value="O"> On Rent</option>
                                                <option value="N"> Returned</option>
                                                <% } if(U.status.equals("O")){%>
                                                    <option value="R"> Reserved</option>
                                                    <option value="C"> Canceled</option>
                                                    <option value="O" selected> On Rent</option>
                                                    <option value="N"> Returned</option>
                                                    <% } if(U.status.equals("N")){%>
                                                        <option value="R"> Reserved</option>
                                                        <option value="C"> Canceled</option>
                                                        <option value="O"> On Rent</option>
                                                        <option value="N" selected> Returned</option>
                                                        <% } %>

                                    </select>
                                </h3>
                                <h3>
                                    Inspection Details
                                    <textarea name="inspection_details" id="inspection_details"
                                        placeholder="<%=U.inspection_details%>" value="<%=U.inspection_details%>"
                                        rows=5></textarea>
                                </h3>
                                <h3>
                                    Assessed Value
                                    <input style="float: right" type="number" id="assessed_value" name="assessed_value"
                                        placeholder="<%=U.assessed_value%>" step="0.01" value="<%=U.assessed_value%>">
                                </h3>
                                <h3>
                                    Receiving Officer
                                    <select style="float: right" name="officer_involved_return"
                                        id="officer_involved_return">
                                        <%U.get_officer();int has=-1; for (int i=0;i<U.officer_List.size();i++){
                                            if(U.officer_List.get(i).getInfo().equals(U.officer_involved_return)) { %>
                                            <option value="<%=U.officer_List.get(i).getInfo()%>" selected>
                                                <%=U.officer_List.get(i).getId()%>
                                                    <%=U.officer_List.get(i).getPosition()%>
                                                        <%=U.officer_List.get(i).getElecdate()%>
                                            </option>
                                            <% has=1; }else{ %>
                                                <option value="<%=U.officer_List.get(i).getInfo()%>">
                                                    <%=U.officer_List.get(i).getId()%>
                                                        <%=U.officer_List.get(i).getPosition()%>
                                                            <%=U.officer_List.get(i).getElecdate()%>
                                                </option>
                                                <% }} if(has==-1){ %>
                                                    <option value="None" selected> None Yet </option>
                                                    <% }else{ %>
                                                        <option value="None" selected> None Yet </option>
                                                        <% } %>
                                    </select>
                                </h3>
                                <h3>
                                    Return Date
                                    <input style="float: right" type="date" id="return_date" name="return_date"
                                        value="<%=U.return_date%>">
                                </h3><br>
                                <input type="submit" value="Update" id="Update">
                        </form>
                    </div>
                </div>
            </body>

            </html>