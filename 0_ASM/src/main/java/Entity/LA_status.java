/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author FPTSHOP
 */
@Entity
@Table(name = "LA_status")
public class LA_status implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "sid")
    private Integer sid;
    @Basic(optional = false)
    @Column(name = "sname")
    private String sname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "status")
    private List<Leave_Application> leaveApplicationList;

    public LA_status() {
    }

    public LA_status(Integer sid) {
        this.sid = sid;
    }

    public LA_status(Integer sid, String sname) {
        this.sid = sid;
        this.sname = sname;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public List<Leave_Application> getLeaveApplicationList() {
        return leaveApplicationList;
    }

    public void setLeaveApplicationList(List<Leave_Application> leaveApplicationList) {
        this.leaveApplicationList = leaveApplicationList;
    }
    
}
