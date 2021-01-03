package com.formacionbdi.microservicios.app.respuestas.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.microservicios.app.respuestas.models.entity.Respuesta;
import com.formacionbdi.microservicios.app.respuestas.service.RespuestaService;

@RestController
public class RespuestaController {

	private RespuestaService service;

	public RespuestaController(RespuestaService service) {
		super();
		this.service = service;
	}
	
	
	@PostMapping
	public ResponseEntity<?> crear (@RequestBody Iterable<Respuesta> respuestas) {
		respuestas = ((List<Respuesta>) respuestas).stream().map(r -> {
			r.setAlumnoId(r.getAlumno().getId());
			r.setPreguntaId(r.getPregunta().getId());
			return r;
		}).collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.saveAll(respuestas));
	} 

	
	@GetMapping("/alumno/{alumnoId}/examen/{examenId}")
	public ResponseEntity<?> obtenerRespuestaPorAlumnoYExamen (@PathVariable Long alumnoId, @PathVariable Long examenId) {
		return ResponseEntity.ok().body(service.findRespuestaByAlumnoByExamen(alumnoId, examenId));
	} 
	
	@GetMapping("/alumno/{alumnoId}/examenes-respondidos")
	public ResponseEntity<?> obtenerExamenesIdsConRespuestasAlumno(@PathVariable Long alumnoId) {
		return ResponseEntity.ok().body(service.findExamenesIdsConRespuestasByAlumno(alumnoId));
	}
}
