import { gql } from '@apollo/client'

export type CreateUserInput = {
    username: string
    password: string
    name: string
}
  
export const REGISTER_USER = gql`
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