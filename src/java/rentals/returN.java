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


public class returN {
    public String asset_id;
    public String rental_date;
    public String return_date;

    public String enclosed_string;
    public ArrayList<String> splitValue = new ArrayList<>();
    public ArrayList<String> splitValueName = new ArrayList<>();

    public String assessed_value;
    public String inspection_details;
    public String status;
    public String officer_involved;
    public ArrayList<Integer> asset_idList = new ArrayList<>();
    public ArrayList<String> asset_nameList = new ArrayList<>();
    public ArrayList<String> asset_dateList = new ArrayList<>();
    public ArrayList<officers> officer_List = new ArrayList<>();

    public ArrayList<String> allEnclosing = new ArrayList<>();

    public ArrayList<String> iList = new ArrayList<>();
    public ArrayList<String> vList = new ArrayList<>();


    public void clear_array() {
        asset_idList.clear();
        asset_nameList.clear();
        asset_dateList.clear();
        officer_List.clear();
    }

    public void get_asset() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");

            String q = "SELECT ar.asset_id, ar.rental_date, a.asset_name FROM asset_transactions ast JOIN assets a ON a.asset_id = ast.asset_id JOIN asset_rentals ar ON (ar.asset_id = ast.asset_id AND ar.rental_date=ast.transaction_date) WHERE ar.return_date IS NULL AND (ar.status LIKE 'R' OR ar.status LIKE 'O') AND ast.isdeleted = 0;";

            PreparedStatement pstmt = conn.prepareStatement(q);

            ResultSet rst = pstmt.executeQuery();
            clear_array();
            while (rst.next()) {
                int aId = rst.getInt("ar.asset_id");
                asset_idList.add(aId);
                asset_nameList.add(rst.getString("a.asset_name"));
                String ard = rst.getString("ar.rental_date");
                asset_dateList.add(ard);
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
            //System.out.println("connect success");
            PreparedStatement pstmt = conn.prepareStatement("SELECT a.asset_id, a.asset_name FROM assets a JOIN asset_transactions ast ON a.asset_id = ast.asset_id JOIN asset_rentals ar on (ar.asset_id = ast.asset_id AND ar.rental_date=ast.transaction_date) WHERE a.enclosing_asset=? AND ar.rental_date=? AND a.asset_id!=a.enclosing_asset;");
            pstmt.setString(1, assetID);
            pstmt.setString(2, rental_date);
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
        List<String> enclosedAssetList = new ArrayList<>();
        enclosedAssetList = getEnclosedAssets(asset_id);
        Integer[] results = new Integer[enclosedAssetList.size()];
        fill(results, 0);
        int temp = 0, errIndex = -999;
        for (int i = 0; i < enclosedAssetList.size(); i++) {

            asset_id = enclosedAssetList.get(i);
            allEnclosing.add(asset_id);
            results[i] = return_rental();
            errIndex = i;
            if (results[i] != 0) {
                temp = results[i];
                break;
            }

        }
        if (temp == 0) {
            create_pdf_return();
        }
        return temp;
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
            pstmt.setString(2, rental_date);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error Undoing Records. Please contact moderator.");
        }
    }

    public int return_rental() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            System.out.println("Connection Successful");
            PreparedStatement pstmt1 = conn.prepareStatement("UPDATE asset_rentals SET status = 'N', inspection_details = ?, assessed_value = ?, accept_hoid = ?, "
                    + "accept_position=?, accept_electiondate = ?, return_date = ? WHERE asset_id = ? AND rental_date = ?;");
            System.out.println("xd");
            String[] ofc_info = officer_involved.split(" ");
            pstmt1.setString(1, inspection_details);
            pstmt1.setString(2, assessed_value);
            pstmt1.setInt(3, Integer.parseInt(ofc_info[0]));
            pstmt1.setString(4, ofc_info[1]);
            pstmt1.setString(5, ofc_info[2]);
            pstmt1.setString(6, return_date);
            pstmt1.setString(7, asset_id);
            pstmt1.setString(8, rental_date);
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

    public void getName() {
        int size = splitValue.size();
        splitValueName.clear();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            for (int i = 0; i < size; i++) {
                String Query = "SELECT asset_name FROM assets WHERE asset_id=?";
                PreparedStatement pstmt = conn.prepareStatement(Query);
                pstmt.setString(1, splitValue.get(i));
                ResultSet rst = pstmt.executeQuery();
                while (rst.next()) {
                    splitValueName.add(rst.getString("asset_name"));
                }
                rst.close();
                pstmt.close();
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    public int updateEnclosed() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            for (int i = 0; i < iList.size(); i++) {
                String Query = "UPDATE asset_rentals SET inspection_details=?, assessed_value=? WHERE asset_id=? and rental_date=?";
                PreparedStatement pstmt = conn.prepareStatement(Query);
                pstmt.setString(1, iList.get(i));
                pstmt.setString(2, vList.get(i));
                pstmt.setString(3, splitValue.get(i + 1));
                pstmt.setString(4, rental_date);
                pstmt.executeUpdate();
                pstmt.close();
                create_pdf_enclosed(iList.get(i), vList.get(i), splitValue.get(i + 1));
            }
            iList.clear();
            vList.clear();
            conn.close();
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public void create_pdf_return() {
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
            Phrase phrase2 = new Phrase("Asset ID " + asset_id + " has been returned!");
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
            phrase1 = new Phrase("Inspection Details: ", font1);
            phrase2 = new Phrase(" " + inspection_details);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Date Returned:", font1);
            phrase2 = new Phrase(" " + return_date);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            String hatdog[] = officer_involved.split(" ");
            phrase1 = new Phrase("Receiving Officer:", font1);
            phrase2 = new Phrase(" " + hatdog[1]);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Assessed Value for Damages:", font1);
            phrase2 = new Phrase(" " + assessed_value);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            for (int i = 1; i < allEnclosing.size(); i++) {
                paragraph1.clear();
                phrase1 = new Phrase("Enclosed Asset " + (i) + ":", font1);
                phrase2 = new Phrase(" " + allEnclosing.get(i) + " has also been returned!");
                paragraph1.add(phrase1);
                paragraph1.add(phrase2);
                document.add(paragraph1);
            }
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void create_pdf_enclosed(String ind, String vd, String id) {
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
            Phrase phrase2 = new Phrase("Asset ID " + id + " has been updated!");
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
            phrase2 = new Phrase(" " + id);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Inspection Details: ", font1);
            phrase2 = new Phrase(" " + ind);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Date Rented:", font1);
            phrase2 = new Phrase(" " + rental_date);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Assessed Value for Damages:", font1);
            phrase2 = new Phrase(" " + vd);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();

            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void main(String args[]) {
        returN b = new returN();
        //b.asset_id = "5007";
        b.rental_date = "2023-06-02";

        b.officer_involved = "9011 Auditor 2022-12-01";
        b.status = "N";
        b.return_date = "2023-06-20";
        b.assessed_value = "0";
        b.inspection_details = "Good";
        b.get_officer();
        for (int i = 0; i < 5; i++)
            System.out.println(b.officer_List.get(i).getInfo());
        b.return_rental();


        List<String> enclosedAssetList = new ArrayList<>();
        enclosedAssetList = b.getEnclosedAssets("5006");
        for (int i = 0; i < enclosedAssetList.size(); i++) {
            System.out.println(enclosedAssetList.get(i));
            b.asset_id = enclosedAssetList.get(i);
            b.return_rental();

        }

    }
}
