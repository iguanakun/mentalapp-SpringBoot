package com.mentalapp.cbt_basic.entity;

import com.mentalapp.common.entity.NegativeFeel;
import com.mentalapp.common.entity.PositiveFeel;
import com.mentalapp.common.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cbt_basics")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"cbtBasicsNegativeFeels", "cbtBasicsPositiveFeels", "negativeFeels", "positiveFeels"})
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

    @Column(name = "user_id", nullable = false)
    private Long user_id;

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
    
    // getUserId() method is kept to maintain naming convention compatibility
    public Long getUserId() {
        return user_id;
    }

    public void setUserId(Long userId) {
        this.user_id = userId;
    }
}