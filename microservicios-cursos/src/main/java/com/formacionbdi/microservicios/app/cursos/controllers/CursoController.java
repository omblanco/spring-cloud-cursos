package com.formacionbdi.microservicios.app.cursos.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;
import com.formacionbdi.microservicios.app.cursos.models.entity.CursoAlumno;
import com.formacionbdi.microservicios.app.cursos.services.CursoService;
import com.formacionbdi.microservicios.commmons.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.controllers.CommonController;

@RestController
public class CursoController extends CommonController<Curso, CursoService>{

	
	@Value("${config.balanceador.test}")
	private String balanceadorTest;
	
	public CursoController(CursoService service) {
		super(service);
	}
	
	
	@DeleteMapping("/eliminar-alumno/{id}")
	public ResponseEntity<?> eliminarCursoAlumnoPorId(@PathVariable Long id) {
		service.eliminarCursoAlumnoPorId(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("/pagina")
	@Override
	public ResponseEntity<?> listar(Pageable pageable) {
		Page<Curso> cursos = service.findAll(pageable).map(curso -> {
			curso.getCursoAlumnos().forEach(ca -> {
				Alumno alumno = new Alumno();
				alumno.setId(ca.getAlumnoId());
				curso.addAlumno(alumno);
				
			});
			return curso;
		});
		
		return ResponseEntity.ok().body(cursos);
	}	
	
	@GetMapping("/{id}")
	@Override
	public ResponseEntity<?> ver(@PathVariable Long id) {
		
		Optional<Curso> optionalEntity = service.findById(id);
		
		if (optionalEntity.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			
			Curso curso = optionalEntity.get();
			
			if (!curso.getCursoAlumnos().isEmpty()) {
				List<Long> ids = curso.getCursoAlumnos().stream().map(ca -> ca.getAlumnoId())
						.collect(Collectors.toList());
				
				List<Alumno> alumnos = (List<Alumno>) service.obtenerAlumnosPorCurso(ids);
				curso.setAlumnos(alumnos);
			}
			
			return ResponseEntity.ok().body(curso);	
		}
	}	

	@GetMapping
	@Override
	public ResponseEntity<?> listar() {
		List<Curso> cursos = ((List<Curso>) service.findAll()).stream().map(c -> {
			c.getCursoAlumnos().forEach(ca -> {
				Alumno alumno = new Alumno();
				alumno.setId(ca.getAlumnoId());
				c.addAlumno(alumno);
				
			});
			return c;
		}).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(cursos);
	}	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {
		
		if (result.hasErrors()) {
			return validar(result);
		}
		
		Optional<Curso> optionalCurso = service.findById(id);
		
		if (optionalCurso.isEmpty()) {
			return ResponseEntity.notFound().build();
		} 
		
		Curso cursoDb = optionalCurso.get();
		cursoDb.setNombre(curso.getNombre());

		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cursoDb));
	}
	
	@PutMapping("/{id}/asignar-alumnos")
	public ResponseEntity<?> asignarAlumnos(@RequestBody List<Alumno> alumnos, @PathVariable Long id) {

		Optional<Curso> optionalCurso = service.findById(id);
		
		if (optionalCurso.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Curso cursoDb = optionalCurso.get();
		
		alumnos.forEach(a -> {
			CursoAlumno cursoAlumno = new CursoAlumno();
			cursoAlumno.setAlumnoId(a.getId());
			cursoAlumno.setCurso(cursoDb);
			cursoDb.addCursoAlumnos(cursoAlumno);
		});
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cursoDb));
	}
	
	@PutMapping("/{id}/eliminar-alumno")
	public ResponseEntity<?> eliminarAlumno(@RequestBody Alumno alumno, @PathVariable Long id) {

		Optional<Curso> optionalCurso = service.findById(id);
		
		if (optionalCurso.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Curso cursoDb = optionalCurso.get();
		CursoAlumno cursoAlumno = new CursoAlumno();
		cursoAlumno.setAlumnoId(alumno.getId());
		
		cursoDb.removeCursoAlumnos(cursoAlumno);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cursoDb));
	}
	
	@GetMapping("/alumno/{id}")
	public ResponseEntity<?> buscarPorAlumnoId(@PathVariable Long id) {
		Curso curso = service.findCursoByAlumnoId(id);
		
		if (curso != null) {
			List<Long> examenesId = (List<Long>) service.obtenerExamenesIdsConRespuestasAlumno(id);
			
			if (examenesId != null && examenesId.size() > 0) {
				List<Examen> examenes = curso.getExamenes().stream().map(examen -> {
					if (examenesId.contains(examen.getId())) {
						examen.setRespondido(true);
					}
					
					return examen;
				}).collect(Collectors.toList());
				
				curso.setExamenes(examenes);
			}
			

		}
		
		
		
		return ResponseEntity.ok(curso);
	}
	
	@PutMapping("/{id}/asignar-examenes")
	public ResponseEntity<?> asignarExamenes(@RequestBody List<Examen> examenes, @PathVariable Long id) {

		Optional<Curso> optionalCurso = service.findById(id);
		
		if (optionalCurso.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Curso cursoDb = optionalCurso.get();
		
		examenes.forEach(e -> {
			cursoDb.addExamen(e);
		});
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cursoDb));
	}
	
	@PutMapping("/{id}/eliminar-examen")
	public ResponseEntity<?> eliminarExamen(@RequestBody Examen examen, @PathVariable Long id) {

		Optional<Curso> optionalCurso = service.findById(id);
		
		if (optionalCurso.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Curso cursoDb = optionalCurso.get();
		
		cursoDb.removeExamen(examen);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cursoDb));
	}

	@GetMapping("/balanceador-test")
	public ResponseEntity<?> balanceadorTest() {
		
		Map<String, Object> response = new HashMap<String, Object> ();
		
		response.put("balanceador", balanceadorTest);
		response.put("cursos", service.findAll());
		
		return ResponseEntity.ok(response);
	}
	
	
}
