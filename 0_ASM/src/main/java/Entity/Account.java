package Entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Account")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aid")
    private Integer aid;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "gid", referencedColumnName = "gid")
    private Group group;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(name = "mgrid", referencedColumnName = "aid")
    private Account manager;

    public Account() {}

    // Getters and setters
    public Integer getAid() { return aid; }
    public void setAid(Integer aid) { this.aid = aid; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Account getManager() { return manager; }
    public void setManager(Account manager) { this.manager = manager; }
} 