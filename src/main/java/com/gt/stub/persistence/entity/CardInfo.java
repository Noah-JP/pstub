package com.gt.stub.persistence.entity;

import com.gt.stub.persistence.converter.BooleanConverter;
import com.gt.stub.persistence.converter.LocalDateTimeConverter;
import com.gt.stub.persistence.enums.IssuedBy;
import com.gt.stub.persistence.enums.RegStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by noah on 2017. 5. 20..
 */
@Entity
@Table(name = "CARD_INFO")
public class CardInfo implements Serializable {

    @Id
    @Column(name = "CARD_NO")
    private String cardNo;

    @Column(name = "TOKEN", unique = true, nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "ISSUED_BY", nullable = false)
    private IssuedBy issuedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "REG_STATUS", nullable = false)
    private RegStatus regStatus;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cardInfo", orphanRemoval = true)
    private Owner owner;

    @Column(name = "POINTS", nullable = false)
    private BigDecimal points;

    @Column(name = "EXPIRING_DATE_OF_POINTS", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime expiringDateOfPoints;

    @Column(name = "ISSUED_DATE_OF_POINTS", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime issuedDateOfPoints;

    @Column(name = "EXPIRING_POINTS", nullable = false)
    private BigDecimal expiringPoints;

    @Column(name = "RANK", nullable = false)
    private Integer rank;

    @Column(name = "TOKEN_EXPIRED", nullable = false)
    @Convert(converter = BooleanConverter.class)
    private Boolean tokenExpired;

    @Column(name = "MYPAGE_AUTHENTICATED")
    @Convert(converter = BooleanConverter.class)
    private Boolean mypageAuthenticated;

    @Column(name = "WITHDRAW", nullable = false)
    @Convert(converter = BooleanConverter.class)
    private Boolean withdraw;


    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public IssuedBy getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(IssuedBy issuedBy) {
        this.issuedBy = issuedBy;
    }

    public RegStatus getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(RegStatus regStatus) {
        this.regStatus = regStatus;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    public BigDecimal getExpiringPoints() {
        return expiringPoints;
    }

    public void setExpiringPoints(BigDecimal expiringPoints) {
        this.expiringPoints = expiringPoints;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public LocalDateTime getIssuedDateOfPoints() {
        return issuedDateOfPoints;
    }

    public void setIssuedDateOfPoints(LocalDateTime issuedDateOfPoints) {
        this.issuedDateOfPoints = issuedDateOfPoints;
    }

    public LocalDateTime getExpiringDateOfPoints() {
        return expiringDateOfPoints;
    }

    public void setExpiringDateOfPoints(LocalDateTime expiringDateOfPoints) {
        this.expiringDateOfPoints = expiringDateOfPoints;
    }

    public Boolean getTokenExpired() {
        return tokenExpired;
    }

    public void setTokenExpired(Boolean tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public Boolean getMypageAuthenticated() {
        return mypageAuthenticated;
    }

    public void setMypageAuthenticated(Boolean mypageAuthenticated) {
        this.mypageAuthenticated = mypageAuthenticated;
    }

    public Boolean getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(Boolean withdraw) {
        this.withdraw = withdraw;
    }
}
