<%-- Document : updateAsset_selected Created on : 04 14, 23, 8:23:21 PM Author : ccslearner --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <%@page import="java.util.*, assetmgmt.*" %>
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
                        <h2>Update Asset Information</h2><br>
                        <form action="updateAsset_processing.jsp">
                            <jsp:useBean id="A" class="assetmgmt.assets" scope="session" />
                            <h3>
                                <% String v_Asset_ID=request.getParameter("Asset_ID");
                                    A.asset_id=Integer.parseInt(v_Asset_ID); %>
                                    Asset ID: <%=A.asset_id%>
                                        <% A.get_info(A.asset_id); %>
                                            <br>Asset Name
                                            <input type="text" id="asset_name" name="asset_name" required maxlength="45"
                                                value="<%=A.asset_name%>">
                            </h3><br>
                            <h3>
                                Asset Description<br>
                                <input type="text" id="asset_description" name="asset_description" required
                                    maxlength="45" value="<%=A.asset_description%>">
                            </h3><br>
                            <h3>
                                Acquisition Date
                                <input type="date" style="float: right" id="asset_acquisition" name="asset_acquisition"
                                    required value="<%=A.asset_acquisition%>">
                            </h3>
                            <h3>

                                <%if(A.for_rent.equals("1")){%>
                                    <input type="checkbox" id="for_rent" name="for_rent" value="1" checked>
                                    <% }else{%>
                                        <input type="checkbox" id="for_rent" name="for_rent" value="1">
                                        <% } %>
                                            For Rent
                            </h3>
                            <h3>
                                Asset Value
                                <input style="float: right" type="number" id="asset_value" name="asset_value"
                                    placeholder="0.00" value="<%=A.asset_value%>" step="0.01" max="9999999.99"
                                    required />
                            </h3>
                            <h3>
                                Asset Type
                                <select name="type_asset" id="type_asset" style="float: right" required>
                                    <% if (A.type_asset.equals("P")) {%>
                                        <option value="P" selected> Property</option>
                                        <option value="E"> Equipment</option>
                                        <option value="F"> Furniture and Fixtures</option>
                                        <option value="O"> Other Assets</option>
                                        <% } if(A.type_asset.equals("E")){%>
                                            <option value="P"> Property</option>
                                            <option value="E" selected> Equipment</option>
                                            <option value="F"> Furniture and Fixtures</option>
                                            <option value="O"> Other Assets</option>
                                            <% } %>
                                                <% if(A.type_asset.equals("F")){%>
                                                    <option value="P"> Property</option>
                                                    <option value="E"> Equipment</option>
                                                    <option value="F" selected> Furniture and Fixtures</option>
                                                    <option value="O"> Other Assets</option>
                                                    <% } %>
                                                        <% if(A.type_asset.equals("O")){%>
                                                            <option value="P"> Property</option>
                                                            <option value="E"> Equipment</option>
                                                            <option value="F"> Furniture and Fixtures</option>
                                                            <option value="O" selected> Other Assets</option>
                                                            <% } %>
                                </select>
                            </h3>
                            <h3>
                                Status
                                <select style="float: right" name="status" id="status" required>
                                    <% if (A.status.equals("W")) {%>
                                        <option value="W" selected> Working Condition</option>
                                        <option value="D"> Deteriorated</option>
                                        <option value="P"> For Repair</option>
                                        <option value="S"> For Disposal</option>
                                        <!--<option value ="X"> Disposed</option>-->
                                        <% } if(A.status.equals("D")){%>
                                            <option value="W"> Working Condition</option>
                                            <option value="D" selected> Deteriorated</option>
                                            <option value="P"> For Repair</option>
                                            <option value="S"> For Disposal</option>
                                            <!--<option value ="X"> Disposed</option>-->
                                            <% } %>
                                                <% if(A.status.equals("P")){%>
                                                    <option value="W"> Working Condition</option>
                                                    <option value="D"> Deteriorated</option>
                                                    <option value="P" selected> For Repair</option>
                                                    <option value="S"> For Disposal</option>
                                                    <!--<option value ="X"> Disposed</option>-->
                                                    <% } %>
                                                        <% if(A.status.equals("S")){%>
                                                            <option value="W"> Working Condition</option>
                                                            <option value="D"> Deteriorated</option>
                                                            <option value="P"> For Repair</option>
                                                            <option value="S selected"> For Disposal</option>
                                                            <!--<option value ="X"> Disposed</option>-->
                                                            <% } %>

                                </select>
                            </h3>
                            <h3>
                                Location Latitude
                                <input style="float: right" type="number" id="loc_lat" name="loc_lat"
                                    placeholder="0.0000" step="0.0001" min="-90.0000" max="90.0000" required
                                    value="<%=A.loc_lat%>" />
                            </h3>
                            <h3>
                                Location Longitude
                                <input style="float: right" type="number" id="loc_long" name="loc_long"
                                    placeholder="0.0000" step="0.0001" min="-180.0000" max="180.0000" required
                                    value="<%=A.loc_long%>" />
                            </h3>
                            <h3>
                                HOA Name
                                <select name="HOA_Name" id="HOA_Name" style="float: right" required>
                                    <% A.get_hoa(); for (int i=0;i<A.HOA_NameList.size();i++){ %>
                                        <option value="<%=A.HOA_NameList.get(i)%>" <%
                                            if(A.HOA_NameList.get(i).equals(A.HOA_Name)) {%> selected <%} %>>
                                                <%=A.HOA_NameList.get(i)%>
                                        </option>
                                        <% } %>
                                </select>
                            </h3>
                            <h3>
                                Enclosing Asset
                                <select style="float: right" name="Enclosing_Asset" id="Enclosing_Asset" required>
                                    <option value="NULL"> None</option>
                                    <% if (!A.Enclosing_Asset.equals("null")){ %>
                                        <% A.get_asset(); for (int i=0;i<A.asset_idList.size();i++){ %>
                                            <option value="<%=A.asset_idList.get(i)%>" <%
                                                if(A.asset_idList.get(i).equals(Integer.parseInt(A.Enclosing_Asset)))
                                                {%> selected <%} %>> <%=A.asset_nameList.get(i)%>
                                                        <%=A.asset_idList.get(i)%>
                                            </option>
                                            <% }} %>
                                </select>
                            </h3><br>
                            <input type="submit" value="Update" id="Update">
                        </form>
                    </div>
                </div>
            </body>

            </html>