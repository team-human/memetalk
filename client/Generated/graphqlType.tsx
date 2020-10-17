import { gql } from '@apollo/client'
import * as Apollo from '@apollo/client'
export type Maybe<T> = T | null
export type Exact<T extends { [key: string]: unknown }> = {
  [K in keyof T]: T[K]
}
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string
  String: string
  Boolean: boolean
  Int: number
  Float: number
}

export type MemeCounter = {
  __typename?: 'MemeCounter'
  likeCount: Scalars['Int']
  dislikeCount: Scalars['Int']
  shareCount: Scalars['Int']
  commentCount: Scalars['Int']
}

export type User = {
  __typename?: 'User'
  id: Scalars['ID']
  name: Scalars['String']
}

export type Query = {
  __typename?: 'Query'
  /**  DEPRECATED: Should use `popular_tags` + `memes(tag)` instead. */
  topics?: Maybe<Array<Topic>>
  /**  Gets the current user. */
  currentUser?: Maybe<User>
  /**  Gets popular tags. */
  popularTags?: Maybe<Array<Scalars['String']>>
  /**  Gets memes of a tag. */
  memesByTag?: Maybe<Array<Meme>>
  /**  Gets memes of a user. */
  memesByAuthorId?: Maybe<Array<Meme>>
}

export type QueryMemesByTagArgs = {
  tag: Scalars['String']
}

export type QueryMemesByAuthorIdArgs = {
  userId: Scalars['ID']
}

export type Meme = {
  __typename?: 'Meme'
  id: Scalars['ID']
  author: User
  tags?: Maybe<Array<Scalars['String']>>
  url: Scalars['String']
  createTime: Scalars['String']
  counter: MemeCounter
}

export type Mutation = {
  __typename?: 'Mutation'
  createMeme?: Maybe<Meme>
}

export type MutationCreateMemeArgs = {
  file: Scalars['String']
  tags?: Maybe<Array<Scalars['String']>>
}

/**  DEPRECATED: Should use tag and meme seperatedly. */
export type Topic = {
  __typename?: 'Topic'
  tag: Scalars['String']
  memes?: Maybe<Array<Meme>>
}

export type Popular_TagsQueryVariables = Exact<{ [key: string]: never }>

export type Popular_TagsQuery = { __typename?: 'Query' } & Pick<
  Query,
  'popularTags'
>

export type Memes_By_TagQueryVariables = Exact<{
  tag: Scalars['String']
}>

export type Memes_By_TagQuery = { __typename?: 'Query' } & {
  memesByTag?: Maybe<
    Array<
      { __typename?: 'Meme' } & Pick<
        Meme,
        'id' | 'tags' | 'url' | 'createTime'
      > & {
          author: { __typename?: 'User' } & Pick<User, 'id' | 'name'>
          counter: { __typename?: 'MemeCounter' } & Pick<
            MemeCounter,
            'likeCount' | 'dislikeCount'
          >
        }
    >
  >
}

export const Popular_TagsDocument = gql`
  query Popular_Tags {
    popularTags
  }
`

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
export function usePopular_TagsQuery(
  baseOptions?: Apollo.QueryHookOptions<
    Popular_TagsQuery,
    Popular_TagsQueryVariables
  >
) {
  return Apollo.useQuery<Popular_TagsQuery, Popular_TagsQueryVariables>(
    Popular_TagsDocument,
    baseOptions
  )
}
export function usePopular_TagsLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<
    Popular_TagsQuery,
    Popular_TagsQueryVariables
  >
) {
  return Apollo.useLazyQuery<Popular_TagsQuery, Popular_TagsQueryVariables>(
    Popular_TagsDocument,
    baseOptions
  )
}
export type Popular_TagsQueryHookResult = ReturnType<
  typeof usePopular_TagsQuery
>
export type Popular_TagsLazyQueryHookResult = ReturnType<
  typeof usePopular_TagsLazyQuery
>
export type Popular_TagsQueryResult = Apollo.QueryResult<
  Popular_TagsQuery,
  Popular_TagsQueryVariables
>
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
`

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
export function useMemes_By_TagQuery(
  baseOptions?: Apollo.QueryHookOptions<
    Memes_By_TagQuery,
    Memes_By_TagQueryVariables
  >
) {
  return Apollo.useQuery<Memes_By_TagQuery, Memes_By_TagQueryVariables>(
    Memes_By_TagDocument,
    baseOptions
  )
}
export function useMemes_By_TagLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<
    Memes_By_TagQuery,
    Memes_By_TagQueryVariables
  >
) {
  return Apollo.useLazyQuery<Memes_By_TagQuery, Memes_By_TagQueryVariables>(
    Memes_By_TagDocument,
    baseOptions
  )
}
export type Memes_By_TagQueryHookResult = ReturnType<
  typeof useMemes_By_TagQuery
>
export type Memes_By_TagLazyQueryHookResult = ReturnType<
  typeof useMemes_By_TagLazyQuery
>
export type Memes_By_TagQueryResult = Apollo.QueryResult<
  Memes_By_TagQuery,
  Memes_By_TagQueryVariables
>
