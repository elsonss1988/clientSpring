package com.example.client.services;

import com.example.client.dto.ClientDTO;
import com.example.client.entities.Client;
import com.example.client.repository.ClientRepository;
import com.example.client.services.exceptions.DataBaseException;
import com.example.client.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    public ClientRepository clientRepository;

    @Transactional(readOnly=true)
    public Page<ClientDTO> findAll(PageRequest pageRequest){
        Page<Client> page = clientRepository.findAll(pageRequest);
        Page<ClientDTO> pageDTO = page.map(x-> new ClientDTO(x));
        return pageDTO;
    }

    @Transactional(readOnly=true)
    public ClientDTO findById(Long id){
        Optional<Client> client=clientRepository.findById(id);
        Client entity= client.orElseThrow( ()-> new ResourceNotFoundException("Entity not Found"));
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO insert (ClientDTO clientDTO){
        Client client = new Client(clientDTO);
        client=clientRepository.save(client);
        return new ClientDTO(client);
    }

    @Transactional
    public ClientDTO update (ClientDTO clientDTO, Long id){
        try{
            Optional<Client> client= clientRepository.findById(id);
            Client entity=client.orElseThrow(()-> new ResourceNotFoundException("Id not Found"));
            entity.setName(clientDTO.getName());
            entity.setBirthDate(clientDTO.getBirthDate());
            entity.setIncome(clientDTO.getIncome());
            entity.setCpf(clientDTO.getCpf());
            entity.setChildren(clientDTO.getChildren());
            clientDTO=new ClientDTO(clientRepository.save(entity));
        return clientDTO;
        }catch(ResourceNotFoundException e){
            throw new ResourceNotFoundException("Not Found "+ id);
        }catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Not Resource found");
        }
    }

    @Transactional
    public void delete(Long id){
        try {
            clientRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not Found "+id);
        }catch (DataIntegrityViolationException e){
            throw new DataBaseException("Integraty Violation");
        }
    }
}
