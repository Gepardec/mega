package com.gepardec.mega.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Employee {

    public abstract String userId();
    public abstract String email();
    public abstract String title();
    public abstract String firstName();
    public abstract String sureName();
    public abstract String salutation();
    public abstract String releaseDate();
    public abstract String workDescription();
    public abstract Integer role();
    public abstract boolean active();

    public static Builder builder() {
        return new com.gepardec.mega.domain.AutoValue_Employee.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder userId(String userId);
        public abstract Builder email(String email);
        public abstract Builder title(String title);
        public abstract Builder firstName(String firstName);
        public abstract Builder sureName(String sureName);
        public abstract Builder salutation(String salutation);
        public abstract Builder releaseDate(String releaseDate);
        public abstract Builder workDescription(String workDescription);
        public abstract Builder role(Integer role);
        public abstract Builder active(boolean active);
        public abstract Employee build();
    }
}

//    public Employee() {
//        // nop
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getSureName() {
//        return sureName;
//    }
//
//    public void setSureName(String sureName) {
//        this.sureName = sureName;
//    }
//
//    public String getSalutation() {
//        return salutation;
//    }
//
//    public void setSalutation(String salutation) {
//        this.salutation = salutation;
//    }
//
//    public String getReleaseDate() {
//        return releaseDate;
//    }
//
//    public void setReleaseDate(String releaseDate) {
//        this.releaseDate = releaseDate;
//    }
//
//    public String getWorkDescription() {
//        return workDescription;
//    }
//
//    public void setWorkDescription(String workDescription) {
//        this.workDescription = workDescription;
//    }
//
//    public Integer getRole() {
//        return role;
//    }
//
//    public void setRole(Integer role) {
//        this.role = role;
//    }
//
//    public boolean isActive() {
//        return active;
//    }
//
//    public void setActive(boolean active) {
//        this.active = active;
//    }
//}
