/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author FPTSHOP
 */
@Entity
@Table(name = "Leave_Application")
public class Leave_Application implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "lid")
    private Integer lid;
    @Basic(optional = false)
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Basic(optional = false)
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Basic(optional = false)
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Basic(optional = false)
    @Column(name = "reason")
    private String reason;
    @Column(name = "approval_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalTime;
    @JoinColumn(name = "sid", referencedColumnName = "sid")
    @ManyToOne(optional = false)
    private LA_status status;
    @JoinColumn(name = "aid", referencedColumnName = "aid")
    @ManyToOne(optional = false)
    private Account account;
    @JoinColumn(name = "approver_aid", referencedColumnName = "aid")
    @ManyToOne
    private Account approverAccount;

    public Leave_Application() {
    }

    public Leave_Application(Integer lid) {
        this.lid = lid;
    }

    public Leave_Application(Integer lid, Date startDate, Date endDate, Date createTime, String reason) {
        this.lid = lid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createTime = createTime;
        this.reason = reason;
    }

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Date approvalTime) {
        this.approvalTime = approvalTime;
    }

    public LA_status getStatus() {
        return status;
    }

    public void setStatus(LA_status status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getApproverAccount() {
        return approverAccount;
    }

    public void setApproverAccount(Account approverAccount) {
        this.approverAccount = approverAccount;
    }
    
}
