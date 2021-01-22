import { gql } from '@apollo/client';
import * as Apollo from '@apollo/client';
export type Maybe<T> = T | null;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string;
  String: string;
  Boolean: boolean;
  Int: number;
  Float: number;
  /** Generic binary filetype. */
  File: any;
};

export type MemeCounter = {
  __typename?: 'MemeCounter';
  likeCount: Scalars['Int'];
  dislikeCount: Scalars['Int'];
  shareCount: Scalars['Int'];
  commentCount: Scalars['Int'];
};

export type CreateUserInput = {
  username: Scalars['String'];
  password: Scalars['String'];
  name: Scalars['String'];
};

export type User = {
  __typename?: 'User';
  /**  a unique identifier, and currently the DB primary key */
  id: Scalars['ID'];
  /**  username is used for login */
  username: Scalars['String'];
  name: Scalars['String'];
  /**
   * The current role is ADMIN or USER. ADMIN role has a super power to delete
   * any Meme, update password, remove a user account, and so on. USER role can only delete
   * his/her Meme, update his/her passowrd.
   */
  roles?: Maybe<Array<Maybe<Scalars['String']>>>;
};

export type Query = {
  __typename?: 'Query';
  /**  Gets the current user. */
  currentUser?: Maybe<User>;
  /**  Gets popular tags. */
  popularTags?: Maybe<Array<Scalars['String']>>;
  /**  Gets memes of a tag. */
  memesByTag?: Maybe<Array<Meme>>;
  /**  Gets memes of a user. */
  memesByAuthorId?: Maybe<Array<Meme>>;
};


export type QueryMemesByTagArgs = {
  tag: Scalars['String'];
};


export type QueryMemesByAuthorIdArgs = {
  userId: Scalars['ID'];
};

export type LoginUser = {
  __typename?: 'LoginUser';
  user: User;
  token: Scalars['String'];
};

export type Meme = {
  __typename?: 'Meme';
  id: Scalars['ID'];
  author: User;
  tags?: Maybe<Array<Scalars['String']>>;
  url: Scalars['String'];
  createTime: Scalars['String'];
  /**  NOTE: This field is unsupported. Don't query this field. */
  counter: MemeCounter;
};

export type Mutation = {
  __typename?: 'Mutation';
  createMeme?: Maybe<Meme>;
  createUser?: Maybe<LoginUser>;
  login?: Maybe<LoginUser>;
};


export type MutationCreateMemeArgs = {
  file: Scalars['File'];
  tags?: Maybe<Array<Scalars['String']>>;
};


export type MutationCreateUserArgs = {
  userInfo: CreateUserInput;
};


export type MutationLoginArgs = {
  username: Scalars['String'];
  password: Scalars['String'];
};


export type Popular_TagsQueryVariables = Exact<{ [key: string]: never; }>;


export type Popular_TagsQuery = (
  { __typename?: 'Query' }
  & Pick<Query, 'popularTags'>
);

export type Current_UserQueryVariables = Exact<{ [key: string]: never; }>;


export type Current_UserQuery = (
  { __typename?: 'Query' }
  & {
    currentUser?: Maybe<(
      { __typename?: 'User' }
      & Pick<User, 'id' | 'name'>
    )>
  }
);

export type Memes_By_TagQueryVariables = Exact<{
  tag: Scalars['String'];
}>;


export type Memes_By_TagQuery = (
  { __typename?: 'Query' }
  & {
    memesByTag?: Maybe<Array<(
      { __typename?: 'Meme' }
      & Pick<Meme, 'id' | 'tags' | 'url' | 'createTime'>
      & {
        author: (
          { __typename?: 'User' }
          & Pick<User, 'id' | 'name'>
        ), counter: (
          { __typename?: 'MemeCounter' }
          & Pick<MemeCounter, 'likeCount' | 'dislikeCount'>
        )
      }
    )>>
  }
);

export type Memes_By_AuthorIdQueryVariables = Exact<{
  userId: Scalars['ID'];
}>;


export type Memes_By_AuthorIdQuery = (
  { __typename?: 'Query' }
  & {
    memesByAuthorId?: Maybe<Array<(
      { __typename?: 'Meme' }
      & Pick<Meme, 'id' | 'tags' | 'url' | 'createTime'>
      & {
        author: (
          { __typename?: 'User' }
          & Pick<User, 'id' | 'name'>
        ), counter: (
          { __typename?: 'MemeCounter' }
          & Pick<MemeCounter, 'likeCount' | 'dislikeCount'>
        )
      }
    )>>
  }
);

