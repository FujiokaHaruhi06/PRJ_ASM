/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author FPTSHOP
 */
@Entity
@Table(name = "User_Role")
public class User_Role implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected User_RolePK userRolePK;
    @JoinColumn(name = "rid", referencedColumnName = "rid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Role role;
    @JoinColumn(name = "aid", referencedColumnName = "aid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Account account;

    public User_Role() {
    }

    public User_Role(User_RolePK userRolePK) {
        this.userRolePK = userRolePK;
    }

    public User_RolePK getUserRolePK() {
        return userRolePK;
    }

    public void setUserRolePK(User_RolePK userRolePK) {
        this.userRolePK = userRolePK;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userRolePK != null ? userRolePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User_Role)) {
            return false;
        }
        User_Role other = (User_Role) object;
        if ((this.userRolePK == null && other.userRolePK != null) || (this.userRolePK != null && !this.userRolePK.equals(other.userRolePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.User_Role[ userRolePK=" + userRolePK + " ]";
    }
    
}
