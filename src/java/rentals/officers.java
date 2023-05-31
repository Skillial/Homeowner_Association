/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rentals;

/**
 * @author ccslearner
 */
public class officers {
    public int id;
    public String position;
    public String elecdate;
    public String all_info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getElecdate() {
        return elecdate;
    }

    public void setElecdate(String elecdate) {
        this.elecdate = elecdate;
    }

    public String getInfo() {
        return all_info;
    }

    public void setInfo(String all_info) {
        this.all_info = all_info;
    }


    public officers(int id, String position, String elecdate, String all_info) {
        this.id = id;
        this.position = position;
        this.elecdate = elecdate;
        this.all_info = all_info;
    }
}
