package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.request.ProducerPutRequest;
import academy.devdojo.response.ProducerGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {

    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all producers, param name :'{}'", name);
        var producers = Producer.getProducers();
        List<ProducerGetResponse> producerGetResponseList = MAPPER.toProducerGetResponseList(producers);
        if (name == null) return ResponseEntity.ok(producerGetResponseList);

        var response = producerGetResponseList.stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find producer by id: {}", id);
        var producerGetResponse = Producer
                .getProducers()
                .stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst()
                .map(MAPPER::toProducerGetResponse)
                .orElse(null);

        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "x-api-key=1234")
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("{}", headers);
        var producer = MAPPER.toProducer(producerPostRequest);
        Producer.getProducers().add(producer);
        var response2 = MAPPER.toProducerGetResponse(producer);
        return ResponseEntity.ok(response2);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        log.debug("Request to delete producer by id: {}", id);
        var producerToDelete = Producer.getProducers()
                .stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not Found"));
        Producer.getProducers().remove(producerToDelete);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest request){
        log.debug("Request to update producer by id: {}", request);
        var producerToRemove = Producer.getProducers()
                .stream()
                .filter(producer -> producer.getId().equals(request.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not Found"));
        var producerUpdated = MAPPER.toProducer(request, producerToRemove.getCreatedAt());
        Producer.getProducers().remove(producerToRemove);
        Producer.getProducers().add(producerUpdated);
        return ResponseEntity.noContent().build();
    }
}
