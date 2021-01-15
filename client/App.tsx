import * as React from 'react'
import * as eva from '@eva-design/eva';
import { apolloClient } from './Apollo/ApolloClient'
import { ApolloProvider } from '@apollo/client'
import { ApplicationProvider } from '@ui-kitten/components';
import { AppScreen } from './Screens/AppScreen';
import { AuthProvider } from './Provider/AuthProvider';

export default function App() {
  return (
    <ApplicationProvider {...eva} theme={eva.light}>
      <ApolloProvider client={apolloClient}>
        <AuthProvider>
          <AppScreen />
        </AuthProvider>
      </ApolloProvider>
    </ApplicationProvider>
  )
}
