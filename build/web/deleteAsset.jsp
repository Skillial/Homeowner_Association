<%-- Document : deleteAsset Created on : 04 15, 23, 12:32:51 AM Author : ccslearner --%>

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
                    <br>
                    <h2>Delete Asset</h2><br>
                    <form action="deleteAsset_processing.jsp">
                        <jsp:useBean id="A" class="assetmgmt.assets" scope="session" />
                        <h3>
                            Asset ID
                            <select style="float: right" name="Asset_ID" id="Asset_ID" required>
                                <% A.deletable_asset(); for (int i=0;i<A.asset_idList.size();i++){ %>
                                    <option value="<%=A.asset_idList.get(i)%>">
                                        <%=A.asset_nameList.get(i)%>
                                            <%=A.asset_idList.get(i)%>
                                    </option>
                                    <% } %>
                            </select>
                        </h3><br>
                        <input type="submit" value="Delete">
                    </form>
                </div>
            </div>
        </body>

        </html>