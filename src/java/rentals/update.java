package rentals;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.*;

import static java.util.Arrays.fill;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;

/**
 * @author ccslearner
 */


public class update {

    //maybe i can reuse record and returN classes?
    public String asset_id;
    public String rental_date; //aka transaction date

    public String officer_involved_auth;
    public String or_num;

    public String reservation_date;
    public String resident_id;
    public String rental_amt;
    public String discount;
    public String status;

    public String inspection_details;
    public String assessed_value;
    public String officer_involved_return;
    public String return_date;


    public ArrayList<Integer> asset_idList = new ArrayList<>();
    public ArrayList<String> asset_nameList = new ArrayList<>();
    public ArrayList<String> asset_dateList = new ArrayList<>();
    public ArrayList<officers> officer_List = new ArrayList<>();

    public void clear_array() {
        asset_idList.clear();
        asset_nameList.clear();
        asset_dateList.clear();
        officer_List.clear();
    }

    public void nullers() {
        officer_involved_auth = null;
        or_num = null;

        reservation_date = null;
        resident_id = null;
        rental_amt = null;
        discount = null;
        status = null;

        inspection_details = null;
        assessed_value = null;
        officer_involved_return = null;
        return_date = null;
    }

    public void get_asset() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");

            clear_array();
            String s = "SELECT DISTINCT ar.asset_id, ar.rental_date, a.asset_name FROM asset_transactions ast JOIN assets a on a.asset_id = ast.asset_id JOIN asset_rentals ar on (ar.asset_id = ast.asset_id AND ar.rental_date=ast.transaction_date) WHERE ast.isdeleted=0;";
            PreparedStatement pstmt = conn.prepareStatement(s);

            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                asset_idList.add(rst.getInt("ar.asset_id"));
                asset_nameList.add(rst.getString("a.asset_name"));
                asset_dateList.add(rst.getString("ar.rental_date"));
            }
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void get_officer() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            PreparedStatement pstmt = conn.prepareStatement("SELECT ho_id, position, election_date FROM officer WHERE DATEDIFF(end_Date, NOW()) > 0 AND DATEDIFF(NOW(), start_date) > 0;");
            ResultSet rst = pstmt.executeQuery();
            clear_array();
            String temp;
            while (rst.next()) {
                temp = Integer.toString(rst.getInt("ho_id")).concat(" ").concat(rst.getString("position")).concat(" ").concat(rst.getString("election_date"));
                officer_List.add(new officers(rst.getInt("ho_id"), rst.getString("position"), rst.getString("election_date"), temp));

            }
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String> getEnclosedAssets(String assetId) {
        List<String> result = new ArrayList<>();
        result.add(assetId);
        List<String> childAssets = getChildAssets(assetId);
        for (String childAsset : childAssets) {
            result.addAll(getEnclosedAssets(childAsset));
        }

        return result;
    }

    public List<String> getChildAssets(String assetID) {
        List<String> childAssets = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            PreparedStatement pstmt = conn.prepareStatement("SELECT asset_id FROM assets WHERE enclosing_asset = ?");
            pstmt.setString(1, assetID);
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                if (!assetID.equals(rst.getString("asset_id")))
                    childAssets.add(rst.getString("asset_id"));
            }
            rst.close();
            pstmt.close();
            conn.close();


        } catch (Exception e) {
            System.out.println("Error");
        }
        return childAssets;
    }

    public void get_info(String asset_info) {
        String[] split_info = asset_info.split(",");
        asset_id = split_info[1];
        rental_date = split_info[0];
        nullers();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");

            String q = "SELECT ast.ornum, ast.trans_hoid, ast.trans_position, ast.trans_electiondate, ar.reservation_date, ar.resident_id, ar.rental_amount, ar.discount, ar.status, ar.inspection_details, ar.assessed_value, ar.accept_hoid, ar.accept_position, ar.accept_electiondate, ar.return_date\n" +
                    "FROM asset_rentals ar JOIN asset_transactions ast on ar.asset_id = ast.asset_id AND ar.rental_date = ast.transaction_date\n" +
                    "WHERE ar.asset_id = ? AND ar.rental_date = ?;";
            PreparedStatement pstmt = conn.prepareStatement(q);
            pstmt.setString(1, asset_id);
            pstmt.setString(2, rental_date);
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                or_num = rst.getString("ast.ornum");
                String temp = Integer.toString(rst.getInt("ast.trans_hoid")).concat(" ").concat(rst.getString("ast.trans_position")).concat(" ").concat(rst.getString("ast.trans_electiondate"));
                if (temp == null)
                    officer_involved_auth = "None";
                else
                    officer_involved_auth = temp;
                reservation_date = rst.getString("ar.reservation_date");
                resident_id = rst.getString("ar.resident_id");
                rental_amt = rst.getString("ar.rental_amount");
                discount = rst.getString("ar.discount");
                status = rst.getString("ar.status");
                inspection_details = rst.getString("ar.inspection_details");
                assessed_value = rst.getString("ar.assessed_value");
                temp = Integer.toString(rst.getInt("ar.accept_hoid")).concat(" ").concat(rst.getString("ar.accept_position")).concat(" ").concat(rst.getString("ar.accept_electiondate"));

                if (temp == null)
                    officer_involved_return = "None";
                else
                    officer_involved_return = temp;
                return_date = rst.getString("ar.return_date");
                rst.close();
                pstmt.close();
                conn.close();

            }

        } catch (Exception e) {

        }
    }

    public List<String> enclosedAssetList = new ArrayList<>();

    public int update_rental() {
//        System.out.println(enclosedAssetList.size());
        enclosedAssetList.clear();
        enclosedAssetList = getEnclosedAssets(asset_id);
//        List<String> a = getEnclosedAssets(asset_id);
//        System.out.println(a.size());
        System.out.println(enclosedAssetList.size());
        String asd;
        for (int i = 0; i < enclosedAssetList.size(); i++) {
            asd = enclosedAssetList.get(i);
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
                System.out.println("Connection Successful");
                String s = "UPDATE asset_transactions SET ornum=?, trans_hoid=?, trans_position=?, trans_electiondate=? "
                        + "WHERE asset_id = (SELECT asset_id FROM asset_rentals WHERE rental_date = ? AND asset_id = ?)"
                        + "AND   transaction_date = (SELECT rental_date FROM asset_rentals WHERE rental_date = ? AND asset_id = ?)";
                PreparedStatement pstmt = conn.prepareStatement(s);
                String[] ofc_authinfo = officer_involved_auth.split(" ");
                if (!or_num.equals("-1"))
                    pstmt.setInt(1, Integer.parseInt(or_num));
                else
                    pstmt.setNull(1, Types.NULL);
                pstmt.setInt(2, Integer.parseInt(ofc_authinfo[0]));
                pstmt.setString(3, ofc_authinfo[1]);
                pstmt.setString(4, ofc_authinfo[2]);
                pstmt.setString(5, rental_date);
                pstmt.setString(6, asd);
                pstmt.setString(7, rental_date);
                pstmt.setString(8, asd);
                pstmt.executeUpdate();
                pstmt.close();

                s = "UPDATE asset_rentals SET reservation_date=?, resident_id=?, rental_amount=?, discount=?, status=?, inspection_details=?, assessed_value=?, "
                        + "accept_hoid=?, accept_position=?, accept_electiondate=?, return_date=? WHERE rental_date = ? AND asset_id = ?";
                pstmt = conn.prepareStatement(s);
                pstmt.setString(1, reservation_date);
                pstmt.setString(2, resident_id);

                if (rental_amt.isEmpty())
                    pstmt.setNull(3, Types.NULL);
                else
                    pstmt.setString(3, rental_amt);

                if (discount.isEmpty())
                    pstmt.setNull(4, Types.NULL);
                else
                    pstmt.setString(4, discount);
                pstmt.setString(5, status);


                if (inspection_details.isEmpty())
                    pstmt.setNull(6, Types.NULL);
                else
                    pstmt.setString(6, inspection_details);
                if (assessed_value.isEmpty())
                    pstmt.setNull(7, Types.NULL);
                else
                    pstmt.setString(7, assessed_value);

                if (officer_involved_return.equals("None")) {
                    pstmt.setNull(8, Types.NULL);
                    pstmt.setNull(9, Types.NULL);
                    pstmt.setNull(10, Types.NULL);
                } else {
                    String[] ofc_retinfo = officer_involved_return.split(" ");
                    pstmt.setInt(8, Integer.parseInt(ofc_retinfo[0]));
                    pstmt.setString(9, ofc_retinfo[1]);
                    pstmt.setString(10, ofc_retinfo[2]);
                }
                if (return_date.isEmpty())
                    pstmt.setNull(11, Types.NULL);
                else
                    pstmt.setString(11, return_date);
                pstmt.setString(12, rental_date);
                pstmt.setString(13, asd);
                pstmt.executeUpdate();
                pstmt.close();
                conn.close();
                System.out.println("Success " + asd);
                create_pdf_update_rental();
//                enclosedAssetList.clear();
                System.out.println("Success1");
                //return 0;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return -1;
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                return -2;
            } catch (Exception e) {
                return -3;
            }
        }
        return 0;
    }

    public void create_pdf_update_rental() {
        Document document = new Document();
        String[] ofc_authinfo = officer_involved_auth.split(" ");
        String[] ofc_returninfo = officer_involved_return.split(" ");
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
            String folder = System.getProperty("user.home") + "/Desktop/Logs";
            String name = now.format(formatter2) + ".pdf";
            File f = new File(folder);
            f.mkdir();
            String folder1 = folder + "/" + now.format(formatter1);
            File f1 = new File(folder1);
            f1.mkdir();
            PdfWriter.getInstance(document, new FileOutputStream(folder1 + "/" + name));
            document.open();
            Paragraph centeredParagraph = new Paragraph();
            centeredParagraph.setAlignment(Element.ALIGN_CENTER);
            Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.UNDERLINE | Font.BOLD);
            Font font1 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Phrase phrase = new Phrase("HOADB", font);
            centeredParagraph.add(phrase);
            Phrase phrase1 = new Phrase("Action: ", font1);
            Phrase phrase2 = new Phrase("Asset ID " + asset_id + " with transaction date " + rental_date + " has been updated.");
            Phrase phrase3 = new Phrase("Date & Time: ", font1);
            Paragraph paragraph = new Paragraph();
            Paragraph paragraph1 = new Paragraph();
            paragraph.add(phrase1);
            paragraph.add(phrase2);
            document.add(centeredParagraph);
            document.add(paragraph);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            Phrase phrase4 = new Phrase(formattedDateTime);
            paragraph1.add(phrase3);
            paragraph1.add(phrase4);
            document.add(paragraph1);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(new Phrase("---Information---", font1)));
            Paragraph paragraph3 = new Paragraph();
            phrase1 = new Phrase("Asset ID:", font1);
            phrase2 = new Phrase(" " + asset_id);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("OR Number:", font1);
            if (or_num.equals("-1")) {
                phrase2 = new Phrase(" None");
            } else {
                phrase2 = new Phrase(" " + or_num);
            }
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Authorizing Officer:", font1);
            phrase2 = new Phrase(" " + ofc_authinfo[1]);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Reservation Date:", font1);
            phrase2 = new Phrase(" " + reservation_date);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Resident ID:", font1);
            phrase2 = new Phrase(" " + resident_id);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();

            phrase1 = new Phrase("Rental Amount:", font1);
            if (rental_amt.isEmpty())
                phrase2 = new Phrase(" None");
            else
                phrase2 = new Phrase(" " + rental_amt);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();

            phrase1 = new Phrase("Discount:", font1);
            if (discount.isEmpty())
                phrase2 = new Phrase(" None");
            else
                phrase2 = new Phrase(" " + discount);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();

            phrase1 = new Phrase("Status:", font1);
            phrase2 = new Phrase(" " + status);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();

            phrase1 = new Phrase("Inspection Details:", font1);
            if (inspection_details.isEmpty())
                phrase2 = new Phrase(" None");
            else
                phrase2 = new Phrase(" " + inspection_details);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();

            phrase1 = new Phrase("Assessed Value:", font1);
            if (assessed_value.isEmpty())
                phrase2 = new Phrase(" None");
            else
                phrase2 = new Phrase(" " + assessed_value);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();

            phrase1 = new Phrase("Officer Receive:", font1);
            if (officer_involved_return.equals("None"))
                phrase2 = new Phrase(" None");
            else
                phrase2 = new Phrase(" " + ofc_returninfo[1]);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();

            phrase1 = new Phrase("Return Date:", font1);
            if (return_date.isEmpty())
                phrase2 = new Phrase(" None");
            else
                phrase2 = new Phrase(" " + return_date);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            for (int i = 1; i < enclosedAssetList.size(); i++) {
                paragraph1.clear();
                phrase1 = new Phrase("Enclosed Asset " + (i) + ":", font1);
                phrase2 = new Phrase(" " + enclosedAssetList.get(i) + " has also been updated!");
                paragraph1.add(phrase1);
                paragraph1.add(phrase2);
                document.add(paragraph1);
            }


            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        update b = new update();

        b.get_officer();
        for (int i = 0; i < 5; i++)
            System.out.println(b.officer_List.get(i).getInfo());

        b.get_asset();
        for (int i = 0; i < b.asset_dateList.size(); i++)
            System.out.println(b.asset_dateList.get(i));
        System.out.println(b.asset_idList.size());
        b.get_info("2023-04-15,5012");
        b.asset_id = "5012";
        //b.rental_date="2023-03-31";
        //b.rental_date="2023-08-08";
        b.rental_date = "2023-04-15";
        b.inspection_details = "SERVERLY Slightly Damaged";
        b.assessed_value = "100.1";
        //b.officer_involved_auth = "9011 Auditor 2022-12-01";
        b.officer_involved_auth = "9003 Vice-President 2022-12-01";
        b.or_num = "-1";
        b.update_rental();
        System.out.println(b.officer_involved_auth);
        System.out.println(b.officer_involved_return);
        System.out.println(b.assessed_value);


    }
}
