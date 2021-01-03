package com.formacionbdi.microservicios.app.examenes.models.entity;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formacionbdi.microservicios.commmons.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.commmons.examenes.models.entity.Pregunta;

public class EntityTest {

	@Test
	public void serialize() throws JsonMappingException, JsonProcessingException {
		String examenString = "{\"id\":1,\"nombre\":\"nombre\",\"createAt\":1597006211939,\"preguntas\":[{\"id\":1,\"texto\":\"Fooo\"}]}";
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.reader().forType(Examen.class).readValue(examenString);
	}
	
	@Test
	public void deserialize() throws JsonProcessingException {
		Examen examen = new Examen();
		
		examen.setId(1L);
		examen.setNombre("nombre");
		examen.setCreateAt(new Date());
		examen.setPreguntas(new ArrayList<Pregunta>());
		
		Pregunta p1 = new Pregunta();
		
		p1.setTexto("Fooo");
		p1.setId(1L);
		p1.setExamen(examen);
		
		examen.getPreguntas().add(p1);
		
		ObjectMapper mapper = new ObjectMapper();
	    mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
	    String result = mapper.writer().writeValueAsString(examen);
	    
	    System.out.println(result);
	}
	
}
