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


public class record {

    public String or_num;
    public String res_date;
    public String transac_date;
    public String rental_date;
    public String asset_id;
    public String resident_id;

    public String rent_price;
    public String discount;
    public String status;
    public String officer_involved;
    public ArrayList<Integer> rental_idList = new ArrayList<>();
    public ArrayList<String> rental_nameList = new ArrayList<>();
    public ArrayList<Integer> asset_idList = new ArrayList<>();
    public ArrayList<String> asset_nameList = new ArrayList<>();
    public ArrayList<officers> officer_List = new ArrayList<>();

    public void clear_array() {
        asset_idList.clear();
        asset_nameList.clear();
        officer_List.clear();
        rental_idList.clear();
        rental_nameList.clear();
    }

    public void get_asset() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            PreparedStatement pstmt = conn.prepareStatement("SELECT asset_id, asset_name FROM assets WHERE forrent='1'");
            ResultSet rst = pstmt.executeQuery();
            clear_array();
            while (rst.next()) {
                asset_idList.add(rst.getInt("asset_id"));
                asset_nameList.add(rst.getString("asset_name"));
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

            String query = "SELECT asset_id FROM assets WHERE asset_id NOT IN \n" +
                    "(SELECT a2.asset_id FROM assets a2 JOIN asset_transactions ast ON a2.asset_id=ast.asset_ID\n" +
                    "JOIN asset_rentals ar ON (ast.asset_id=ar.asset_id AND ast.transaction_date=ar.rental_date) WHERE ar.return_date IS NULL AND ast.transaction_date=?) \n" +
                    "AND enclosing_asset IN(SELECT a3.enclosing_asset FROM assets a3 WHERE a3.enclosing_asset IS NOT NULL) AND enclosing_asset=?;";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, rental_date);
            pstmt.setString(2, assetID);

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

    public int caller() {
        System.out.println(asset_id);
        get_res();
        if (!resIDList.contains(resident_id)) {
            return -10;
        }
        if (res_date.compareTo(rental_date) < 0)
            return -10;
//        List<String> enclosedAssetList = new ArrayList<>();
        enclosedAssetList = getEnclosedAssets(asset_id);
//        System.out.println(asset_id);
        Integer[] results = new Integer[enclosedAssetList.size()];
        fill(results, 0);
        int temp = 0, errIndex = -999;
        String asd;
        for (int i = 0; i < enclosedAssetList.size(); i++) {

            asd = enclosedAssetList.get(i);

            results[i] = record_rental(asd);
            errIndex = i;
            if (results[i] != 0) {
                temp = results[i];
                break;
            }
        }
        if (temp != 0) {
            for (int i = 0; i < errIndex; i++) {
                asset_id = enclosedAssetList.get(i);
                undo_records();
            }
        }
        if (temp == 0) {
            create_pdf_record();
            enclosedAssetList.clear();
        }
        return temp;
    }

    public ArrayList<String> resIDList = new ArrayList<>();

