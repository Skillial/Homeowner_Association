<%-- Document : register Created on : 04 14, 23, 3:43:16 PM Author : ccslearner --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link rel="stylesheet" href="styles.css">
            <title>ARM: Assets and Rentals Management</title>
        </head>

        <body>
            <div class="container" style="min-height:320px">
                <div class="rectangle" style="padding: 5px; min-height:320px">
                    <br>
                    <h2>Register an Asset</h2><br>
                    <form action="register_processing.jsp">
                        <h3>Asset Name</h3>
                        <input type="text" id="asset_name" name="asset_name" required maxlength="45"><br><br>
                        <h3>Asset Description</h3>
                        <input type="text" id="asset_description" name="asset_description" maxlength="45"
                            required><br><br>
                        <h3>
                            Acquisition Date
                            <input style="float: right;" type="date" id="asset_acquisition" name="asset_acquisition"
                                required>
                        </h3>
                        <h3>
                            <input type="checkbox" id="for_rent" name="for_rent" value="1">
                            For Rent
                        </h3>
                        <h3>
                            Asset Value
                            <input style="float: right" type="number" id="asset_value" name="asset_value"
                                placeholder="0.00" step="0.01" max="9999999.99" required />
                        </h3>
                        <h3>
                            Asset Type
                            <select style="float: right" name="type_asset" id="type_asset" required>
                                <option value="P"> Property</option>
                                <option value="E"> Equipment</option>
                                <option value="F"> Furniture and Fixtures</option>
                                <option value="O"> Other Assets</option>
                            </select>
                        </h3>
                        <h3>
                            Status
                            <select style="float: right" name="status" id="status" required>
                                <option value="W"> Working Condition</option>
                                <option value="D"> Deteriorated</option>
                                <option value="P"> For Repair</option>
                                <!--<option value ="S"> For Disposal</option>-->
                                <!--                <option value ="X"> Disposed</option>-->
                            </select>
                        </h3>
                        <h3>
                            Location Latitude
                            <input style="float: right" type="number" id="loc_lat" name="loc_lat" placeholder="0.0000"
                                step="0.0001" min="-90.0000" max="90.0000" required />
                        </h3>
                        <h3>
                            Location Longitude
                            <input style="float: right" type="number" id="loc_long" name="loc_long" placeholder="0.0000"
                                step="0.0001" min="-180.0000" max="180.0000" required />
                        </h3>
                        <h3>
                            HOA Name
                            <select style="float: right" name="HOA_Name" id="HOA_Name" required>
                                <jsp:useBean id="A" class="assetmgmt.assets" scope="session" />
                                <% A.get_hoa(); for (int i=0;i<A.HOA_NameList.size();i++){ %>
                                    <option value="<%=A.HOA_NameList.get(i)%>">
                                        <%=A.HOA_NameList.get(i)%>
                                    </option>
                                    <% } %>
                            </select>
                        </h3>
                        <h3>
                            Enclosing Asset
                            <select style="float: right" name="Enclosing_Asset" id="Enclosing_Asset" required>
                                <option value="NULL"> None</option>
                                <% A.get_asset(); for (int i=0;i<A.asset_idList.size();i++){ %>
                                    <option value="<%=A.asset_idList.get(i)%>">
                                        <%=A.asset_nameList.get(i)%>
                                            <%=A.asset_idList.get(i)%>
                                    </option>
                                    <% } %>
                            </select>
                        </h3><br>
                        <input type="submit" value="Register new Asset">
                    </form>
                </div>
            </div>
        </body>

        </html>