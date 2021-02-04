import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { createStackNavigator } from "@react-navigation/stack";
import React from "react";
import { ComposeScreen } from "./ComposeScreen";
import { ProfileScreen } from "./ProfileScreen";
import { TopicScreen } from "./TopicScreen";

const Tab = createBottomTabNavigator()
const TopicStack = createStackNavigator()
const ProfileStack = createStackNavigator()
const ComposeStack = createStackNavigator()
const TopicStackScreen = () => {
    return (
        <TopicStack.Navigator>
            <TopicStack.Screen name="Topic" component={TopicScreen} />
        </TopicStack.Navigator>
    )
}
const ProfileStackScreen = () => {
    return (
        <ProfileStack.Navigator>
            <ProfileStack.Screen name="Profile" component={ProfileScreen} />
        </ProfileStack.Navigator>
    )
}
const ComposeStackScreen = () => {
    return (
        <ComposeStack.Navigator>
            <ComposeStack.Screen name="Compose" component={ComposeScreen} />
        </ComposeStack.Navigator>
    )
}

export const HomeScreen = () => {
    return (
        <Tab.Navigator>
            <Tab.Screen name="Topic" component={TopicStackScreen} />
            <Tab.Screen name="Profile" component={ProfileStackScreen} />
            <Tab.Screen name="Compose" component={ComposeStackScreen} />
        </Tab.Navigator>
    )
}