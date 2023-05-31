package assetmgmt;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author ccslearner
 */

import java.util.*;
import java.sql.*;

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


public class assets {

    public int asset_id;
    public String asset_name;
    public String asset_description;
    public String asset_acquisition;
    public String for_rent;
    public String asset_value;
    public String type_asset;
    public String status;
    public String loc_lat;
    public String loc_long;
    public String HOA_Name;
    public String Enclosing_Asset;
    public ArrayList<Integer> asset_idList = new ArrayList<>();
    public ArrayList<String> asset_nameList = new ArrayList<>();
    public ArrayList<String> HOA_NameList = new ArrayList<>();
    public ArrayList<String> enclosed_assetList = new ArrayList<>();

    public assets() {

    }

    public void clear_array() {
        asset_idList.clear();
        HOA_NameList.clear();
        asset_nameList.clear();
    }

    public void get_hoa() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            PreparedStatement pstmt = conn.prepareStatement("SELECT hoa_name FROM hoa");
            ResultSet rst = pstmt.executeQuery();
            clear_array();
            while (rst.next()) {
                HOA_NameList.add(rst.getString("hoa_name"));
            }
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void get_info(int id_no) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            String query = "SELECT asset_name, asset_description, acquisition_date, forrent, asset_value, type_asset, status, loc_lattitude, loc_longiture, hoa_name, enclosing_asset FROM assets WHERE asset_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id_no);
            ResultSet rst = pstmt.executeQuery();
            clear_array();
            while (rst.next()) {
                asset_name = rst.getString("asset_name");
                asset_description = rst.getString("asset_description");
                asset_acquisition = rst.getString("acquisition_date");
                for_rent = rst.getString("forrent");
                asset_value = rst.getString("asset_value");
                type_asset = rst.getString("type_asset");
                status = rst.getString("status");
                loc_lat = rst.getString("loc_lattitude");
                loc_long = rst.getString("loc_longiture");
                HOA_Name = rst.getString("hoa_name");
                Enclosing_Asset = rst.getString("enclosing_asset");
                if (rst.wasNull()) {
                    Enclosing_Asset = "0";
                }
            }
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void get_asset() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            PreparedStatement pstmt = conn.prepareStatement("SELECT asset_id, asset_name FROM assets WHERE status!='X'");
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

    public void deletable_asset() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            String query = "SELECT asset_id, asset_name FROM assets WHERE asset_id NOT IN (SELECT asset_id FROM asset_activity) AND asset_id NOT IN (SELECT asset_id FROM asset_rentals) ORDER BY asset_id";
            PreparedStatement pstmt = conn.prepareStatement(query);
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


