package com.formacionbdi.microservicios.app.respuestas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.formacionbdi.microservicios.app.respuestas.clients.ExamenesFeignClient;
import com.formacionbdi.microservicios.app.respuestas.models.entity.Respuesta;
import com.formacionbdi.microservicios.app.respuestas.models.repository.RespuestaRepository;

@Service
public class RespuestaServiceImpl implements RespuestaService {

	private RespuestaRepository repository;
	
	private ExamenesFeignClient examenClient;
	
	public RespuestaServiceImpl(RespuestaRepository repository, ExamenesFeignClient examenClient) {
		super();
		this.repository = repository;
		this.examenClient = examenClient;
	}

	@Override
	public Iterable<Respuesta> saveAll(Iterable<Respuesta> respuestas) {
		return repository.saveAll(respuestas);
	}

	@Override
	public Iterable<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId) {
//		Examen examen = examenClient.obtenerExamenPorId(examenId);
//		
//		Iterable<Long> preguntaIds = examen.getPreguntas().stream().map(p -> p.getId()).collect(Collectors.toList());
//		
//		Iterable<Respuesta> respuestas = repository.findRespuestaByAlumnoByPreguntaIds(alumnoId, preguntaIds);
//		
//		respuestas = ((List<Respuesta>) respuestas).stream().map(r ->  {
//			examen.getPreguntas().forEach(p -> {
//				if (p.getId().equals(r.getPreguntaId())) {
//					r.setPregunta(p);
//				}
//			});
//			return r;
//		}).collect(Collectors.toList());
		
		Iterable<Respuesta> respuestas = repository.findRespuestaByAlumnoByExamen(alumnoId, examenId);
		
		return respuestas;
	}

	@Override
	public Iterable<Long> findExamenesIdsConRespuestasByAlumno(Long alumnoId) {
		
//		List<Respuesta> respuestasAlumno = (List<Respuesta>) repository.findByAlumnoId(alumnoId);
//		List<Long> examenIds = Collections.emptyList();
//		
//		if (respuestasAlumno.size() > 0) {
//			List<Long> preguntaIds = respuestasAlumno.stream()
//					.map(r -> r.getPreguntaId()).collect(Collectors.toList()); 
//			
//			examenIds = examenClient.obtenerExamenesIdsPorPreguntasIdRespondidas(preguntaIds);
//		}
		
		List<Respuesta> respuestasAlumno = (List<Respuesta>) repository.findExamenesIdsConRespuestasByAlumno(alumnoId);
		List<Long> examenIds = respuestasAlumno.stream()
				.map(r -> r.getPregunta().getExamen().getId())
				.distinct()
				.collect(Collectors.toList());
		
		return examenIds;
	}

	@Override
	public Iterable<Respuesta> findByAlumnoId(Long alumnoId) {
		return repository.findByAlumnoId(alumnoId);
	}
	
}
