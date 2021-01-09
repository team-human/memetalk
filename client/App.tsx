import * as React from 'react'
import * as eva from '@eva-design/eva';
import { NavigationContainer } from '@react-navigation/native'
import { createStackNavigator } from '@react-navigation/stack'
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs'
import TopicScreen from './Pages/topic'
import ProfileScreen from './Pages/profile'
import { apolloClient } from './Apollo/ApolloClient'
import { ApolloProvider } from '@apollo/client'
import { ApplicationProvider} from '@ui-kitten/components';
import ComposeScreen from './Pages/compose';
import LoginScreen from './Pages/login';


const TopicStack = createStackNavigator()

function TopicStackScreen() {
  return (
    <TopicStack.Navigator>
      <TopicStack.Screen name="Topic" component={TopicScreen} />
    </TopicStack.Navigator>
  )
}

const ProfileStack = createStackNavigator()

function ProfileStackScreen() {
  return (
    <ProfileStack.Navigator>
      <ProfileStack.Screen name="Profile" component={ProfileScreen} />
    </ProfileStack.Navigator>
  )
}

const ComposeStack = createStackNavigator()

function ComposeStackScreen() {
  return (
    <ComposeStack.Navigator>
      <ComposeStack.Screen name="Compose" component={ComposeScreen}/>
    </ComposeStack.Navigator>
  )
}

const Stack = createStackNavigator();
const Tab = createBottomTabNavigator()

export default function App() {
  return (
    <ApplicationProvider {...eva} theme={eva.light}>
    <ApolloProvider client={apolloClient}>
      <NavigationContainer>
      <Stack.Navigator initialRouteName="Home">
        <Stack.Screen name="Home" component={LoginScreen} />
      </Stack.Navigator>
        {/* <Tab.Navigator>
          <Tab.Screen name="Login" component={LoginScreen} />
          <Tab.Screen name="Topic" component={TopicStackScreen} />
          <Tab.Screen name="Profile" component={ProfileStackScreen} />
          <Tab.Screen name="Compose" component={ComposeStackScreen}/>
        </Tab.Navigator> */}
      </NavigationContainer>
    </ApolloProvider>
    </ApplicationProvider>

  )
}
