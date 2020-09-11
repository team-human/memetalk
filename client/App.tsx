import * as React from 'react'
import { Text, View } from 'react-native'
import { NavigationContainer } from '@react-navigation/native'
import { createStackNavigator } from '@react-navigation/stack'
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs'
import TopicScreen from './pages/topic'

function SettingsScreen({ navigation }) {
  return (
    <View style={{ flex: 1 }}>
      <Text>Settings screen</Text>
    </View>
  )
}

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

const SettingsStack = createStackNavigator()

function SettingsStackScreen() {
  return (
    <SettingsStack.Navigator>
      <SettingsStack.Screen name="Settings" component={SettingsScreen} />
    </SettingsStack.Navigator>
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
    <NavigationContainer>
      <Tab.Navigator>
        <Tab.Screen name="Topic" component={TopicStackScreen} />
        <Tab.Screen name="Settings" component={SettingsStackScreen} />
        <Tab.Screen name="Notification" component={NotificationsStackScreen} />
      </Tab.Navigator>
    </NavigationContainer>
  )
}
