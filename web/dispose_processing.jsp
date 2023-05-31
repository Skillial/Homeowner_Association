<%-- Document : dispose_processing Created on : 04 15, 23, 2:40:07 PM Author : ccslearner --%>

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
                    <br><br><br><br><br><br><br><br><br>
                    <jsp:useBean id="A" class="assetmgmt.assets" scope="session" />
                    <% String v_Asset_ID=request.getParameter("Asset_ID"); A.asset_id=Integer.parseInt(v_Asset_ID); %>
                        <% int status=A.dispose_asset(); if (status==1){ %>

                            <h1 style="text-align: center">Disposing Asset Successful</h1>
                            <% }else{ %>

                                <h1 style="text-align: center; color: red">Disposing Asset Failed</h1>
                                <% } %>
                                    <br><br><br><br><br><br><br><br><br>
                                    <form action="index.html">
                                        <input type="submit" value="Return to Main Menu">
                                    </form>
                </div>
            </div>
        </body>

        </html>