export type CreateUserMutationVariables = Exact<{
  userInfo: CreateUserInput;
}>;


export type CreateUserMutation = (
  { __typename?: 'Mutation' }
  & {
    createUser?: Maybe<(
      { __typename?: 'LoginUser' }
      & Pick<LoginUser, 'token'>
      & {
        user: (
          { __typename?: 'User' }
          & Pick<User, 'username' | 'name' | 'roles'>
        )
      }
    )>
  }
);

export type LoginMutationVariables = Exact<{
  username: Scalars['String'];
  password: Scalars['String'];
}>;


export type LoginMutation = (
  { __typename?: 'Mutation' }
  & {
    login?: Maybe<(
      { __typename?: 'LoginUser' }
      & Pick<LoginUser, 'token'>
      & {
        user: (
          { __typename?: 'User' }
          & Pick<User, 'username' | 'name' | 'roles'>
        )
      }
    )>
  }
);


export const Popular_TagsDocument = gql`
    query Popular_Tags {
  popularTags
}
    `;

/**
 * __usePopular_TagsQuery__
 *
 * To run a query within a React component, call `usePopular_TagsQuery` and pass it any options that fit your needs.
 * When your component renders, `usePopular_TagsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = usePopular_TagsQuery({
 *   variables: {
 *   },
 * });
 */
export function usePopular_TagsQuery(baseOptions?: Apollo.QueryHookOptions<Popular_TagsQuery, Popular_TagsQueryVariables>) {
  return Apollo.useQuery<Popular_TagsQuery, Popular_TagsQueryVariables>(Popular_TagsDocument, baseOptions);
}
export function usePopular_TagsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<Popular_TagsQuery, Popular_TagsQueryVariables>) {
  return Apollo.useLazyQuery<Popular_TagsQuery, Popular_TagsQueryVariables>(Popular_TagsDocument, baseOptions);
}
export type Popular_TagsQueryHookResult = ReturnType<typeof usePopular_TagsQuery>;
export type Popular_TagsLazyQueryHookResult = ReturnType<typeof usePopular_TagsLazyQuery>;
export type Popular_TagsQueryResult = Apollo.QueryResult<Popular_TagsQuery, Popular_TagsQueryVariables>;
export const Current_UserDocument = gql`
    query Current_User {
  currentUser {
    id
    name
  }
}
    `;

/**
 * __useCurrent_UserQuery__
 *
 * To run a query within a React component, call `useCurrent_UserQuery` and pass it any options that fit your needs.
 * When your component renders, `useCurrent_UserQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useCurrent_UserQuery({
 *   variables: {
 *   },
 * });
 */
export function useCurrent_UserQuery(baseOptions?: Apollo.QueryHookOptions<Current_UserQuery, Current_UserQueryVariables>) {
  return Apollo.useQuery<Current_UserQuery, Current_UserQueryVariables>(Current_UserDocument, baseOptions);
}
export function useCurrent_UserLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<Current_UserQuery, Current_UserQueryVariables>) {
  return Apollo.useLazyQuery<Current_UserQuery, Current_UserQueryVariables>(Current_UserDocument, baseOptions);
}
export type Current_UserQueryHookResult = ReturnType<typeof useCurrent_UserQuery>;
export type Current_UserLazyQueryHookResult = ReturnType<typeof useCurrent_UserLazyQuery>;
export type Current_UserQueryResult = Apollo.QueryResult<Current_UserQuery, Current_UserQueryVariables>;
export const Memes_By_TagDocument = gql`
    query Memes_By_Tag($tag: String!) {
  memesByTag(tag: $tag) {
    id
    author {
      id
      name
    }
    tags
    url
    createTime
    counter {
      likeCount
      dislikeCount
    }
  }
}
    `;

/**
 * __useMemes_By_TagQuery__
 *
 * To run a query within a React component, call `useMemes_By_TagQuery` and pass it any options that fit your needs.
 * When your component renders, `useMemes_By_TagQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useMemes_By_TagQuery({
 *   variables: {
 *      tag: // value for 'tag'
 *   },
 * });
 */
