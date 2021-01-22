import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import React from "react";
import { AuthStateContext } from "../Provider/AuthProvider";
import { HomeScreen } from "./HomeScreen";
import { LogInScreen } from "./LogInScreen";
import { SplashScreen } from "./SplashScreen";

export const AppScreen = () => {
    const Stack = createStackNavigator();
    const authStateContext = React.useContext(AuthStateContext);

    return (
        <NavigationContainer>
            <Stack.Navigator>
                {authStateContext.isLoading ? (
                    // We haven't finished checking for the token yet
                    <Stack.Screen name="Splash" component={SplashScreen} />
                ) : authStateContext.userToken == null ? (
                    // No token found, user isn't signed in
                    <Stack.Screen
                        name="SignIn"
                        component={LogInScreen}
                        options={{
                            title: 'Sign in',
                            // When logging out, a pop animation feels intuitive
                            animationTypeForReplace: authStateContext.isSignout ? 'pop' : 'push',
                            headerShown: false
                        }}
                    />
                ) : (
                            // User is signed in
                            <Stack.Screen name="Home" component={HomeScreen} />
                        )}
            </Stack.Navigator>
        </NavigationContainer>
    )
}