    public int register_asset() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            System.out.println("Connection Successful");
            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(asset_id)+1 AS newID FROM assets");
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                asset_id = rst.getInt("newID");
            }
            pstmt = conn.prepareStatement("INSERT INTO assets (asset_id, asset_name, asset_description, acquisition_date, forrent, asset_value, type_asset, status, loc_lattitude, loc_longiture, hoa_name, enclosing_asset) VALUE(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setInt(1, asset_id);
            pstmt.setString(2, asset_name);
            pstmt.setString(3, asset_description);
            pstmt.setString(4, asset_acquisition);
            pstmt.setString(5, for_rent);
            pstmt.setString(6, asset_value);
            pstmt.setString(7, type_asset);
            pstmt.setString(8, status);
            pstmt.setString(9, loc_lat);
            pstmt.setString(10, loc_long);
            pstmt.setString(11, HOA_Name);
            if ("NULL".equals(Enclosing_Asset)) {
                pstmt.setNull(12, Types.NULL);
            } else {
                pstmt.setString(12, Enclosing_Asset);
            }

            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            create_pdf_record();
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public int update_asset() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            String query = "UPDATE assets SET asset_name=? ,asset_description=?, acquisition_date=?, forrent=?, asset_value=?, type_asset=?, status=?,loc_lattitude=?, loc_longiture = ?, hoa_name = ?, enclosing_asset=? WHERE asset_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setString(1, asset_name);
            pstmt.setString(2, asset_description);
            pstmt.setString(3, asset_acquisition);
            pstmt.setString(4, for_rent);
            pstmt.setString(5, asset_value);
            pstmt.setString(6, type_asset);
            pstmt.setString(7, status);
            pstmt.setString(8, loc_lat);
            pstmt.setString(9, loc_long);
            pstmt.setString(10, HOA_Name);
            if ("NULL".equals(Enclosing_Asset)) {
                pstmt.setNull(11, Types.NULL);
            } else {
                pstmt.setString(11, Enclosing_Asset);
            }
            pstmt.setInt(12, asset_id);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            create_pdf_update();
            return 1;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public int delete_asset() {
        try {
            delete_enclosing();
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            String query = "UPDATE assets SET enclosing_asset=null WHERE asset_id=?;";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, asset_id);
            pstmt.executeUpdate();
            pstmt.close();
            query = "DELETE from assets WHERE asset_id = ?";
            PreparedStatement pstmt1 = conn.prepareStatement(query);
            pstmt1.setInt(1, asset_id);
            pstmt1.executeUpdate();
            pstmt1.close();
            conn.close();

            create_pdf_delete();
            enclosed_assetList.clear();
            return 1;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public int dispose_asset() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            String query = "UPDATE assets SET status='X' WHERE asset_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, asset_id);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
//            Thread.sleep(750);
            delete_enclosing();
            create_pdf_dispose();
            enclosed_assetList.clear();
            return 1;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public void delete_enclosing() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            String query = "SELECT asset_id FROM assets WHERE enclosing_asset=? AND asset_id!=enclosing_asset;";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, asset_id);
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                enclosed_assetList.add(rst.getString("asset_id"));
            }
            rst.close();
            pstmt.close();
            conn.close();
            for (int i = 0; i < enclosed_assetList.size(); i++) {
                delete_enclosed(enclosed_assetList.get(i));
//            get_info(Integer.parseInt(enclosed_assetList.get(i)));
//            asset_id=Integer.parseInt(enclosed_assetList.get(i));
//            if (i+1==enclosed_assetList.size())
//            Thread.sleep(750);
//            create_pdf_update();
//            System.out.println(i);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete_enclosed(String remove) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HOADB?useTimezone=true&serverTimezone=UTC&user=root&password=12345678&useSSL=false");
            String query = "UPDATE assets SET enclosing_asset=null WHERE asset_id=?;";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, remove);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            Phrase phrase2 = new Phrase("Asset ID " + asset_id + " has been deleted!");
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
            for (int i = 0; i < enclosed_assetList.size(); i++) {
                paragraph1.clear();
                phrase1 = new Phrase("Enclosed Asset " + (i + 1), font1);
                phrase2 = new Phrase(": " + enclosed_assetList.get(i) + " has its enclosed assets set to NULL!");
                paragraph1.add(phrase1);
                paragraph1.add(phrase2);
                document.add(paragraph1);
            }
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void create_pdf_dispose() {
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
            Phrase phrase2 = new Phrase("Asset ID " + asset_id + " has been disposed!");
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
            System.out.println(enclosed_assetList.size());
            for (int i = 0; i < enclosed_assetList.size(); i++) {
                paragraph1.clear();
                phrase1 = new Phrase("Enclosed Asset " + (i + 1) + ":", font1);
                phrase2 = new Phrase(" " + enclosed_assetList.get(i) + " has its enclosed assets set to NULL!");
                paragraph1.add(phrase1);
                paragraph1.add(phrase2);
                document.add(paragraph1);
            }
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
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
            Phrase phrase2 = new Phrase("Asset ID " + asset_id + " has been registered!");
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
            phrase1 = new Phrase("Asset Name:", font1);
            phrase2 = new Phrase(" " + asset_name);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Asset Description:", font1);
            phrase2 = new Phrase(" " + asset_description);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Acquisition Date:", font1);
            phrase2 = new Phrase(" " + asset_acquisition);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("For Rent:", font1);
            if (for_rent.equals("1")) {
                phrase2 = new Phrase(" True");
            } else {
                phrase2 = new Phrase(" False");
            }
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Asset Value:", font1);
            phrase2 = new Phrase(" " + asset_value);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Asset Type:", font1);
            if (type_asset.equals("P")) {
                phrase2 = new Phrase(" Property");
            } else if (type_asset.equals("E")) {
                phrase2 = new Phrase(" Equipment");
            } else if (type_asset.equals("F")) {
                phrase2 = new Phrase(" Furniture and Fixtures");
            } else {
                phrase2 = new Phrase(" Other Assets");
            }
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Status:", font1);
            if (status.equals("W")) {
                phrase2 = new Phrase(" Working Condition");
            } else if (status.equals("D")) {
                phrase2 = new Phrase(" Deteriorated");
            } else if (status.equals("P")) {
                phrase2 = new Phrase(" For Repair");
            } else {
                phrase2 = new Phrase(" For Disposal");
            }
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Location Latitude:", font1);
            phrase2 = new Phrase(" " + loc_lat);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Location Longitude:", font1);
            phrase2 = new Phrase(" " + loc_long);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("HOA Name:", font1);
            phrase2 = new Phrase(" " + HOA_Name);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            if (Enclosing_Asset.equals("NULL")) {
                Enclosing_Asset = "None";
            }

            phrase1 = new Phrase("Enclosing Asset:", font1);
            phrase2 = new Phrase(" " + Enclosing_Asset);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create_pdf_update() {
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
            File directory = new File(folder1 + "/" + name);
            PdfWriter.getInstance(document, new FileOutputStream(folder1 + "/" + name));
            document.open();
            Paragraph centeredParagraph = new Paragraph();
            centeredParagraph.setAlignment(Element.ALIGN_CENTER);
            Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.UNDERLINE | Font.BOLD);
            Font font1 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Phrase phrase = new Phrase("HOADB", font);
            centeredParagraph.add(phrase);
            Phrase phrase1 = new Phrase("Action: ", font1);
            Phrase phrase2 = new Phrase("Asset ID " + asset_id + " has been updated!");
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
            phrase1 = new Phrase("Asset Name:", font1);
            phrase2 = new Phrase(" " + asset_name);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Asset Description:", font1);
            phrase2 = new Phrase(" " + asset_description);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Acquisition Date:", font1);
            phrase2 = new Phrase(" " + asset_acquisition);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("For Rent:", font1);
            if (for_rent.equals("1")) {
                phrase2 = new Phrase(" True");
            } else {
                phrase2 = new Phrase(" False");
            }
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Asset Value:", font1);
            phrase2 = new Phrase(" " + asset_value);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Asset Type:", font1);
            if (type_asset.equals("P")) {
                phrase2 = new Phrase(" Property");
            } else if (type_asset.equals("E")) {
                phrase2 = new Phrase(" Equipment");
            } else if (type_asset.equals("F")) {
                phrase2 = new Phrase(" Furniture and Fixtures");
            } else {
                phrase2 = new Phrase(" Other Assets");
            }
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Status:", font1);
            if (status.equals("W")) {
                phrase2 = new Phrase(" Working Condition");
            } else if (status.equals("D")) {
                phrase2 = new Phrase(" Deteriorated");
            } else if (status.equals("P")) {
                phrase2 = new Phrase(" For Repair");
            } else {
                phrase2 = new Phrase(" For Disposal");
            }
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Location Latitude:", font1);
            phrase2 = new Phrase(" " + loc_lat);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("Location Longitude:", font1);
            phrase2 = new Phrase(" " + loc_long);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            phrase1 = new Phrase("HOA Name:", font1);
            phrase2 = new Phrase(" " + HOA_Name);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            if (Enclosing_Asset.equals("NULL")) {
                Enclosing_Asset = "None";
            }
            phrase1 = new Phrase("Enclosing Asset:", font1);
            phrase2 = new Phrase(" " + Enclosing_Asset);
            paragraph3.add(phrase1);
            paragraph3.add(phrase2);
            document.add(paragraph3);
            paragraph3.clear();
            document.close();
            System.out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String args[]) {
        assets A = new assets();
//        A.asset_name="Hatdog";
//        A.asset_description="Jumbo";
//        A.asset_acquisition = "2023-04-09";
//        A.for_rent="1";
//        A.asset_value="96";
//        A.type_asset="F";
//        A.status="W";
//        A.loc_lat="10.435";
//        A.loc_long="10.43";
//        A.HOA_Name="SJH";
//        A.Enclosing_Asset="NULL";
//        A.register_asset();
        A.get_info(5015);
//    System.out.println(A.Enclosing_Asset);
        A.asset_id = 5015;
        A.dispose_asset();
        for (int i = 0; i < A.enclosed_assetList.size(); i++) {
            System.out.println(A.enclosed_assetList.get(i));
            System.out.println(i);
        }
//        
    }

}