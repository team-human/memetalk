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

public class RunTimeWiringFactory {

    private final Map<String, Map<String, DataFetcher>> repository;
    private final List<GraphQLScalarType> scalars;

    private RunTimeWiringFactory() {
        repository = new HashMap<>();
        scalars = new ArrayList<>();
    }

    static RunTimeWiringFactory getInstance() {
        return new RunTimeWiringFactory();
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

    public RuntimeWiring build() {
        RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();

        repository.forEach(
                (type, dataFetchers) -> {
                    builder.type(TypeRuntimeWiring.newTypeWiring(type).dataFetchers(dataFetchers));
                });
        scalars.forEach(builder::scalar);

        return builder.build();
    }
}
