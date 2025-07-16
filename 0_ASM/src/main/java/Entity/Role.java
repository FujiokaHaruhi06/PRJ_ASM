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
@Table(name = "[Role]")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "rid")
    private Integer rid;
    @Basic(optional = false)
    @Column(name = "rname")
    private String rname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private List<Role_Feature> roleFeatureList;

    public Role() {
    }

    public Role(Integer rid) {
        this.rid = rid;
    }

    public Role(Integer rid, String rname) {
        this.rid = rid;
        this.rname = rname;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public List<Role_Feature> getRoleFeatureList() {
        return roleFeatureList;
    }

    public void setRoleFeatureList(List<Role_Feature> roleFeatureList) {
        this.roleFeatureList = roleFeatureList;
    }
    
}
