package com.websystique.springmvc.model;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "time_record")
public class DBEntry {

    public DBEntry() {

    }

    public DBEntry(long id, long num, String string, String date1) {
        this.date1 = date1;
        this.num = num;
        this.string = string;
        this.id = id;
    }

    public DBEntry(long num, String string, String date1) {
        this.date1 = date1;
        this.num = num;
        this.string = string;
    }

    private static final long serialVersionUID = -3009157732242241606L;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    @Column(name = "num")
    private Long num;

    @Column(name = "string")
    private String string;

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    @Column(name = "date1")
    private String date1;


    public String getString() {
        return string;
    }


    public void setString(String string) {
        this.string = string;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBEntry dbEntry = (DBEntry) o;
        return id == dbEntry.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
