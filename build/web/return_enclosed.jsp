<%-- Document : return_enclosed Created on : 04 16, 23, 12:05:37 AM Author : ccslearner --%>

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
                    <jsp:useBean id="C" class="rentals.returN" scope="session" />
                    <br>
                    <h2>Return Rental</h2><br>
                    <form action="return_enclosed_processing.jsp">
                        <% String receive=request.getParameter("enclosed"); String hatdog[]=receive.split(",");
                            C.splitValue.clear(); C.splitValueName.clear(); for (int i=0;i<hatdog.length;i++){
                            C.splitValue.add(hatdog[i]); } C.getName(); %>
                            <table style="width:100%">
                                <tr>
                                    <th>Asset</th>
                                    <th>Inspection Details</th>
                                    <th>Assessed Value for Damages</th>
                                </tr>
                                <% for (int i=1;i<C.splitValueName.size();i++){ %>
                                    <tr>
                                        <td style="text-align:center">
                                            <%=C.splitValueName.get(i)%>
                                        </td>
                                        <td style="text-align:center"><textarea
                                                name="inspection_details <%=C.splitValueName.get(i)%>"
                                                id="inspection_details" required> </textarea></td>
                                        <td style="text-align:center"><input type="number" id="assessed_value"
                                                value="<%=C.assessed_value%>"
                                                name="assessed_value <%=C.splitValueName.get(i)%>" placeholder="0.00"
                                                step="0.01" min="0" max="9999999.99" required> </td>
                                    </tr>
                                    <%}%>
                            </table>
                            <input type="hidden" value="<%=C.splitValueName.size()%>" name="size">
                            <input type="submit" value="Update Enclosed Assets" id="Update">
                    </form>
                </div>
            </div>
        </body>

        </html>