package edu.iit.itmd4515.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user_groups_table")
@NamedQuery(name = "Group.findAll", query = "SELECT g FROM Group g")
@NamedQuery(name = "Group.findByGroupName", query = "SELECT g FROM Group g WHERE g.groupName = :groupName")
public class Group {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Group name is required")
    @Size(max = 50, message = "Group name must not exceed 50 characters")
    @Column(name = "group_name", unique = true, nullable = false, length = 50)
    private String groupName;
    
    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Column(length = 255)
    private String description;
    
    // ManyToMany relationship with User
    @ManyToMany(mappedBy = "groups", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();
    
    // Constructors
    public Group() {
    }
    
    public Group(String groupName) {
        this.groupName = groupName;
    }
    
    public Group(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }
    
    // Helper methods for relationship management
    public void addUser(User user) {
        if (!this.users.contains(user)) {
            this.users.add(user);
        }
    }
    
    public void removeUser(User user) {
        if (this.users.contains(user)) {
            this.users.remove(user);
        }
    }
    
    public int getUserCount() {
        return users.size();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getGroupName() {
        return groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    // equals and hashCode based on groupName
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(groupName, group.groupName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(groupName);
    }
    
    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", description='" + description + '\'' +
                ", userCount=" + getUserCount() +
                '}';
    }
}