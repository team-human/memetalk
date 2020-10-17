import { gql } from '@apollo/client'

export const GET_POPULAR_TAGS = gql`
  query Popular_Tags {
    popularTags
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
