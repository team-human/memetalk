import React from 'react'
import { createStackNavigator } from '@react-navigation/stack';
import { SignUpScreen } from './SignUpScreen';
import { SignInScreen } from './SignInScreen';
const logo = require("../assets/logo.png")

export const LogInScreen = ({ navigation }) => {
    const Stack = createStackNavigator();
    return (
        <Stack.Navigator initialRouteName="Sign In">
            <Stack.Screen name="Sign In" component={SignInScreen} />
            <Stack.Screen name="Sign Up" component={SignUpScreen} />
        </Stack.Navigator>
    );
}
