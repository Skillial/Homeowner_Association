<%-- Document : updateAsset_processing Created on : 04 15, 23, 12:16:45 AM Author : ccslearner --%>

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
                        <jsp:useBean id="A" class="assetmgmt.assets" scope="session" />
                        <% String v_asset_name=request.getParameter("asset_name"); String
                            v_asset_description=request.getParameter("asset_description"); String
                            v_asset_acquisition=request.getParameter("asset_acquisition"); String
                            v_for_rent=request.getParameter("for_rent"); String
                            v_asset_value=request.getParameter("asset_value"); String
                            v_type_asset=request.getParameter("type_asset"); String
                            v_status=request.getParameter("status"); String v_loc_lat=request.getParameter("loc_lat");
                            String v_loc_long=request.getParameter("loc_long"); String
                            v_HOA_Name=request.getParameter("HOA_Name"); String
                            v_Enclosing_Asset=request.getParameter("Enclosing_Asset"); A.asset_name=v_asset_name;
                            A.asset_description=v_asset_description; A.asset_acquisition=v_asset_acquisition; if
                            (v_for_rent!=null){ A.for_rent=v_for_rent; } else{ A.for_rent="0" ; }
                            A.asset_value=v_asset_value; A.type_asset=v_type_asset; A.status=v_status;
                            A.loc_lat=v_loc_lat; A.loc_long=v_loc_long; A.HOA_Name=v_HOA_Name;
                            A.Enclosing_Asset=v_Enclosing_Asset; %>
                            <br><br><br><br><br><br><br><br><br>
                            <% int status=A.update_asset(); if (status==1){ %>

                                <h1 style="text-align: center">Updating Asset Successful</h1>
                                <% }else{ %>

                                    <h1 style="text-align: center; color: red">Updating Asset Failed</h1>
                                    <% } %>
                                        <br><br><br><br><br><br><br><br><br>
                                        <form action="index.html">
                                            <input type="submit" value="Return to Main Menu">
                                        </form>
                    </div>
                </div>
            </body>

            </html>