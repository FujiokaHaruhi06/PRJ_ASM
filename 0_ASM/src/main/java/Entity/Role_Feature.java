package Entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Role_Feature")
public class Role_Feature implements Serializable {

    @EmbeddedId
    private Role_FeaturePK id;

    @ManyToOne
    @JoinColumn(name = "rid", referencedColumnName = "rid", insertable = false, updatable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "fid", referencedColumnName = "fid", insertable = false, updatable = false)
    private Feature feature;

    public Role_Feature() {
    }

    // Getters and Setters

    public Role_FeaturePK getId() {
        return id;
    }

    public void setId(Role_FeaturePK id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }
} 