package org.group02.guitarshop.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Entity
@DynamicUpdate
@Table(name = "Person")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_Id", nullable = false, updatable = false)
    private int user_Id;

    @Column(name = "Name", nullable = true, columnDefinition="nvarchar(255)")
    @NotEmpty(message = "Vui lòng nhập tên")
    private String name;

    @Column(name = "Email", nullable = true, length = 255, updatable = false)
    @Email(message = "Vui lòng nhập vào địa chỉ email hợp lệ")
    @NotEmpty(message = "Vui lòng nhập email")
    private String email;

    @Column(name = "Password", nullable = true, length = 255)
    @Length(min = 5, message = "Vui lòng nhập mật mã có ít nhất 5 ký tự")
    @NotEmpty(message = "Vui lòng nhập mật mã")
    private String password;

    @Column(name = "verificationCode", updatable = false)
    String verificationCode;

    private boolean enable;

    @Column(name = "Address", nullable = true, columnDefinition="nvarchar(1255)")
    private String address;

    @Column(name = "Phone", nullable = true, length = 20)
    @NotEmpty(message = "Vui lòng nhập số điện thoại")
    private String phone;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "UserRole", joinColumns = @JoinColumn(name = "User_Id"), inverseJoinColumns = @JoinColumn(name = "Role_Id"))
    private Set<Role> roles;
}
