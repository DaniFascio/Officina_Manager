package io.github.danifascio.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Dao<T> {

	@Nullable T get(Object key);

	@NotNull List<T> getAll();

	@NotNull List<T> getAll(int page, int limit);

	int save(T t);

	int update(T t, Object[] params);

	int delete(T t);

}