    public void get_res() {
        try {
            resIDList.clear();
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            String Query = "SELECT resident_id from residents;";
            PreparedStatement pstmt = conn.prepareStatement(Query);

            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                resIDList.add(rst.getString("resident_id"));
            }
            rst.close();
            pstmt.close();

            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    public void undo_records() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            System.out.println("Connection Successful");
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM asset_rentals WHERE asset_id=? and rental_date=?;");
            pstmt.setString(1, asset_id);
            pstmt.setString(2, rental_date);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = conn.prepareStatement("DELETE FROM asset_transactions WHERE asset_id=? and transaction_date=?;");
            pstmt.setString(1, asset_id);
            pstmt.setString(2, transac_date);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error Undoing Records. Please contact moderator.");
        }
    }

    public int record_rental(String asd) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            System.out.println("Connection Successful");
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO asset_transactions(asset_id, " +
                    "transaction_date, trans_hoid, trans_position, trans_electiondate, isdeleted, approval_hoid, approval_position, approval_electiondate, ornum, transaction_type) \n" +
                    "VALUES (?, ?, ?, ?, ?, 0, ?, ?, ?, ?, 'R')");

            transac_date = rental_date;
            String[] ofc_info = officer_involved.split(" ");
            pstmt.setInt(1, Integer.parseInt(asd));
            pstmt.setString(2, transac_date); //transaction date = res date? can we assume this?
            pstmt.setInt(3, Integer.parseInt(ofc_info[0]));
            pstmt.setString(4, ofc_info[1]);
            pstmt.setString(5, ofc_info[2]);
            pstmt.setNull(6, Types.NULL);
            pstmt.setNull(7, Types.NULL);
            pstmt.setNull(8, Types.NULL);
            if (!or_num.equals("-1"))
                pstmt.setInt(9, Integer.parseInt(or_num));
            else
                pstmt.setNull(9, Types.NULL);
            //pstmt.setString(10, 'R');
            pstmt.executeUpdate();
            pstmt.close();

            PreparedStatement pstmt1 = conn.prepareStatement("INSERT INTO asset_rentals "
                    + "(asset_id, rental_date, reservation_date, resident_id, rental_amount, discount, status, inspection_details, assessed_value, accept_hoid, accept_position, accept_electiondate, return_date) "
                    + "VALUES (?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?)");
            System.out.println("xd");
            pstmt1.setInt(1, Integer.parseInt(asd));
            pstmt1.setString(2, rental_date);
            pstmt1.setString(3, res_date);
            pstmt1.setInt(4, Integer.parseInt(resident_id));
            pstmt1.setString(5, rent_price);
            pstmt1.setString(6, discount);
            pstmt1.setString(7, "R");
            pstmt1.setNull(8, Types.NULL);
            pstmt1.setNull(9, Types.NULL);
            pstmt1.setNull(10, Types.NULL);
            pstmt1.setNull(11, Types.NULL);
            pstmt1.setNull(12, Types.NULL);
            pstmt1.setNull(13, Types.NULL);
            pstmt1.executeUpdate();
            pstmt1.close();
            conn.close();
            System.out.println("Success");
            return 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -2;
        }
    }

    public void deletable_rentals() {


        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            String query = "SELECT DISTINCT a.asset_id, a.asset_name FROM assets a JOIN asset_transactions ast ON a.asset_id = ast.asset_id JOIN asset_rentals ar on (ar.asset_id = ast.asset_id AND ar.rental_date=ast.transaction_date) WHERE ast.isdeleted = 0;";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rst = pstmt.executeQuery();
            clear_array();
            while (rst.next()) {
                rental_idList.add(rst.getInt("asset_id"));
                rental_nameList.add(rst.getString("asset_name"));
            }
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public ArrayList<String> get_deletable_rental_dates(String asd) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            String query = "SELECT transaction_date FROM asset_transactions WHERE asset_id = ? AND isdeleted = 0";
            ArrayList<String> dates = new ArrayList<>();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, asd);
            ResultSet rst = pstmt.executeQuery();
            clear_array();
            while (rst.next()) {
                dates.add(rst.getString("transaction_date"));
            }
            pstmt.close();
            conn.close();
            return dates;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<String> enclosedAssetList = new ArrayList<>();

    public boolean delete_rental() {
        enclosedAssetList.clear();
        String asd;
        enclosedAssetList = getEnclosedAssets(asset_id);
//        System.out.println(enclosedAssetList.size());
        for (int i = 0; i < enclosedAssetList.size(); i++) {
            asd = enclosedAssetList.get(i);
//            asset_id=asd;
//            if (i+1!=enclosedAssetList.size())
//                try{
//                Thread.sleep(750);
//                }catch(Exception e){
//                    System.out.println(e);
//                }
            try {

                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
                String query = "UPDATE asset_transactions SET isdeleted=1, approval_hoid = (SELECT ho_id FROM officer_presidents WHERE election_date = (SELECT MAX(election_date) FROM officer_presidents)), approval_position = (SELECT position FROM officer_presidents WHERE election_date = (SELECT MAX(election_date) FROM officer_presidents)), approval_electiondate = (SELECT election_date FROM officer_presidents WHERE election_date = (SELECT MAX(election_date) FROM officer_presidents)) WHERE asset_id = ? AND DATEDIFF(transaction_date, ?) = 0; ";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, asd);
                pstmt.setString(2, rental_date);
                pstmt.executeUpdate();
                pstmt.close();
                conn.close();


            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        create_pdf_delete();
        enclosedAssetList.clear();
        return true;
    }

    public void create_pdf_record() {
        Document document = new Document();
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
            Phrase phrase2 = new Phrase("Asset ID " + asset_id + " has been rented for " + res_date + "!");
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
            phrase1 = new Phrase("Date for when Reserved:", font1);
            phrase2 = new Phrase(" " + res_date);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Transaction Date:", font1);
            phrase2 = new Phrase(" " + rental_date);
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
            String hatdog[] = officer_involved.split(" ");
            phrase1 = new Phrase("Authorizing Officer:", font1);
            phrase2 = new Phrase(" " + hatdog[1]);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Rental Amount:", font1);
            phrase2 = new Phrase(" " + rent_price);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Discount:", font1);
            phrase2 = new Phrase(" " + discount);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            for (int i = 1; i < enclosedAssetList.size(); i++) {
                paragraph1.clear();
                phrase1 = new Phrase("Enclosed Asset " + (i) + ":", font1);
                phrase2 = new Phrase(" " + enclosedAssetList.get(i) + " has also been rented!");
                paragraph1.add(phrase1);
                paragraph1.add(phrase2);
                document.add(paragraph1);
            }
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void create_pdf_delete() {
        Document document = new Document();
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
            Phrase phrase2 = new Phrase("Rental Information of Asset ID " + asset_id + " has been deleted!");
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
            for (int i = 1; i < enclosedAssetList.size(); i++) {
                paragraph1.clear();
                phrase1 = new Phrase("Enclosed Asset " + (i) + ":", font1);
                phrase2 = new Phrase(" Rental Information of Asset ID " + enclosedAssetList.get(i) + " has also been deleted!");
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
        record b=new record();
        b.or_num = "-1";
        b.res_date = "2023-05-28";
        b.rental_date = "2023-05-11";
        b.asset_id = "5006";
        b.resident_id = "9020";
        b.rent_price = "10000";
        b.discount = "0";
        b.status = "R";
        b.officer_involved = "9011 Auditor 2022-12-01";

        b.get_officer();
        for (int i = 0; i < 5; i++)
            System.out.println(b.officer_List.get(i).getInfo());
        List<String> enclosedAssetList = new ArrayList<>();
        enclosedAssetList = b.getEnclosedAssets("5006");
        for (int i = 0; i < enclosedAssetList.size(); i++) {
            System.out.println(enclosedAssetList.get(i));
            b.asset_id = enclosedAssetList.get(i);
//            b.record_rental();

        }
//        b.resident_id="9020";
//        b.get_res();
//        if(!b.resIDList.contains(b.resident_id)){
//            System.out.println("True");
//        }

    }
}