package com.formacionbdi.microservicios.app.cursos.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionbdi.microservicios.app.cursos.clients.AlumnoFeignClient;
import com.formacionbdi.microservicios.app.cursos.clients.RespuestasFeignClient;
import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;
import com.formacionbdi.microservicios.app.cursos.models.repository.CursoRepository;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.services.CommonServiceImpl;

@Service
public class CursoServiceImpl extends CommonServiceImpl<Curso, CursoRepository> implements CursoService {

	private RespuestasFeignClient client;
	
	private AlumnoFeignClient alumnoClient;
	
	public CursoServiceImpl(CursoRepository repository, RespuestasFeignClient client, AlumnoFeignClient alumnoClient) {
		super(repository);
		this.client = client;
		this.alumnoClient = alumnoClient;
	}

	@Override
	@Transactional(readOnly = true)
	public Curso findCursoByAlumnoId(Long id) {
		return repository.findCursoByAlumnoId(id);
	}

	@Override
	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(Long alumnoId) {
		return client.obtenerExamenesIdsConRespuestasAlumno(alumnoId);
	}

	@Override
	public Iterable<Alumno> obtenerAlumnosPorCurso(List<Long> ids) {
		return alumnoClient.obtenerAlumnosPorCurso(ids);
	}

	@Override
	@Transactional
	public void eliminarCursoAlumnoPorId(Long id) {
		repository.eliminarCursoAlumnoPorId(id);
	}
	
}
