package com.example.springdoc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(CustomerController.URI)
@Tag(name=CustomerController.TAG)
public class CustomerController {

    public static final String URI = "/api/v1/customers";
    public static final String TAG = "Customer";

    private static final List<Customer> customers = List.of(
            new Customer("id10", "Prasanta", "Bhuyan", "email1@gmail.com"),
            new Customer("id11", "Prasanta", "Bhuyan", "email2@gmail.com"),
            new Customer("id12", "Prasanta", "Bhuyan", "email3@gmail.com"),
            new Customer("id13", "Prasanta", "Bhuyan", "email4@gmail.com")
    );


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "CustomerSecurityScheme")
    @Operation(summary = "List Customers", description = """
    ### Authentication required
    Returns status 200: Returns a list of all customers. Returns an empty array 
    if no customer present in the system
    
    Returns status 403: If not authenticated  
    """)
    public List<CustomerResponseDTO> getCustomers() {
        return customers.stream().map(this::getCustomerResponseDTO).toList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add Customer", description = """
    ### Authentication not required

    Post a CustomerDTO to create new customer.
    
    Returns status 201: When successfully created
    """)
    public CustomerResponseDTO saveCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO(
                UUID.randomUUID().toString(),
                customerDTO.getFirstName(),
                customerDTO.getLastName(),
                customerDTO.getEmail()
        );
        customerResponseDTO.add(Link.of(URI + "/" + customerResponseDTO.getId(), "self"));
        return customerResponseDTO;
    }


    @SecurityRequirement(name = "CustomerSecurityScheme")
    @Operation(summary = "Get Customer By Id", description = """
    ### Authentication required
    Returns status 200: When customer found
    
    Returns status 404: If no customer found with the given id
    
    Returns status 403: If not authenticated
    """)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable String id) {
        Optional<CustomerResponseDTO> customerDTOOptional = customers.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .map(this::getCustomerResponseDTO);
        return customerDTOOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Customer By Id", description = """
    ### ADMIN role required
    Returns 204:No Content after successful deletion

    Returns 404:Not Found if no customer found with the given id
    
    Returns 403:If user is not an ADMIN
    """)
    @SecurityRequirement(name = "CustomerSecurityScheme")
    public ResponseEntity<String> deleteCustomer(@PathVariable String id) {
        if(customers.stream().filter(it -> it.getId().equals(id)).count()==0){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    private CustomerResponseDTO getCustomerResponseDTO(Customer customer){
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO(
            customer.getId(),
            customer.getFirstName(),
            customer.getLastName(),
            customer.getEmail()
        );
        customerResponseDTO.add(Link.of(URI + "/" + customerResponseDTO.getId(), "self"));
        return customerResponseDTO;
    }
}
