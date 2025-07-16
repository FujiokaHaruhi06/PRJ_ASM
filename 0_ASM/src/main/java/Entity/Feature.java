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
@Table(name = "Feature")
public class Feature implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "fid")
    private Integer fid;
    @Basic(optional = false)
    @Column(name = "fname")
    private String fname;
    @Basic(optional = false)
    @Column(name = "link")
    private String link;
    @Column(name = "description")
    private String description;
    @Column(name = "isActive")
    private boolean isActive = true;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feature")
    private List<Role_Feature> roleFeatureList;

    public Feature() {
    }

    public Feature(Integer fid) {
        this.fid = fid;
    }

    public Feature(Integer fid, String fname, String link) {
        this.fid = fid;
        this.fname = fname;
        this.link = link;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<Role_Feature> getRoleFeatureList() {
        return roleFeatureList;
    }

    public void setRoleFeatureList(List<Role_Feature> roleFeatureList) {
        this.roleFeatureList = roleFeatureList;
    }
    
}
