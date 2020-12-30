package com.backbase.oss.boat.bay.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Dashboard.
 */
@Entity
@Table(name = "dashboard")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dashboard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "sub_title")
    private String subTitle;

    @Column(name = "nav_title")
    private String navTitle;

    @Lob
    @Column(name = "content")
    private String content;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Portal defaultPortal;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Dashboard name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public Dashboard title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public Dashboard subTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getNavTitle() {
        return navTitle;
    }

    public Dashboard navTitle(String navTitle) {
        this.navTitle = navTitle;
        return this;
    }

    public void setNavTitle(String navTitle) {
        this.navTitle = navTitle;
    }

    public String getContent() {
        return content;
    }

    public Dashboard content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Portal getDefaultPortal() {
        return defaultPortal;
    }

    public Dashboard defaultPortal(Portal portal) {
        this.defaultPortal = portal;
        return this;
    }

    public void setDefaultPortal(Portal portal) {
        this.defaultPortal = portal;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dashboard)) {
            return false;
        }
        return id != null && id.equals(((Dashboard) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dashboard{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", title='" + getTitle() + "'" +
            ", subTitle='" + getSubTitle() + "'" +
            ", navTitle='" + getNavTitle() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
