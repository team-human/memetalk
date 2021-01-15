import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { createStackNavigator } from "@react-navigation/stack";
import React from "react";
import { ComposeScreen } from "./ComposeScreen";
import { ProfileScreen } from "./ProfileScreen";
import { TopicScreen } from "./TopicScreen";

export const HomeScreen = () => {
    const Tab = createBottomTabNavigator()

    const TopicStack = createStackNavigator()
    const TopicStackScreen = () => {
        return (
            <TopicStack.Navigator>
                <TopicStack.Screen name="Topic" component={TopicScreen} />
            </TopicStack.Navigator>
        )
    }

    const ProfileStack = createStackNavigator()
    const ProfileStackScreen = () => {
        return (
            <ProfileStack.Navigator>
                <ProfileStack.Screen name="Profile" component={ProfileScreen} />
            </ProfileStack.Navigator>
        )
    }

    const ComposeStack = createStackNavigator()
    const ComposeStackScreen = () => {
        return (
            <ComposeStack.Navigator>
                <ComposeStack.Screen name="Compose" component={ComposeScreen} />
            </ComposeStack.Navigator>
        )
    }

    return (
        <Tab.Navigator>
            <Tab.Screen name="Topic" component={TopicStackScreen} />
            <Tab.Screen name="Profile" component={ProfileStackScreen} />
            <Tab.Screen name="Compose" component={ComposeStackScreen} />
        </Tab.Navigator>
    )
}