package Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Role_FeaturePK implements Serializable {

    @Column(name = "rid")
    private int rid;

    @Column(name = "fid")
    private int fid;

    public Role_FeaturePK() {
    }

    // Getters, Setters, hashCode, equals

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role_FeaturePK that = (Role_FeaturePK) o;
        return rid == that.rid && fid == that.fid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rid, fid);
    }
} 