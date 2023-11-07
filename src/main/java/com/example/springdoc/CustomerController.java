package com.example.springdoc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(CustomerController.URI)
@OpenAPIDefinition(
        info = @Info(title = "Customer API",
                description = """
                #### Customer REST API comes with all basic operations for customers
                
                """,
                version = "1.0",
                contact=@Contact(name="Prasanta Bhuyan",email = "prasanta.k.bhuyan@gmail.com")),
        tags = {@Tag(name=CustomerController.TAG)},
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Server"),
                @Server(url = "http://dev.app.com", description = "Dev Server"),
                @Server(url = "http://qa.app.com", description = "QA Server")
        }
)
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
    @Operation(summary = "List Customers", description = """
    Returns a list of all customers. Returns an empty array if no customer present in the system.
    
    Returns status 200.
    
    """)
    public List<CustomerResponseDTO> getCustomers() {
        return customers.stream().map(this::getCustomerResponseDTO).toList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add Customer", description = """
    Post a CustomerDTO to create new customer.
    
    Returns status 201.
    """)
    public CustomerResponseDTO saveCustomers(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO(
                UUID.randomUUID().toString(),
                customerDTO.getFirstName(),
                customerDTO.getLastName(),
                customerDTO.getEmail()
        );
        customerResponseDTO.add(Link.of(URI + "/" + customerResponseDTO.getId(), "self"));
        return customerResponseDTO;
    }

    @Operation(summary = "Get Customer By Id", description = """
    Returns 200: When customer found
    
    Returns 404:Not Found if no customer found with the given id.
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
    @DeleteMapping(path = "/{id}", produces=MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Delete Customer By Id", description = """
    Returns 404:Not Found if no customer found with the given id.
    
    Returns 204:No Content after successful deletion.
    """)
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
