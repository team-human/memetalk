import { ApolloClient, InMemoryCache } from '@apollo/client'
import { EnvironmentConfigs } from '../Configs/EnvironmentConfigs'

export const apolloClient = new ApolloClient({
  uri: EnvironmentConfigs.dev.graphQLEndPoint,
  cache: new InMemoryCache(),
})
