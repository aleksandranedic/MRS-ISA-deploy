package com.project.team9.controller;

import com.project.team9.dto.ClientDTO;
import com.project.team9.dto.ReservationDTO;
import com.project.team9.dto.UserStatDTO;
import com.project.team9.exceptions.CannotDeleteException;
import com.project.team9.model.Address;
import com.project.team9.model.request.DeleteRequest;
import com.project.team9.model.user.Client;
import com.project.team9.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "client")
@CrossOrigin("*")
public class ClientController {

    private final ClientService clientService;



    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(value = "changeProfilePicture/{id}")
    public Boolean changeProfilePicture(@PathVariable String id, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        return clientService.changeProfilePicture(id, multipartFile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable String id) {
        return ResponseEntity.ok(clientService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        return ResponseEntity.ok().body(clientService.getClients());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable String id, @RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(clientService.updateLoggedUser(id,clientDTO));
    }

    @GetMapping("/reservation/{id}")
    public List<ReservationDTO> getReservations(@PathVariable Long id) {
        return clientService.getReservations(id);
    }

    @PostMapping("/delete/{id}")
    Long delete(@PathVariable Long id) throws CannotDeleteException {
        return clientService.delete(id);
    }
}

