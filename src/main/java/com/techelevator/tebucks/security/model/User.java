package com.techelevator.tebucks.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {

   private int id;
   private String username;
   @JsonIgnore
   private String password;
   private String firstName;
   private String lastName;
   private String email;
   @JsonIgnore
   private boolean activated;
   private Set<Authority> authorities = new HashSet<>();

   public User() {

   }

   public User(int id, String username, String password, String role, boolean activated) {
      this.id = id;
      this.username = username;
      this.password = password;
      this.authorities.add(new Authority(role));
      this.activated = activated;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public boolean isActivated() {
      return activated;
   }

   public void setActivated(boolean activated) {
      this.activated = activated;
   }

   public Set<Authority> getAuthorities() {
      return authorities;
   }

   public void setAuthorities(Set<Authority> authorities) {
      this.authorities = authorities;
   }

   public void setAuthorities(String authorities) {
      String[] roles = authorities.split(",");
      for (String role : roles) {
         this.authorities.add(new Authority("ROLE_" + role));
      }
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return id == user.id &&
              activated == user.activated &&
              Objects.equals(username, user.username) &&
              Objects.equals(password, user.password) &&
              Objects.equals(firstName, user.firstName) &&
              Objects.equals(lastName, user.lastName) &&
              Objects.equals(email, user.email) &&
              Objects.equals(authorities, user.authorities);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, username, password, firstName, lastName, email, activated, authorities);
   }

   @Override
   public String toString() {
      return "User{" +
         "username='" + username + '\'' +
         ", password='" + password + '\'' +
         ", firstname='" + firstName + '\'' +
         ", lastname='" + lastName + '\'' +
         ", email='" + email + '\'' +
         ", activated=" + activated +
         '}';
   }
}
