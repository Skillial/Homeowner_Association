<%-- Document : update Created on : 04 14, 23, 8:11:00 PM Author : ccslearner --%>

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
                    <form action="updateAsset_selected.jsp">
                        <jsp:useBean id="A" class="assetmgmt.assets" scope="session" />
                        <br>
                        <h2>Update Asset Information</h2><br>
                        <h3>
                            Asset ID
                            <select style="float: right" name="Asset_ID" id="Asset_ID" required>
                                <% A.get_asset(); for (int i=0;i<A.asset_idList.size();i++){ %>
                                    <option value="<%=A.asset_idList.get(i)%>">
                                        <%=A.asset_nameList.get(i)%>
                                            <%=A.asset_idList.get(i)%>
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