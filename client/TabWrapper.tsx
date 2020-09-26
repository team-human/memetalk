import { createBottomTabNavigator } from '@react-navigation/bottom-tabs'

export const TabWrapper = () => {
  const Tab = createBottomTabNavigator()
  return (
    <Tab.Navigator>
      {/* <Tab.Screen name="Home" component={HomeStackScreen} /> */}
      {/* <Tab.Screen name="Profile" component={ProfileStackScreen} /> */}
    </Tab.Navigator>
  )
}
