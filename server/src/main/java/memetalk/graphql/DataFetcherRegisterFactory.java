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
