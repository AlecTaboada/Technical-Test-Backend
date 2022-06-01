package es.softtek.calificaciones.controller;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import es.softtek.calificaciones.dto.Nota;
import es.softtek.calificaciones.repository.NotaRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/note")
@CrossOrigin(origins = "", allowedHeaders = "")
public class NotaController {

	@Autowired
	NotaRepository notaRepository;

	@GetMapping
	public ResponseEntity<List<Nota>> getAllNotes(@RequestParam(required = false) String title, @RequestHeader("Authorization") String bearer) {
		List<Nota> notes= new ArrayList<>();
		if(validaRolProfesor(bearer.trim().substring(7))){
			 notes = notaRepository.findAll();
		}
			else{
			String userName = obtenerUsername(bearer.trim().substring(7));
			notes = notaRepository.findByUsername(userName.replaceAll("\"",""));
		}



			if (notes.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(notes, HttpStatus.OK);

	}

	private boolean validaRolProfesor(String token) {
		try {
			String[] deco = token.split("\\.");
			Base64.Decoder decoder = Base64.getUrlDecoder();
			String payload = new String(decoder.decode(deco[1]));
			JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
			//return jsonObject.get("sub").toString();
			System.out.println(deco);
			System.out.println(payload);
			if(jsonObject.get("authorities").getAsString().equals("PROFESOR")){
				return true;

			}
			return false;

		} catch (Exception e) {
			return false;
		}

	}

	private String obtenerUsername(String token) {
		try {
		String[] deco = token.split("\\.");
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String payload = new String(decoder.decode(deco[1]));
		JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
		return jsonObject.get("sub").toString();
		} catch (Exception e) {
			return "";
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Nota> getNoteById(@PathVariable("id") long id) {
		Optional<Nota> noteData = notaRepository.findById(id);

		if (noteData.isPresent()) {
			return new ResponseEntity<>(noteData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Nota> createNote(@RequestBody Nota nota, @RequestHeader("Authorization") String bearer) {
		try {
			if(validaRolProfesor(bearer.trim().substring(7))){
				Nota _nota = notaRepository
						.save(new Nota(nota.getAsignatura(), nota.getDescription(), nota.getUsername(), nota.getCalificacion()));
				return new ResponseEntity<>(_nota, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Nota> updateNote(@PathVariable("id") long id, @RequestBody Nota nota) {
		Optional<Nota> noteData = notaRepository.findById(id);

		if (noteData.isPresent()) {
			Nota _note = noteData.get();
			_note.setAsignatura(nota.getAsignatura());
			_note.setDescription(nota.getDescription());
			_note.setUsername(nota.getUsername());
			return new ResponseEntity<>(notaRepository.save(_note), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
