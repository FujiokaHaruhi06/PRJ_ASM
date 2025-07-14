package Entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Account_Role")
public class Account_Role implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "aid", referencedColumnName = "aid")
    private Account account;

    @Id
    @ManyToOne
    @JoinColumn(name = "rid", referencedColumnName  = "rid")
    private Role role;

    public Account_Role() {}

    public Account_Role(Account account, Role role) {
        this.account = account;
        this.role = role;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account_Role that = (Account_Role) o;
        return account.equals(that.account) && role.equals(that.role);
    }

    @Override
    public int hashCode() {
        return account.hashCode() + role.hashCode();
    }
} 