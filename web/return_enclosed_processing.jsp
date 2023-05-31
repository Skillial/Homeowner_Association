<%-- Document : return_enclosed_processing Created on : 04 16, 23, 1:39:15 AM Author : ccslearner --%>

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
                        <jsp:useBean id="C" class="rentals.returN" scope="session" />
                        <br><br><br><br><br><br><br><br><br>
                        <% int size=Integer.parseInt(request.getParameter("size")); for (int i=1; i < size; i++) {
                            String assetName=C.splitValueName.get(i); String
                            inspectionDetails=request.getParameter("inspection_details " + assetName);
                        String assessedValue = request.getParameter(" assessed_value " + assetName); 
                        C.iList.add(inspectionDetails);
                        C.vList.add(assessedValue);
                    }
                    int status= C.updateEnclosed();
                    if (status==1){
                %>
                <h1 style=" text-align: center">Enclosed Asset Update Successful</h1>
                            <% }else{ %>
                                <h1 style="text-align: center; color: red">Registering Asset Update Failed</h1>
                                <% } %>
                                    <br><br><br><br><br><br><br><br><br>
                                    <form action="index.html">
                                        <input type="submit" value="Return to Main Menu">
                                    </form>
                    </div>
                </div>
            </body>

            </html>