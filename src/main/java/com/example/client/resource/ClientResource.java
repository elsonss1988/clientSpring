package com.example.client.resource;

import com.example.client.dto.ClientDTO;
import com.example.client.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clients")
public class ClientResource {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<Page<ClientDTO>> findAll(
            @RequestParam(value="page", defaultValue="0")Integer page,
            @RequestParam(value="linesPerPage", defaultValue="4") Integer linesPerPage,
            @RequestParam(value="direction", defaultValue="ASC") String direction,
            @RequestParam(value="orderBy", defaultValue = "name") String  orderBy
            ){
        PageRequest pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction),orderBy);
        Page<ClientDTO> list = clientService.findAll(pageRequest);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id){
      ClientDTO  clientDTO=clientService.findById(id);
      return ResponseEntity.ok().body(clientDTO);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> insert(@RequestBody ClientDTO clientDTO){
        clientDTO=clientService.insert(clientDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(clientDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(clientDTO);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<ClientDTO> update(@RequestBody ClientDTO clientDTO,@PathVariable Long id){
        clientDTO=clientService.update(clientDTO,id);
        return ResponseEntity.ok().body(clientDTO);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }




}
