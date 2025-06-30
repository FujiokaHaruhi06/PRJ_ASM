package Entity;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 *
 * @author FPTSHOP
 */
@Embeddable
public class User_RolePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "aid")
    private int aid;
    @Basic(optional = false)
    @Column(name = "rid")
    private int rid;

    public User_RolePK() {
    }

    public User_RolePK(int aid, int rid) {
        this.aid = aid;
        this.rid = rid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) aid;
        hash += (int) rid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User_RolePK)) {
            return false;
        }
        User_RolePK other = (User_RolePK) object;
        if (this.aid != other.aid) {
            return false;
        }
        if (this.rid != other.rid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.User_RolePK[ aid=" + aid + ", rid=" + rid + " ]";
    }
    
} 