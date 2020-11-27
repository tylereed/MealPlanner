package reed.tyler.mealplanner;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import reed.tyler.mealplanner.utils.ErrorMessages;
import reed.tyler.mealplanner.utils.Identifiable;

public class CrudController<TEntity extends Identifiable<TId>, TRepository extends JpaRepository<TEntity, TId>, TId> {

	protected final TRepository repository;

	protected CrudController(TRepository repository) {
		this.repository = repository;
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Validated TEntity entity) {
		try {
			entity = repository.saveAndFlush(entity);
		} catch (DataIntegrityViolationException e) {
			Pattern findUniqueContraint = Pattern.compile("constraint \\[\"PUBLIC\\.UK.*? ON PUBLIC.(.*?)\\((.*?)\\)");
			Matcher matcher = findUniqueContraint.matcher(e.getMessage());
			if (matcher.find()) {
				String tableName = matcher.group(1);
				String firstCharTable = tableName.substring(0, 1).toUpperCase();
				tableName = firstCharTable + tableName.substring(1).toLowerCase();
				String columnName = matcher.group(2).toLowerCase();

				String an = firstCharTable.matches("[AEIOU]") ? "n" : "";

				var errors = new ErrorMessages(String.format("A%s %s with the %s \"%s\" already exists", an, tableName,
						columnName, entity.getName()));

				return ResponseEntity.badRequest().body(errors);
			}

			throw e;
		}

		// Using fromMethod always threw errors, probably because the read method takes
		// in a generic parameter, so just hard code the path
		URI location = MvcUriComponentsBuilder
				.fromController(this.getClass())
				.path("/{id}")
				.build(entity.getEntityId());

		return ResponseEntity.created(location).build();
	}

	@GetMapping
	public List<TEntity> read() {
		return repository.findAll();
	}

	@GetMapping("{id}")
	public ResponseEntity<TEntity> read(@PathVariable TId id) {
		var entity = repository.findById(id);
		return ResponseEntity.of(entity);
	}

	@PutMapping("{id}")
	public ResponseEntity<?> update(@PathVariable TId id, @RequestBody TEntity updatedEntity) {
		var entity = repository.findById(id).map(dbEntity -> {
			BeanUtils.copyProperties(updatedEntity, dbEntity, updatedEntity.getIdName());
			return dbEntity;
		}).map(repository::saveAndFlush);

		return ofNoContent(entity);
	}

	protected static ResponseEntity<?> ofNoContent(Optional<?> body) {
		return body.map(x -> ResponseEntity.noContent().build())
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

}
