package com.gt.stub.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by noah on 2017. 5. 20..
 */
@Entity
@Table(name = "OWNER")
public class Owner implements Serializable {

    @Id
    @Column(name = "CARD_NO")
    private String cardNo;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private CardInfo cardInfo;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NAME_SEI")
    private String nameSei;

    @Column(name = "NAME_MEI")
    private String nameMei;

    @Column(name = "NAME_SEI_KANA")
    private String nameSeiKana;

    @Column(name = "NAME_MEI_KANA")
    private String nameMeiKana;

    @Column(name = "BIRTHDAY")
    private String birthday;

    @Column(name = "SEX")
    private String sex;

    @Column(name = "POST_NO")
    private String postNo;

    @Column(name = "ADDRESS1")
    private String address1;

    @Column(name = "ADDRESS2")
    private String address2;

    @Column(name = "ADDRESS3")
    private String address3;

    @Column(name = "TEL_NO")
    private String telNo;

    @Column(name = "EMAIL")
    private String email;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNameSei() {
        return nameSei;
    }

    public void setNameSei(String nameSei) {
        this.nameSei = nameSei;
    }

    public String getNameMei() {
        return nameMei;
    }

    public void setNameMei(String nameMei) {
        this.nameMei = nameMei;
    }

    public String getNameSeiKana() {
        return nameSeiKana;
    }

    public void setNameSeiKana(String nameSeiKana) {
        this.nameSeiKana = nameSeiKana;
    }

    public String getNameMeiKana() {
        return nameMeiKana;
    }

    public void setNameMeiKana(String nameMeiKana) {
        this.nameMeiKana = nameMeiKana;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPostNo() {
        return postNo;
    }

    public void setPostNo(String postNo) {
        this.postNo = postNo;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
