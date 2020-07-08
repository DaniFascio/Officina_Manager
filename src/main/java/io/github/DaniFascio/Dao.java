package io.github.DaniFascio;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Dao<T> {

	@Nullable
	T get(Object key);

	List<T> getAll(int page, int limit);

	void save(T t);

	void update(T t, Object[] params);

	void delete(T t);

}
