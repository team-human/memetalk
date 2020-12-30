import { gql } from '@apollo/client'

export const GET_POPULAR_TAGS = gql`
  query Popular_Tags {
    popularTags
  }
`

export const GET_CURRENT_USER = gql`
  query Current_User {
    currentUser {
      id
      name
    }
  }
`

export const GET_MEMES_BY_TAG = gql`
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

export const GET_MEMES_BY_AUTHORID = gql`
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
`
