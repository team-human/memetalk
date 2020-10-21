package memetalk.graphql;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLScalarType;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.TypeRuntimeWiring;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NonNull;

public class DataFetcherRegisterFactory {

    private final Map<String, Map<String, DataFetcher>> repository;
    private final List<GraphQLScalarType> scalars;

    private DataFetcherRegisterFactory() {
        repository = new HashMap<>();
        scalars = new ArrayList<>();
    }

    // TODO: Rename to `getInstance` or something similar as we get the whole
    // factory here and generates runtimeWiring later.
    static DataFetcherRegisterFactory getRuntimeWiring() {
        return new DataFetcherRegisterFactory();
    }

    /**
     * Register a dataFetcher to a certain field of a type.
     *
     * @param typeName the name of the type to wire
     * @param fieldName the field that data fetcher should apply to
     * @param dataFetcher the new data Fetcher
     */
    public void registerTypeWiring(
            @NonNull String typeName, @NonNull String fieldName, @NonNull DataFetcher dataFetcher) {
        repository
                .computeIfAbsent(typeName, type -> new HashMap<>())
                .computeIfAbsent(fieldName, field -> dataFetcher);
    }

    /**
     * Register a scalar type.
     *
     * @param scalar the scalar to be registered
     */
    public void registerScalar(GraphQLScalarType scalar) {
        scalars.add(scalar);
    }

    // TODO: Rename to `buildRuntimeWiring` as we also build scalars.
    public RuntimeWiring buildTypeWiring() {
        RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();

        repository.forEach(
                (type, dataFetchers) -> {
                    builder.type(TypeRuntimeWiring.newTypeWiring(type).dataFetchers(dataFetchers));
                });
        for (GraphQLScalarType scalar : scalars) {
            builder.scalar(scalar);
        }

        return builder.build();
    }
}
