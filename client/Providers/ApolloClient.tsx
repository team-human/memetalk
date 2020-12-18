import fetch from 'cross-fetch';
import { ApolloClient, HttpLink, InMemoryCache } from '@apollo/client'
import { EnvironmentConfigs } from '../Configs/EnvironmentConfigs'

export const apolloClient = new ApolloClient({
  link: new HttpLink({ uri: EnvironmentConfigs.dev.graphQLEndPoint, fetch }),
  cache: new InMemoryCache(),
})