export function useMemes_By_TagQuery(baseOptions?: Apollo.QueryHookOptions<Memes_By_TagQuery, Memes_By_TagQueryVariables>) {
  return Apollo.useQuery<Memes_By_TagQuery, Memes_By_TagQueryVariables>(Memes_By_TagDocument, baseOptions);
}
export function useMemes_By_TagLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<Memes_By_TagQuery, Memes_By_TagQueryVariables>) {
  return Apollo.useLazyQuery<Memes_By_TagQuery, Memes_By_TagQueryVariables>(Memes_By_TagDocument, baseOptions);
}
export type Memes_By_TagQueryHookResult = ReturnType<typeof useMemes_By_TagQuery>;
export type Memes_By_TagLazyQueryHookResult = ReturnType<typeof useMemes_By_TagLazyQuery>;
export type Memes_By_TagQueryResult = Apollo.QueryResult<Memes_By_TagQuery, Memes_By_TagQueryVariables>;
export const Memes_By_AuthorIdDocument = gql`
    query Memes_By_AuthorId($userId: ID!) {
  memesByAuthorId(userId: $userId) {
    id
    author {
      id
      name
    }
    tags
    url
    createTime
    counter {
      likeCount
      dislikeCount
    }
  }
}
    `;

/**
 * __useMemes_By_AuthorIdQuery__
 *
 * To run a query within a React component, call `useMemes_By_AuthorIdQuery` and pass it any options that fit your needs.
 * When your component renders, `useMemes_By_AuthorIdQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useMemes_By_AuthorIdQuery({
 *   variables: {
 *      userId: // value for 'userId'
 *   },
 * });
 */
export function useMemes_By_AuthorIdQuery(baseOptions?: Apollo.QueryHookOptions<Memes_By_AuthorIdQuery, Memes_By_AuthorIdQueryVariables>) {
  return Apollo.useQuery<Memes_By_AuthorIdQuery, Memes_By_AuthorIdQueryVariables>(Memes_By_AuthorIdDocument, baseOptions);
}
export function useMemes_By_AuthorIdLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<Memes_By_AuthorIdQuery, Memes_By_AuthorIdQueryVariables>) {
  return Apollo.useLazyQuery<Memes_By_AuthorIdQuery, Memes_By_AuthorIdQueryVariables>(Memes_By_AuthorIdDocument, baseOptions);
}
export type Memes_By_AuthorIdQueryHookResult = ReturnType<typeof useMemes_By_AuthorIdQuery>;
export type Memes_By_AuthorIdLazyQueryHookResult = ReturnType<typeof useMemes_By_AuthorIdLazyQuery>;
export type Memes_By_AuthorIdQueryResult = Apollo.QueryResult<Memes_By_AuthorIdQuery, Memes_By_AuthorIdQueryVariables>;
export const CreateUserDocument = gql`
    mutation createUser($userInfo: CreateUserInput!) {
  createUser(userInfo: $userInfo) {
    user {
      username
      name
      roles
    }
    token
  }
}
    `;
export type CreateUserMutationFn = Apollo.MutationFunction<CreateUserMutation, CreateUserMutationVariables>;

/**
 * __useCreateUserMutation__
 *
 * To run a mutation, you first call `useCreateUserMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useCreateUserMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [createUserMutation, { data, loading, error }] = useCreateUserMutation({
 *   variables: {
 *      userInfo: // value for 'userInfo'
 *   },
 * });
 */
export function useCreateUserMutation(baseOptions?: Apollo.MutationHookOptions<CreateUserMutation, CreateUserMutationVariables>) {
  return Apollo.useMutation<CreateUserMutation, CreateUserMutationVariables>(CreateUserDocument, baseOptions);
}
export type CreateUserMutationHookResult = ReturnType<typeof useCreateUserMutation>;
export type CreateUserMutationResult = Apollo.MutationResult<CreateUserMutation>;
export type CreateUserMutationOptions = Apollo.BaseMutationOptions<CreateUserMutation, CreateUserMutationVariables>;
export const LoginDocument = gql`
    mutation login($username: String!, $password: String!) {
  login(username: $username, password: $password) {
    user {
      username
      name
      roles
    }
    token
  }
}
    `;
export type LoginMutationFn = Apollo.MutationFunction<LoginMutation, LoginMutationVariables>;

/**
 * __useLoginMutation__
 *
 * To run a mutation, you first call `useLoginMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useLoginMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [loginMutation, { data, loading, error }] = useLoginMutation({
 *   variables: {
 *      username: // value for 'username'
 *      password: // value for 'password'
 *   },
 * });
 */
export function useLoginMutation(baseOptions?: Apollo.MutationHookOptions<LoginMutation, LoginMutationVariables>) {
  return Apollo.useMutation<LoginMutation, LoginMutationVariables>(LoginDocument, baseOptions);
}
export type LoginMutationHookResult = ReturnType<typeof useLoginMutation>;
export type LoginMutationResult = Apollo.MutationResult<LoginMutation>;
export type LoginMutationOptions = Apollo.BaseMutationOptions<LoginMutation, LoginMutationVariables>;