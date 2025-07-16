/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 *
 * @author FPTSHOP
 */
@Entity
@Table(name = "Division")
public class Division implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "divid")
    private Integer divid;
    @Basic(optional = false)
    @Column(name = "divname")
    private String divname;
    @JoinColumn(name = "headid", referencedColumnName = "aid")
    @ManyToOne
    private Account head;

    public Division() {
    }

    public Division(Integer divid) {
        this.divid = divid;
    }

    public Division(Integer divid, String divname) {
        this.divid = divid;
        this.divname = divname;
    }

    public Integer getDivid() {
        return divid;
    }

    public void setDivid(Integer divid) {
        this.divid = divid;
    }

    public String getDivname() {
        return divname;
    }

    public void setDivname(String divname) {
        this.divname = divname;
    }

    public Account getHead() {
        return head;
    }

    public void setHead(Account head) {
        this.head = head;
    }
    
}
