import * as React from 'react'
import { useEffect } from 'react'
import { LoginUser } from '../Generated/graphqlType'
import { getStorageItem } from '../Helper/Storage'

type Action = { type: 'RESTORE_TOKEN' | 'SIGN_IN' | 'SIGN_OUT'; token?: string | null }
type Dispatch = (action: Action) => void
type State = { isLoading: boolean; isSignout: boolean; userToken?: string | null }
type AuthProviderProps = { children: React.ReactNode }

export const AuthStateContext = React.createContext({} as State)
export const AuthDispatchContext = React.createContext({} as Dispatch)
export const AuthContext = React.createContext({})


export const AuthProvider = ({ children }: AuthProviderProps) => {
    const [state, dispatch] = React.useReducer(
        (prevState: State, action: Action) => {
            switch (action.type) {
                case 'RESTORE_TOKEN':
                    return {
                        ...prevState,
                        userToken: action.token,
                        isLoading: false,
                    };
                case 'SIGN_IN':
                    return {
                        ...prevState,
                        isSignout: false,
                        userToken: action.token,
                    };
                case 'SIGN_OUT':
                    return {
                        ...prevState,
                        isSignout: true,
                        userToken: null,
                    };
                default: {
                    throw new Error(`Unhandled action type: ${action.type}`)
                }
            }
        },
        {
            isLoading: true,
            isSignout: false,
            userToken: null,
        }
    );

    const authContext = React.useMemo(
        () => ({
            signIn: async (data: LoginUser) => {
                // In a production app, we need to send some data (usually username, password) to server and get a token
                // We will also need to handle errors if sign in failed
                // After getting token, we need to persist the token using `AsyncStorage`
                dispatch({ type: 'SIGN_IN', token: data?.token ?? null });
            },
            signOut: () => dispatch({ type: 'SIGN_OUT' }),
            signUp: async (data: LoginUser) => {
                // In a production app, we need to send user data to server and get a token
                // We will also need to handle errors if sign up failed
                // After getting token, we need to persist the token using `AsyncStorage`
                dispatch({ type: 'SIGN_IN', token: data?.token ?? null });
            },
        }),
        []
    );

    useEffect(() => {
        // use this controller to fix async warning
        const abortController = new window.AbortController();

        // Fetch the token from storage then navigate to our appropriate place
        const bootstrapAsync = async () => {
            let userToken: LoginUser
            try {
                userToken = await getStorageItem('userToken') as LoginUser

                // After restoring token, we may need to validate it in production apps
                // This will switch to the App screen or Auth screen and this loading
                // screen will be unmounted and thrown away.
                dispatch({ type: 'RESTORE_TOKEN', token: userToken?.token ?? null })
            } catch (e) {
                // Restoring token failed
            }
        }

        bootstrapAsync()

        return () => {
            abortController.abort();
        };
    }, [])

    return (
        <AuthStateContext.Provider value={state}>
            <AuthDispatchContext.Provider value={dispatch}>
                <AuthContext.Provider value={authContext}>
                    {children}
                </AuthContext.Provider>
            </AuthDispatchContext.Provider>
        </AuthStateContext.Provider>
    )
}
