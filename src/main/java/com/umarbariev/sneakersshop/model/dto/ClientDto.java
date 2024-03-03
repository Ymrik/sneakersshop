package com.umarbariev.sneakersshop.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientDto {
    @NotEmpty(message = "Поле должно быть заполненным!")
    @Size(min = 2, max = 50, message = "Длина имени пользователя должна быть от 2 до 50 символов!")
    private String username;
    @NotEmpty(message = "Поле должно быть заполненным!")
    @Size(min = 2, max = 50, message = "Длина имени должна быть от 2 до 50 символов!")
    private String name;
    @NotEmpty(message = "Поле должно быть заполненным!")
    @Size(min = 2, max = 50, message = "Длина фамилии должна быть от 2 до 50 символов!")
    private String lastName;
    @NotEmpty(message = "Поле должно быть заполненным!")
    private String sex;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
    @NotEmpty(message = "Поле должно быть заполненным!")
    @Pattern(regexp = "\\+7[0-9]{10}", message = "Номер телефона должен быть в формате +79999999999")
    private String phoneNumber;
    @NotEmpty(message = "Поле должно быть заполненным!")
    @Email(message = "Неверный формат электронной почты")
    private String email;
    private String deliveryAddress;
    @NotEmpty(message = "Поле должно быть заполненным!")
    private String password;
    private Boolean isActive;
}
