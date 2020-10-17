import * as React from 'react'
import * as eva from '@eva-design/eva';
import { View } from 'react-native'
import { NavigationContainer } from '@react-navigation/native'
import { createStackNavigator } from '@react-navigation/stack'
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs'
import TopicScreen from './pages/topic'
import ProfileScreen from './pages/profile'
import { apolloClient } from './Providers/ApolloClient'
import { ApolloProvider } from '@apollo/client'
import { ApplicationProvider, Layout, Text } from '@ui-kitten/components';

function NotificationsScreen({ navigation }) {
  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text>Notification screen</Text>
    </View>
  )
}

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

const NotificationStack = createStackNavigator()

function NotificationsStackScreen() {
  return (
    <NotificationStack.Navigator>
      <NotificationStack.Screen
        name="Notifications"
        component={NotificationsScreen}
      />
    </NotificationStack.Navigator>
  )
}

const Tab = createBottomTabNavigator()

export default function App() {
  return (
    <ApplicationProvider {...eva} theme={eva.light}>
    <ApolloProvider client={apolloClient}>
      <NavigationContainer>
        <Tab.Navigator>
          <Tab.Screen name="Topic" component={TopicStackScreen} />
          <Tab.Screen name="Profile" component={ProfileStackScreen} />
          <Tab.Screen
            name="Notification"
            component={NotificationsStackScreen}
          />
        </Tab.Navigator>
      </NavigationContainer>
    </ApolloProvider>
    </ApplicationProvider>

  )
}
