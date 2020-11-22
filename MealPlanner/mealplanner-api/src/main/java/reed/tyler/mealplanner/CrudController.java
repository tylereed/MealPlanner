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

public class CrudController<TEntity extends Identifiable<Long>, TRepository extends JpaRepository<TEntity, Long>> {

	protected final TRepository repository;

	protected CrudController(TRepository repository) {
		this.repository = repository;
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Validated TEntity entity) {
		entity = repository.save(entity);

		URI location = MvcUriComponentsBuilder.fromMethodName(this.getClass(), "read", entity.getId())
				.build(entity.getId());

		return ResponseEntity.created(location).build();
	}

	@GetMapping
	public List<TEntity> read() {
		return repository.findAll();
	}

	@GetMapping("{id}")
	public ResponseEntity<TEntity> read(@PathVariable Long id) {
		var entity = repository.findById(id);
		return ResponseEntity.of(entity);
	}

	@PutMapping("{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TEntity updatedEntity) {
		var entity = repository.findById(id).map(dbEntity -> {
			BeanUtils.copyProperties(updatedEntity, dbEntity, updatedEntity.getIdName());
			return dbEntity;
		});

		entity.ifPresent(repository::save);

		return ofNoContent(entity);
	}

	protected static ResponseEntity<?> ofNoContent(Optional<?> body) {
		return body.map(x -> ResponseEntity.noContent().build()).orElseGet(() -> ResponseEntity.notFound().build());
	}

}
