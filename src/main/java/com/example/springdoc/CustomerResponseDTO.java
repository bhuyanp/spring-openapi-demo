package com.example.springdoc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO extends RepresentationModel<CustomerResponseDTO> {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
}

