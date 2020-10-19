package memetalk.graphql;

import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.TypeRuntimeWiring;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;

public class DataFetcherRegisterFactory {

    private final Map<String, Map<String, DataFetcher>> repository;

    private DataFetcherRegisterFactory() {
        repository = new HashMap<>();
    }

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

    public RuntimeWiring buildTypeWiring() {
        RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();

        repository.forEach(
                (type, dataFetchers) -> {
                    builder.type(TypeRuntimeWiring.newTypeWiring(type).dataFetchers(dataFetchers));
                });

        return builder.build();
    }
}
