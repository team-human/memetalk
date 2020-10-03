import { ApolloClient, InMemoryCache } from '@apollo/client'

export const apolloClient = new ApolloClient({
  uri: 'https://memetalk.herokuapp.com/graphql',
  cache: new InMemoryCache(),
})
