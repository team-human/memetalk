import { useApolloClient, useQuery } from '@apollo/client'
import React, { useEffect, useState } from 'react'
import {
    View,
    StyleSheet,
    Text,
    TextInput,
    TouchableOpacity,
    Image,
} from 'react-native'
import { SIGNUP_USER } from '../Query/UserQuery'
import { SecureStore } from 'expo';
import { createStackNavigator } from '@react-navigation/stack';
import { HomeScreen } from './HomeScreen';
import { SignUpScreen } from './SignUpScreen';
import { SignInScreen } from './SignInScreen';
const logo = require("../Assets/logo.png")

export const LogInScreen = ({ navigation }) => {
    const Stack = createStackNavigator();
    return (
        <Stack.Navigator initialRouteName="Sign In">
            <Stack.Screen name="Sign In" component={SignInScreen} />
            <Stack.Screen name="Sign Up" component={SignUpScreen} />
        </Stack.Navigator>
    );
}
