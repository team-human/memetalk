import * as React from 'react'
import { useEffect } from 'react'
import { getStorageItem } from '../Helper/Storage'
type Action = { type: 'RESTORE_TOKEN' | 'SIGN_IN' | 'SIGN_OUT'; token?: string | null }
type Dispatch = (action: Action) => void
type State = { isLoading: boolean; isSignout: boolean; userToken?: string | null }
type AuthProviderProps = { children: React.ReactNode }

export const AuthStateContext = React.createContext({} as State)
const authReducer = (prevState: State, action: Action): State => {
    switch (action.type) {
        case 'RESTORE_TOKEN':
            return {
                ...prevState,
                userToken: action.token,
                isLoading: false,
            }
        case 'SIGN_IN':
            return {
                ...prevState,
                isSignout: false,
                userToken: action.token,
            }
        case 'SIGN_OUT':
            return {
                ...prevState,
                isSignout: true,
                userToken: null,
            }
        default: {
            throw new Error(`Unhandled action type: ${action.type}`)
        }
    }
}

export const AuthProvider = ({ children }: AuthProviderProps) => {
    const [state, dispatch] = React.useReducer(authReducer, {
        isLoading: true,
        isSignout: false,
        userToken: null,
    })

    const authContext = React.useMemo(
        () => ({
            signIn: async (data: any) => {
                // In a production app, we need to send some data (usually username, password) to server and get a token
                // We will also need to handle errors if sign in failed
                // After getting token, we need to persist the token using `AsyncStorage`
                // In the example, we'll use a dummy token

                dispatch({ type: 'SIGN_IN', token: 'dummy-auth-token' });
            },
            signOut: () => dispatch({ type: 'SIGN_OUT' }),
            signUp: async (data: any) => {
                // In a production app, we need to send user data to server and get a token
                // We will also need to handle errors if sign up failed
                // After getting token, we need to persist the token using `AsyncStorage`
                // In the example, we'll use a dummy token

                dispatch({ type: 'SIGN_IN', token: 'dummy-auth-token' });
            },
        }),
        []
    );

    useEffect(() => {
        // Fetch the token from storage then navigate to our appropriate place
        const bootstrapAsync = async () => {
            let userToken

            try {
                userToken = await getStorageItem('userToken')
            } catch (e) {
                // Restoring token failed
            }

            // After restoring token, we may need to validate it in production apps

            // This will switch to the App screen or Auth screen and this loading
            // screen will be unmounted and thrown away.
            dispatch({ type: 'RESTORE_TOKEN', token: userToken })
        }

        bootstrapAsync()
    }, [])

    return (
        <AuthStateContext.Provider value={state}>
            {/* <CountDispatchContext.Provider value={dispatch}> */}
            {children}
            {/* </CountDispatchContext.Provider> */}
        </AuthStateContext.Provider>
    )
}
