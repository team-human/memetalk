import { gql } from '@apollo/client'

export type CreateUserInput = {
    username: string
    password: string
    name: string
}

export const SIGNUP_USER = gql`
    mutation createUser ($userInfo: CreateUserInput!) {
        createUser (userInfo: $userInfo) {
            user {
                username
                name
                roles
            }
            token
        }
    }
`

export const SIGNIN_USER = gql`
    mutation login ($username: String!, $password: String!) {
        login (username: $username, password: $password) {
            user {
                username
                name
                roles
            }
            token
        }
    }
`