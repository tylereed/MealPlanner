package reed.tyler.mealplanner;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import reed.tyler.mealplanner.utils.Identifiable;

public class CrudController<TEntity extends Identifiable<TId>, TRepository extends JpaRepository<TEntity, TId>, TId> {

	protected final TRepository repository;

	protected CrudController(TRepository repository) {
		this.repository = repository;
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Validated TEntity entity) {
		entity = repository.saveAndFlush(entity);

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
