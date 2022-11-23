package com.ooush.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ooush.api.entity.enumerables.WeightDenomination;

@Entity
@Table(name = "usersetting")
public class UserSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserId", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(name = "WeightDenomination")
    private WeightDenomination weightDenomination;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public WeightDenomination getWeightDenomination() {
        return weightDenomination;
    }

    public void setWeightDenomination(WeightDenomination weightDenomination) {
        this.weightDenomination = weightDenomination;
    }
}
