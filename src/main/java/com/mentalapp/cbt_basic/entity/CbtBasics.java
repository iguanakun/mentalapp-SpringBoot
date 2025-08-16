package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cbt_basics")
public class CbtBasics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fact")
    private String fact;

    @Column(name = "mind")
    private String mind;

    @Column(name = "body")
    private String body;

    @Column(name = "behavior")
    private String behavior;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "cbtBasics", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CbtBasicsNegativeFeel> cbtBasicsNegativeFeels;
    
    @OneToMany(mappedBy = "cbtBasics", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CbtBasicsPositiveFeel> cbtBasicsPositiveFeels;
    
    @ManyToMany
    @JoinTable(
        name = "cbt_basics_negative_feels",
        joinColumns = @JoinColumn(name = "cbt_basic_id"),
        inverseJoinColumns = @JoinColumn(name = "negative_feel_id")
    )
    private List<NegativeFeel> negativeFeels;
    
    @ManyToMany
    @JoinTable(
        name = "cbt_basics_positive_feels",
        joinColumns = @JoinColumn(name = "cbt_basic_id"),
        inverseJoinColumns = @JoinColumn(name = "positive_feel_id")
    )
    private List<PositiveFeel> positiveFeels;

    public CbtBasics() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public String getMind() {
        return mind;
    }

    public void setMind(String mind) {
        this.mind = mind;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<CbtBasicsNegativeFeel> getCbtBasicsNegativeFeels() {
        return cbtBasicsNegativeFeels;
    }

    public void setCbtBasicsNegativeFeels(List<CbtBasicsNegativeFeel> cbtBasicsNegativeFeels) {
        this.cbtBasicsNegativeFeels = cbtBasicsNegativeFeels;
    }

    public List<CbtBasicsPositiveFeel> getCbtBasicsPositiveFeels() {
        return cbtBasicsPositiveFeels;
    }

    public void setCbtBasicsPositiveFeels(List<CbtBasicsPositiveFeel> cbtBasicsPositiveFeels) {
        this.cbtBasicsPositiveFeels = cbtBasicsPositiveFeels;
    }

    public List<NegativeFeel> getNegativeFeels() {
        return negativeFeels;
    }

    public void setNegativeFeels(List<NegativeFeel> negativeFeels) {
        this.negativeFeels = negativeFeels;
    }

    public List<PositiveFeel> getPositiveFeels() {
        return positiveFeels;
    }

    public void setPositiveFeels(List<PositiveFeel> positiveFeels) {
        this.positiveFeels = positiveFeels;
    }

    @Override
    public String toString() {
        return "CbtBasics{" +
                "id=" + id +
                ", fact='" + fact + '\'' +
                ", mind='" + mind + '\'' +
                ", body='" + body + '\'' +
                ", behavior='" + behavior + '\'' +
                ", user=" + user +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}